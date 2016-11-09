/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author michaelgoode
 */
public class UnZip {

        List<String> fileList;

        public UnZip(InputStream stream, File dest) {
            unZipIt(stream, dest); // the stream i got from the first part and dest as a file
        }

        /**
         * Unzip it
         *
         * @param stream input zip file
         * @param output zip file output folder
         */
        public void unZipIt(InputStream stream, File dest) {

            byte[] buffer = new byte[1024];

            try {

                //create output directory is not exists
                File folder = dest;
                if (!folder.exists()) {
                    folder.mkdir();
                }

                //get the zip file content
                ZipInputStream zis =
                        new ZipInputStream(stream);
                //get the zipped file list entry
                ZipEntry ze = zis.getNextEntry();

                while (ze != null) {

                    String fileName = ze.getName();
                    File newFile = new File(dest + File.separator + fileName);


                    //create all non exists folders
                    //else you will hit FileNotFoundException for compressed folder

                    if (ze.isDirectory()) {
                        new File(newFile.getParent()).mkdirs();
                    } else {
                        FileOutputStream fos = null;

                        new File(newFile.getParent()).mkdirs();

                        fos = new FileOutputStream(newFile);

                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }

                        fos.close();
                    }

                    ze = zis.getNextEntry();
                }

                zis.closeEntry();
                zis.close();

          

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

