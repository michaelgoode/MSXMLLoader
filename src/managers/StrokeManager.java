/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Stroke;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michaelgoode
 */
public class StrokeManager {
    
    private static final Logger LOG = Logger.getLogger(StrokeManager.class.getName());
    
    ContractCache contractCache = ContractCache.getInstance();
    StrokeFilter filter;
    Connection connection;
    
    public StrokeManager() {
        try {
            DBManager db = DBManager.getInstance();
            connection = db.getConnection(ConnectionType.LOOKUP);
            connection.setAutoCommit(false);
            //contractCache = new ContractCache();
            filter = new StrokeFilter(connection);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex.getMessage());
        }
    }
    
    public boolean IsTheContractValid(Stroke stroke) {
        if (stroke.getContractNumber().equals("1388533")) {
            int x = 0;
        }
        
        Contract contract = new Contract();
        contract.setContract(stroke.getContractNumber());
        contract.setDateModified(stroke.getDateModified());
        contract.setStroke(stroke);
        contract.setChecksum2(stroke.getChecksum2());
        boolean result = false;
        if (filter.supplierExist(stroke.getSupplierSeries())) { // the supplier is in the required range 

            if (!contractCache.newContract(contract)) { // old contract
                if (!contractCache.modifiedContract(stroke.getDepartmentNumber(),contract)) { // not changed so remove
                    result = false;
                } else {
                    result = true;
                }
            } else {
                if (contractCache.addContract(contract)) {
                    result = true;
                }
            }
        } else {
            result = false;
        }
        
        
        
        //System.out.println("THIS CODE IS RUNNING A DEBUG CHANGE THAT MUST BE REMOVED BEFORE IT GOES LIVE");
        //result = true; // remove this for the live code
        
        
        
        
        return result;
    }
    
    public boolean commitStrokes( ArrayList strokes ) {
        /*
        try {
            connection.commit();
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, null, e.getMessage());
            }
        }*/
        return this.contractCache.commitContracts(strokes);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
