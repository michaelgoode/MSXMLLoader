/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import Utilities.References;
import entities.Stroke;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class ContractCache {

    private static final ContractCache instance = new ContractCache();

    private static final Logger LOG = Logger.getLogger(ContractCache.class.getName());

    Connection theConnection;

    HashMap<String, Contract> contracts = new HashMap<String, Contract>();

    public static ContractCache getInstance() {
        return instance;
    }

    public ContractCache() {
        DBManager db = DBManager.getInstance();
        theConnection = db.getStrokeCacheConnection();
        loadContracts(theConnection); // get the existing contracts from the db
    }

    public boolean add(String dept, Contract acontract) {
        if (this.newContract(acontract)) {
            return this.addContract(acontract);
        } else {
            return this.modifiedContract(dept, acontract);
        }
    }

    private void loadContracts(Connection conn) {

        try {

            String sql = "select * from MSMS_STROKE_CACHE with (NOLOCK)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Contract contract = new Contract();
                contract.setContract(rs.getString("Contract"));
                contract.setDateModified(rs.getString("DateModified"));
                contract.setChecksum(rs.getString("Hash"));
                contract.setChecksum2(rs.getString("Checksum2"));
                contract.setStatus(rs.getString("Status"));
                contracts.put(contract.getContract(), contract);
            }

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public boolean addContract(Contract contract) {

        try {

            if (contract != null && contract.getStroke() != null) {

                contracts.put(contract.getContract(), contract);
                contract.setStatus("NEW");

                String sql = "insert into MSMS_STROKE_CACHE (Contract,DateModified,Hash,Checksum2,[count],rowUpdate,Status) values (?,?,?,?,1,GetDate(),?)";

                PreparedStatement stmt = theConnection.prepareStatement(sql);

                theConnection.setAutoCommit(false);

                stmt.setString(1, contract.getContract());
                stmt.setString(2, contract.getDateModified());
                stmt.setString(3, References.TRANSIENT_CONTRACT);
                stmt.setString(4, contract.getChecksum2());
                stmt.setString(5, "NEW");

                stmt.executeUpdate();

                LOG.info(String.format("Added contract %s", contract.getContract()));

                theConnection.commit();

                return true;

            } else {
                return false;
            }

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            try {
                theConnection.rollback();
                return false;
            } catch (SQLException ex1) {
                LOG.log(Level.SEVERE, null, ex1);
                return false;
            }
        }
    }

    public Contract getCurrentContract(String contract) {

        return this.contracts.get(contract);

        /*
         if (aContract != null) {
         return aContract.getStatus();
         } else {
         return null;
         }*/

        /*
        
        
        
        
        
        
         PreparedStatement ps = null;
         String status = "";
         String select = "select Status from MSMS_STROKE_CACHE where Contract=?";
         //String sql = "update MSMS_STROKE_CACHE set Hash = ?, Status=? where Contract=?";
         try {

         ps = theConnection.prepareStatement(select);
         ps.setString(1, contract);

         ResultSet rs = ps.executeQuery();
         if (rs.next()) {
         status = rs.getString("Status").trim();
         }
         } catch (SQLException ex) {

         } finally {
         return status;
         }
         */
    }

    public String updateRaiseStatus(String contract, String newStatus) {

        Contract aContract = this.contracts.get(contract);
        aContract.setStatus(newStatus);
        aContract.setChecksum(References.TRANSIENT_CONTRACT);
        return aContract.getStatus();
    }

    public boolean modifiedContract(String dept, Contract contract) {
        boolean result = false;
        RFIDDepartments depts = RFIDDepartments.getInstance();
        try {
            if (contract != null) {
                //if (depts.contains(dept)) { // the department is valid
                Contract temp = this.contracts.get(contract.getContract());
                if ((!temp.getChecksum2().equals(contract.getChecksum2())) || (temp.getStatus().equals("NORAISE"))) {
                    if (!temp.getChecksum2().equals(contract.getChecksum2())) {
                        temp.setChecksumChanged(true);
                        LOG.info(contract.getContract() + " checksum changed");
                    } else {
                        temp.setChecksumChanged(false);
                    }
                    temp.setChecksum2(contract.getChecksum2());
                    String sql = "update MSMS_STROKE_CACHE set Hash = ?, Checksum2 = ?, count = count + 1, rowUpdate = GetDate(), Status=? where Contract=?";
                    PreparedStatement stmt = theConnection.prepareStatement(sql);
                    theConnection.setAutoCommit(false);
                    stmt.setString(1, References.TRANSIENT_CONTRACT);
                    stmt.setString(2, temp.getChecksum2());
                    if (temp.getStatus().equals("NORAISE")) {
                        stmt.setString(3, "NORAISE");
                    } else {
                        stmt.setString(3, "NEW");
                    }
                    stmt.setString(4, temp.getContract());
                    stmt.executeUpdate();
                    theConnection.commit();
                    //LOG.info(String.format("Modified contract %s", contract.getContract()));
                    result = true;
                }
                if (!temp.isChecksumChanged()) {
                    if ((temp.getStatus().trim().equals("NORAISE")) && (depts.contains(dept))) {
                        result = true;
                    } else if ((temp.getStatus().trim().equals("NEW")) && (depts.contains(dept))) {
                        result = true;
                    } else {
                        result = false;
                    }
                } else {
                    result = true; // checksum has changed....add it
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            result = false;
        }
        return result;
    }

    public boolean newContract(Contract contract) {
        // null if not known
        Contract c = contracts.get(contract.getContract());
        if (c == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean commitContracts(ArrayList strokes) {
        // update the Hash field in the CACHE to show COMMITTED
        // TRANSIENT ---> COMMITTED
        try {
            String sql = "update MSMS_STROKE_CACHE set Hash = ?, Status = ? where [Contract] = ?";
            PreparedStatement stmt = theConnection.prepareStatement(sql);
            theConnection.setAutoCommit(false);
            Iterator iter = strokes.iterator();
            while (iter.hasNext()) {
                Stroke aStroke = (Stroke) iter.next();
                stmt.setString(1, References.COMMITTED_CONTRACT);
                stmt.setString(2, aStroke.getStatus());
                //stmt.setString(3, References.TRANSIENT_CONTRACT);
                stmt.setString(3, aStroke.getContractNumber());
                stmt.executeUpdate();
            }
            stmt = theConnection.prepareStatement("update MSMS_STROKE_CACHE set Hash='COMMITTED' where Hash='TRANSIENT'");
            stmt.executeUpdate();
            theConnection.commit();
            LOG.info("COMMITTED contracts");
            //commitNoRaiseContracts(); // clear up anything at NORAISE
            return true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean commitNoRaiseContracts() {
        // update the Hash field in the CACHE to show COMMITTED
        // TRANSIENT ---> COMMITTED
        try {
            String sql = "update MSMS_STROKE_CACHE set Hash = ? where Hash=? and Status = ?";
            PreparedStatement stmt = theConnection.prepareStatement(sql);
            theConnection.setAutoCommit(false);

            stmt.setString(1, References.COMMITTED_CONTRACT);
            stmt.setString(2, References.TRANSIENT_CONTRACT);
            stmt.setString(3, "NORAISE");
            stmt.executeUpdate();

            theConnection.commit();
            LOG.info("COMMITTED contracts");
            return true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
