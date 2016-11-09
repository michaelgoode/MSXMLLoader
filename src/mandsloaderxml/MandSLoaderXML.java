/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mandsloaderxml;

import FileHandler.HttpFileDownload;
import FileHandler.JakartaFtpWrapper;
import Properties.ConfigProperties;
import Properties.FTPProperties;
import Properties.HttpProperties;
import Utilities.UnZip;
import xmlreader.StrokeXMLParser;
import entities.Stroke;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import managers.SKUOutputManager;
import managers.StrokeManager;
import managers.CalloutLists;
import managers.DBManager;
import managers.SizeCodeManager;
import java.sql.Connection;

/**
 *
 * @author michaelgoode
 */
public class MandSLoaderXML {

    private static final Logger LOG = Logger.getLogger(MandSLoaderXML.class.getName());
    private static final String version = "MANDSLoaderXML Version 6.0.1 created by Michael Goode (09-11-2016)"; 

    static FTPProperties ftpProperties = new FTPProperties();
    static ConfigProperties configProperties = new ConfigProperties();
    static HttpProperties httpProperties = new HttpProperties();
    static CalloutLists callouts = new CalloutLists();
    static SizeCodeManager sizeCodeManager = new SizeCodeManager();
    static StrokeManager strokeManager = new StrokeManager();

    public static void main(String[] args) {
        
        System.out.println(version);
        
        Initialise();
        HashMap fileparams = getHttpFiles();
        if (fileparams.isEmpty()) {
            fileparams = getFilesFromLocalFolder();
        }
        processFiles(fileparams);
    }

    private static void processFiles(HashMap fileparams) {

        ArrayList<Stroke> strokes = null;
        StrokeXMLParser strokeParser = null;
        SKUOutputManager skuOutputManager = null;
        String outFileName = null;
        File xmlFile = null;
        File file = new File(configProperties.getInputFolder());
        File[] files = file.listFiles();
        Iterator paramIter = fileparams.keySet().iterator();
        while (paramIter.hasNext()) {
            String xmlfilename = (String) paramIter.next();
            String route = (String) fileparams.get(xmlfilename);
            if ((!xmlfilename.equals("")) && (xmlfilename.toUpperCase().endsWith("XML")) && (xmlfilename.contains(route))) {
                xmlFile = new File(xmlfilename);
                LOG.info(String.format("Start file %s", xmlFile.getName()));
                strokeParser = new StrokeXMLParser();
                strokes = strokeParser.parseXML_SAX(xmlFile, strokeManager);
                if ((strokes.size() > 0)) {
                    skuOutputManager = new SKUOutputManager();
                    outFileName = xmlFile.getName().toUpperCase();
                    String timeStamp = new SimpleDateFormat("YYYY_MM_dd_HH_mm_ss").format(new Date());
                    outFileName = outFileName.substring(0, outFileName.indexOf(".")) + "_" + timeStamp + ".TXT";
                    skuOutputManager.writeCSV(configProperties.getWorkingFolder() + outFileName, strokes, callouts, sizeCodeManager, route);
                    if (ftpProperties.getGetftpFile().toUpperCase().equals("TRUE")) {
                        sendToFTP(configProperties.getWorkingFolder() + outFileName);
                        LOG.info("Total read strokes =" + strokes.size() + "        " + xmlfilename);
                    }
                    strokeManager.commitStrokes(strokes);
                    moveFileToArchive(configProperties.getArchiveFolder(), configProperties.getWorkingFolder() + outFileName);
                    moveFileToArchive(configProperties.getMsArchiveFolder(), configProperties.getInputFolder() + xmlFile.getName());
                }
            }
        }
    }

    private static void Initialise() {
        loadProperties();
    }

