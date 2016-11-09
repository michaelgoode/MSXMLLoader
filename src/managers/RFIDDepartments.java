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
public class RFIDDepartments {

    private static final Logger LOG = Logger.getLogger(RFIDDepartments.class.getName());

    private static final RFIDDepartments instance = new RFIDDepartments();

    ArrayList departments = new ArrayList<String>();

    public static RFIDDepartments getInstance() {
        return instance;
    }
    
    public RFIDDepartments() {
        loadDeptList();
    }
    
    public boolean contains( String dept ) {
        return departments.contains(dept.trim());
    }

    private void loadDeptList() {
        
        Connection conn = null;

        try {
            
            DBManager db = new DBManager();
            
            conn = db.getLookupConnection();

            String sql = "select * from MSMS_Depts with (NOLOCK) where [Type]='RFID' and Active = 1";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            departments.clear();

            while (rs.next()) {

                departments.add(rs.getString("DeptCode"));

            }

        } catch (SQLException ex) {

            LOG.log(Level.SEVERE, null, ex);

        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(RFIDDepartments.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
