/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class ConfigProperties extends FileProperties {
    private static final Logger LOG = Logger.getLogger(ConfigProperties.class.getName());

    String propFileName = "config.properties";
    
    String workingFolder = "C:\\MandS\\Working";
    String inputFolder = "C:\\MandS\\Input";
    String dtd = "C:\\MandS\\Support Files\\Label-Information modified.dtd";
    String archiveFolder = "C:\\MandS\\Archive\\";
    String msArchiveFolder = "C:\\MandS\\MSArchive\\";
    

    public void loadProperties() {

        try (FileReader reader = new FileReader(propFileName)) {
            Properties prop = new Properties();
            if (reader != null) {
                prop.load(reader);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            workingFolder = prop.getProperty("working folder");
            inputFolder = prop.getProperty("input folder");
            dtd = prop.getProperty("dtd");
            archiveFolder = prop.getProperty("archive folder");
            msArchiveFolder = prop.getProperty("ms archive folder");

        } catch (IOException ex) {
            this.saveProperties();
            LOG.log(Level.SEVERE,null,ex);
        }
    }

    public void saveProperties() {
        try (FileWriter writer = new FileWriter(propFileName)) {
            Properties prop = new Properties();
            prop.setProperty("working folder", workingFolder);
            prop.setProperty("input folder", inputFolder);
            prop.setProperty("dtd", dtd);
            prop.setProperty("archive folder", archiveFolder);
            prop.setProperty("ms archive folder", msArchiveFolder);
            prop.store(writer, null);
            writer.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE,null,ex);
        }
    }

    public String getWorkingFolder() {
        return buildPath(workingFolder);
    }

    public void setWorkingFolder(String workingFolder) {
        this.workingFolder = workingFolder;
    }

    public String getInputFolder() {
        return buildPath(inputFolder);
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public String getDtd() {
        return dtd;
    }

    public void setDtd(String dtd) {
        this.dtd = dtd;
    }

    public String getArchiveFolder() {
        return archiveFolder;
    }

    public void setArchiveFolder(String archiveFolder) {
        this.archiveFolder = archiveFolder;
    }

    public String getMsArchiveFolder() {
        return msArchiveFolder;
    }

    public void setMsArchiveFolder(String msArchiveFolder) {
        this.msArchiveFolder = msArchiveFolder;
    }
    
    
  
}