    private static HashMap getHttpFiles() {
        String fname = "";
        HashMap<String, String> fileparams = new HashMap();
        if (httpProperties.getGetFileFromHttp().toUpperCase().equals("TRUE")) {
            fname = getFileFromHttp(httpProperties.getHttpURL() + httpProperties.getFilename(), httpProperties.getUser(), httpProperties.getPassword(), configProperties.getInputFolder(), "CMS_Latest.zip", "CMSLabel");
            if (!fname.equals("")) {
                fileparams.put(fname, "CMS");
            }
            LOG.log(Level.INFO, "Got file " + fname);
            fname = getFileFromHttp(httpProperties.getPlmHttpURL() + httpProperties.getFilename(), httpProperties.getPlmuser(), httpProperties.getPlmpassword(), configProperties.getInputFolder(), "PLM_Latest.zip", "PLMLabel");
            if (!fname.equals("")) {
                fileparams.put(fname, "PLM");
            }
            LOG.log(Level.INFO, "Got file " + fname);
        } else {
            fname = "";
            LOG.log(Level.INFO, null, "No files found");
        }
        return fileparams;
    }

    public static void loadProperties() {
        // get the settings from the config files
        configProperties.loadProperties();
        httpProperties.loadProperties();
        ftpProperties.loadProperties();
    }

    public static void sendToFTP(String filename) {

        JakartaFtpWrapper ftp = new JakartaFtpWrapper();

        try {

            if (ftp.connectAndLogin(ftpProperties.getHost(), ftpProperties.getUsername(), ftpProperties.getPassword())) {

                File f = new File(filename);

                //String tempfname = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf(".")) + ".tmp";

                if (ftp.uploadFile(filename, f.getName())) {

                    //ftp.rename(new File(tempfname).getName(), f.getName());

                    LOG.info(String.format("File %s sent to host %s, user %s , %s", f.getName(), ftpProperties.getHost(), ftpProperties.getUsername(), ftpProperties.getPassword()));

                } else {

                    LOG.info(String.format("File %s is NOT sent to host %s, user %s, %s", f.getName(), ftpProperties.getHost(), ftpProperties.getUsername(), ftpProperties.getPassword()));

                }
            } else {
                
                LOG.info(String.format("UNABLE TO CONNECT TO FTP : %s, %s", ftpProperties.getHost(), ftpProperties.getUsername()));

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void moveFileToArchive(String archivePath, String filename) {

        File folder = new File(archivePath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File inFile = new File(filename);

        String fileName = inFile.getName();

        File outFile = new File(archivePath + fileName);

        if (outFile.exists()) {
            outFile.delete();
        }

        try {
            Files.copy(inFile.toPath(), outFile.toPath());
        } catch (IOException ex) {
            Logger.getLogger(MandSLoaderXML.class.getName()).log(Level.SEVERE, null, ex);
        }

        inFile.delete(); // remove the file from the working folder

    }

    public static String getFileFromHttp(String url, String user, String password, String inputPath, String filename, String filemask) {

        HttpFileDownload http = new HttpFileDownload();

        String path = inputPath;

        http.getFile(url, path + filename, user, password);

        return getXMLFileFromArchive(path, filename, filemask);

    }

    public static String getXMLFileFromArchive(String path, String inFile, String filemask) {
        try {
            HashMap<String, String> filenames = new HashMap();

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path + inFile));

            UnZip unzip = new UnZip(in, new File(path));

            String xmlFilename = "";

            File dir = new File(path);

            File[] files = dir.listFiles();

            for (File file : files) {

                if ((!file.isDirectory()) && (file.getName().contains("xml")) && file.getName().startsWith(filemask)) {

                    xmlFilename = file.getAbsolutePath();

                }

            }

            return xmlFilename;

        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }

    }

    public static HashMap getFilesFromLocalFolder() {

        HashMap<String, String> fileparams = new HashMap();

        File dir = new File(configProperties.getInputFolder());
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.getName().startsWith("PLM")) {
                fileparams.put(f.getAbsolutePath(), "PLM");
            } else if (f.getName().startsWith("CMS")) {
                fileparams.put(f.getAbsolutePath(), "CMS");
            }
        }
        //fileparams.put("C:\\MandS\\Input\\PLMLabelCLA_20160127.xml", "PLM");
        return fileparams;
    }

}
