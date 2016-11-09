/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class StrokeFilter {
    private static final Logger LOG = Logger.getLogger(StrokeFilter.class.getName());
    
    ArrayList<String> suppliers = new ArrayList<String>();
    
    public StrokeFilter(Connection conn) {
        loadSupplierList(conn);
    }
    
    private void loadSupplierList(Connection conn) {
        
        try {

            String sql = "select * from MS_Suppliers with (NOLOCK) where Active = 1";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            
            suppliers.clear();

            while ( rs.next() ) {

                suppliers.add(rs.getString("Supplier"));

            }

        } catch (SQLException ex) {

            LOG.log(Level.SEVERE,null,ex);

        }
        
    }
    
    public boolean supplierExist( String supplierNo ) {
        if ( suppliers.contains(supplierNo) ) {
            return true;
        } else { 
            return false; 
        }
    }
    
}
