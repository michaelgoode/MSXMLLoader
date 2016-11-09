/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Properties;

/**
 *
 * @author michaelgoode
 */
public abstract class FileProperties {
    
    public abstract void loadProperties();
    public abstract void saveProperties();
    
    public String buildPath( String path ) {
        if (!path.endsWith("\\")) {
            path = path + "\\";
        }
        return path;
    }
    
}
