/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.UKProduct;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class CalloutLists {
    private static final Logger LOG = Logger.getLogger(CalloutLists.class.getName());
    
    

    private static final CalloutLists instance = new CalloutLists();

    public static CalloutLists getInstance() {
        return instance;
    }
    
    public ArrayList<String> validCallouts = new ArrayList<String>();
    public HashMap<String, UKProduct> ukProducts = new HashMap<String, UKProduct>();
    public ArrayList<String> internationalProducts = new ArrayList<String>();

    public CalloutLists() {
        getValidCallouts();
        getUKProducts();
        getInternationalProducts();
    }
    
    public boolean isValidCallout( String strCallout ) {
        return validCallouts.contains(strCallout);
    }
    
    public UKProduct getUKProduct( String callout ) {
        return ukProducts.get(callout);
    }

    private void getValidCallouts() {
        Connection conn = null;

        try {
            DBManager db = DBManager.getInstance();

            conn = db.getEDIConnection();

            String sql = "select * from edi_lookup_value with (NOLOCK) where FK_lookup_ConditionID=267";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            validCallouts.clear();

            while (rs.next()) {

                validCallouts.add(rs.getString("CValue1").toUpperCase().trim());

            }

            conn.close();

        } catch (SQLException e) {
            LOG.log(Level.SEVERE,null,e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e1) {
                LOG.log(Level.SEVERE,null,e1);
            }
        }
    }

    private void getUKProducts() {
        Connection conn = null;

        try {
            DBManager db = DBManager.getInstance();

            conn = db.getEDIConnection();

            String sql = "select distinct CValue1,CValue3 from edi_lookup_value with (NOLOCK) where FK_lookup_ConditionID=267 and CValue1 <> CValue3 and CValue3 <> 'NONE' and CValue3 <> ''";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            ukProducts.clear();

            while (rs.next()) {
                UKProduct ukproduct = new UKProduct();
                ukproduct.setCallout(rs.getString("CValue1").toUpperCase().trim());
                ukproduct.setInternationalCallout(rs.getString("CValue3").toUpperCase().trim());
                ukProducts.put(ukproduct.getCallout(), ukproduct);
            }

            conn.close();

        } catch (SQLException e) {
            LOG.log(Level.SEVERE,null,e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e1) {
                LOG.log(Level.SEVERE,null,e1);
            }
        }
    }
    
    private void getInternationalProducts() {
        Connection conn = null;

        try {
            DBManager db = DBManager.getInstance();

            conn = db.getEDIConnection();

            String sql = "select distinct CValue1 from edi_lookup_value with (NOLOCK) where FK_lookup_ConditionID=267 and CValue1 = CValue3";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            internationalProducts.clear();

            while (rs.next()) {
                internationalProducts.add(rs.getString("CValue1").toUpperCase().trim());
            }

            conn.close();

        } catch (SQLException e) {
            LOG.log(Level.SEVERE,null,e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e1) {
                LOG.log(Level.SEVERE,null,e1);
            }
        }
    }
}
