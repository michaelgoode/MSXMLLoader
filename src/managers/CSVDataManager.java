/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Stroke;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class CSVDataManager {

    private static CSVDataManager dm = null;

    private Connection conn = null;

    private CSVDataManager() {

        DBManager db = DBManager.getInstance();
        conn = db.getCSVTXTConnection();

    }

    public static CSVDataManager getInstance() {
        if (dm == null) {
            dm = new CSVDataManager();
        }
        return dm;
    }

    public boolean strokeCompare(Stroke stroke, String route) {
        boolean VALIDATE_ORDER = true; // set for FALSE for debugging only
        if (this.contractExistsInVerificationData(stroke, VALIDATE_ORDER)) {
            if (checkHeader(stroke, route, VALIDATE_ORDER)) {
                // found a match
                return true; // the contract is fully valid
            } else {
                return false; // failed to validate but does exist
            }
        } else if (validStatus(stroke)) {
            return true; // force to be valid as we have no reference to the contract in the verification file
        } else {
            return false; 
        }
    }

    private boolean checkHeader(Stroke stroke, String route, boolean ValidateOrder) {
        if (ValidateOrder) {
            String sql = "select * from MSMS_TXTDATA where Upper(file_type) =? and Upper(stroke_num)=? and Upper(stroke_desc)=? and Upper(contract)=? and Upper(contract_status)=? and Upper(dept)=? and Upper(season)=? and Upper(series)=? and Upper(country)=? and Upper(product_desc)=?";
            PreparedStatement ps;
            boolean strokeMatch = false;
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, route.toUpperCase());
                ps.setString(2, stroke.getStrokeNumber().toUpperCase());
                ps.setString(3, stroke.getStrokeDescription().toUpperCase());
                ps.setString(4, stroke.getContractNumber().toUpperCase());
                ps.setString(5, stroke.getContractStatus().toUpperCase());
                ps.setString(6, stroke.getDepartmentNumber().toUpperCase());
                ps.setString(7, stroke.getSeason().toUpperCase());
                ps.setString(8, stroke.getSupplierSeries().toUpperCase());
                ps.setString(9, stroke.getCountryCode().toUpperCase());
                ps.setString(10, stroke.getProductDesc().toUpperCase());
                ResultSet rs = ps.executeQuery();

                ArrayList<String> textCallouts = new ArrayList<String>();
                ArrayList<String> textUPC = new ArrayList<String>();
                ArrayList<String> strokeCallouts = stroke.getRFIDCallouts(); // get a list of all items marked for RFID from xml
                ArrayList<String> strokeUPC = stroke.getUPCList();
                while (rs.next()) { // we must have a match
                    strokeMatch = true;
                    textCallouts.add(rs.getString("packing_ref"));
                    textUPC.add(rs.getString("upc"));
                }
                if (strokeMatch) {
                    if (checkCalloutLists(strokeCallouts, textCallouts, stroke)) {
                        return checkLists(strokeUPC, textUPC);
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(CSVDataManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            System.out.println("THE VALIDATION OF AN ORDER HAS BEEN BYPASSED....PLEASE CHECK BEFORE GOING LIVE");
            return true;
        }
    }

    private boolean contractExistsInVerificationData(Stroke stroke, boolean validate) {
        // check the contract exists, if not check the status and return the contract accordingly
        if (validate) {
            String sql = "select contract from MSMS_TXTDATA where Upper(contract) = ?";
            PreparedStatement ps;
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, stroke.getContractNumber().toUpperCase());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return true;
                } /*else {
                 List<String> validStatus = Arrays.asList("U", "L", "C", "A");
                 if (validStatus.contains(stroke.getContractStatus())) {
                 return true;
                 } else {
                 return false;
                 }
                 }*/ else {
                    return false;
                }

            } catch (SQLException ex) {
                Logger.getLogger(CSVDataManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean validStatus(Stroke stroke) {
        List<String> validStatus = Arrays.asList("U", "L", "C", "A");
        if (validStatus.contains(stroke.getContractStatus().toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkLists(ArrayList strokeList, ArrayList textList) {
        boolean found = false;
        int foundCount = 0;
        try {
            Iterator textIter = textList.iterator();
            while ((textIter.hasNext())) {
                String value = (String) textIter.next();
                if (strokeList.contains(value)) {
                    foundCount++;
                }
            }
            if (foundCount == textList.size()) {
                found = true;
            } else {
                found = false;
            }

        } finally {
            return found;
        }
    }

    private boolean checkCalloutLists(ArrayList strokeList, ArrayList textList, Stroke stroke) {
        boolean found = false;
        int foundCount = 0;
        try {
            Iterator textIter = textList.iterator();
            while ((textIter.hasNext())) {
                String value = (String) textIter.next();
                if (strokeList.contains(value)) {
                    foundCount++;
                    stroke.rfidCalloutList.add(value);
                }
            }

            if (foundCount == textList.size()) {
                found = true;
            } else {
                found = false;
            }

        } finally {
            return found;
        }
    }
}
