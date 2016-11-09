/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class FTPProperties extends FileProperties {
    private static final Logger LOG = Logger.getLogger(FTPProperties.class.getName());

    String host = "ftp.sml.com";
    String username = "SMLEDI_MandS";
    String password = "EDI310";
    String folder = "";
    String getftpFile = "false";
    
    String propFileName = "ftp.properties";

    public void loadProperties() {

        String result = "";
        

        try (FileReader reader = new FileReader(propFileName)) {

            Properties prop = new Properties();
            if (reader != null) {
                prop.load(reader);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            host = prop.getProperty("host");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            folder = prop.getProperty("folder");
            getftpFile = prop.getProperty("getftpFile");

        } catch (IOException ex) {
            this.saveProperties();
            LOG.log(Level.SEVERE,null,ex);
        }
    }


    public void saveProperties() {
        try (FileWriter writer = new FileWriter(propFileName)) {
            Properties prop = new Properties();
            prop.setProperty("host", host);
            prop.setProperty("username", username);
            prop.setProperty("password", password);
            prop.setProperty("folder", folder);
            prop.setProperty("getftpFile", getftpFile);
            prop.store(writer, null);
            writer.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE,null,ex);
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFolder() {
        return buildPath(folder);
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getGetftpFile() {
        return getftpFile;
    }

    public void setGetftpFile(String getftpFile) {
        this.getftpFile = getftpFile;
    }


    
    
}
