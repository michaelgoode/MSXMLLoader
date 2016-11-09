/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class SizeCodeManager {
    private static final Logger LOG = Logger.getLogger(SizeCodeManager.class.getName());

    private ArrayList<String> sizeCodes = new ArrayList<String>();

    public SizeCodeManager() {
        Connection conn = null;
        try {
            DBManager db = DBManager.getInstance();
            conn = db.getConnection(ConnectionType.LOOKUP);
            String sql = "Select Size_Code from MS_SizeTable with (NOLOCK)";
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement(sql);
                ResultSet rs;
                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        sizeCodes.add(rs.getString("Size_code").toUpperCase());
                    } catch (SQLException ex) {
                        LOG.log(Level.SEVERE,null,ex);
                    }
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE,null,ex);
            }

        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE,null,ex);
            }
        }
    }
    
    public boolean sizeCodeExists( String s ) {
        return this.sizeCodes.contains(s.toUpperCase());
    }
}
