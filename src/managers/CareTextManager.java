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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class CareTextManager {

    private static final CareTextManager instance = new CareTextManager();

    private static final Logger LOG = Logger.getLogger(SizeCodeManager.class.getName());

    private HashMap<String, String> careTexts = new HashMap<String, String>();

    public static CareTextManager getInstance() {
        return instance;
    }

    public CareTextManager() {
        Connection conn = null;
        try {
            DBManager db = DBManager.getInstance();
            conn = db.getConnection(ConnectionType.LOOKUP);
            String sql = "Select CODE, Type from MS_CareInstr with (NOLOCK)";
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement(sql);
                ResultSet rs;
                rs = ps.executeQuery();
                while (rs.next()) {
                    try {
                        careTexts.put(rs.getString("CODE").toUpperCase(), rs.getString("Type").toUpperCase());
                    } catch (SQLException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getCareTextPosition(String s) {
        try {
            String position;
            int pos;
            position = this.careTexts.get(s);
            pos = Integer.parseInt(position) + 1000;
            return pos;
        } catch (Exception ex) {
            return -1;
        }
    }

}
