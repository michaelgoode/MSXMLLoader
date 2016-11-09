/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FileHandler;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class HttpFileDownload {
    private static final Logger LOG = Logger.getLogger(HttpFileDownload.class.getName());
    
    

    public void getFile(String sURL, String filename, String user, String password) {
        int i;
        try {
            URL url = new URL(sURL);

            String authStr = user + ":" + password;
            String authEncoded = Base64.encode(authStr.getBytes());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + authEncoded);
            
            File file = new File(filename);
            
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(file.getAbsolutePath()));
            
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
            
            bos.flush();
            bis.close();
            
        } catch (MalformedInputException malformedInputException) {
            LOG.log(Level.SEVERE, null, malformedInputException);
            
        } catch (IOException ioException) {
            LOG.log(Level.SEVERE, null, ioException);
        }
    }
}
