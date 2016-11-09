/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class DBManager {
    private static final Logger LOG = Logger.getLogger(DBManager.class.getName());

    private static final DBManager instance = new DBManager();

    public static DBManager getInstance() {
        return instance;
    }

    public DBManager() {

    }

    public Connection getConnection(String connType) {
        Connection conn = null;
        if (connType.equals("EDI")) {
            conn = getEDIConnection();
        } else if (connType.equals("LOOKUP")) {
            conn = getLookupConnection();
        }
        return conn;
    }
    
    public Connection getLookupConnection() {

        Connection con = null;

        String RL = "jdbc:sqlserver://192.168.0.65:1489;DatabaseName=lookup";

        String user = "lookup_admin";

        String password = "sbit";
        
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
    
    public Connection getStrokeCacheConnection() {

        Connection con = null;
        
        //System.out.println("WARNING THIS IS NOT CONNECTING TO THE LIVE CACHE");

        //String RL = "jdbc:sqlserver://192.168.0.65:1489;DatabaseName=lookup";
        
        String RL = "jdbc:sqlserver://192.168.2.8:1433;DatabaseName=OrderPool";
        
        System.out.println("WARNING THIS IS IS CONNECTING TO " + RL);

        String user = "lookup_admin";

        String password = "sbit";
        
        user = "sa";
        
        password = "";

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
    
    public Connection getStrokeTestCacheConnection() {

        Connection con = null;
        
        String RL = "jdbc:sqlserver://192.168.2.8:1433;DatabaseName=MSMSTest";
        
        System.out.println("WARNING THIS IS IS CONNECTING TO " + RL);

        String user = "sa";

        String password = "";

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
    
    public Connection getCSVTXTConnection() {

        Connection con = null;
        
        System.out.println("WARNING THIS IS NOT CONNECTING TO THE LIVE CSV DATA");

        //String RL = "jdbc:sqlserver://192.168.0.65:1489;DatabaseName=lookup";
        
        String RL = "jdbc:sqlserver://192.168.2.8:1433;DatabaseName=OrderPool";

        String user = "lookup_admin";

        String password = "sbit";
        
        
        user = "sa";
        
        password = "";

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
    
    public Connection getEDIConnection() {

        Connection con = null;

        String RL = "jdbc:sqlserver://192.168.0.52:1488;DatabaseName=edi_eplatform";

        String user = "sbitapp";

        String password = "sbit";

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(RL, user, password);

        } catch (Exception e) {
            LOG.log(Level.SEVERE,null,e);
        }
        return con;

    }
}
