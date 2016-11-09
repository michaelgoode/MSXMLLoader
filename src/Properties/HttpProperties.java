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
public class HttpProperties extends FileProperties {
    private static final Logger LOG = Logger.getLogger(HttpProperties.class.getName());

    String HttpURL = "http://www.mandsgmpackaging.com/cms/";
    String path = "C:\\MandS\\Input\\";
    String filename = "latest.zip";
    String user = "cms-download";
    String password = "Bj7oeaO3GZ2y6";
    String getFileFromHttp = "false";
    
    String plmuser = "plm-download";
    String plmpassword = "Bj7oeaO3GZ2y6";
    String plmHttpURL = "http://www.mandsgmpackaging.com/plm/";
    
    String propFileName = "http.properties";

    public void loadProperties() {
        
        try (FileReader reader = new FileReader(propFileName)) {

            Properties prop = new Properties();
            if (reader != null) {
                prop.load(reader);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            HttpURL = prop.getProperty("httpurl");
            path = prop.getProperty("path");
            filename = prop.getProperty("filename");
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            getFileFromHttp = prop.getProperty("getFileFromHttp");
            this.plmHttpURL = prop.getProperty("plmhttpurl");
            this.plmuser = prop.getProperty("plmuser");
            this.plmpassword = prop.getProperty("plmpassword");
            
        } catch (IOException ex) {
            this.saveProperties();
            LOG.log(Level.SEVERE,null,ex);
        }
    }

    public void saveProperties() {
        try (FileWriter writer = new FileWriter(propFileName)) {
            Properties prop = new Properties();
            prop.setProperty("httpurl", HttpURL);
            prop.setProperty("path", path);
            prop.setProperty("filename", filename);
            prop.setProperty("user", user);
            prop.setProperty("password", password);
            prop.setProperty("getFileFromHttp", getFileFromHttp);
            prop.setProperty("plmhttpurl", this.plmHttpURL);
            prop.setProperty("plmuser", this.plmuser);
            prop.setProperty("plmpassword", this.plmpassword);
            prop.store(writer, null);
            writer.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE,null,ex);
        }
    }

    public String getHttpURL() {
        return HttpURL;
    }

    public void setHttpURL(String HttpURL) {
        this.HttpURL = HttpURL;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGetFileFromHttp() {
        return getFileFromHttp;
    }

    public void setGetFileFromHttp(String getFileFromHttp) {
        this.getFileFromHttp = getFileFromHttp;
    }

    public String getPlmuser() {
        return plmuser;
    }

    public void setPlmuser(String plmuser) {
        this.plmuser = plmuser;
    }

    public String getPlmpassword() {
        return plmpassword;
    }

    public void setPlmpassword(String plmpassword) {
        this.plmpassword = plmpassword;
    }

    public String getPlmHttpURL() {
        return plmHttpURL;
    }

    public void setPlmHttpURL(String plmHttpURL) {
        this.plmHttpURL = plmHttpURL;
    }
    
    
    
    
}
