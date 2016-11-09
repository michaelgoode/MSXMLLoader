/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Stroke;

/**
 *
 * @author michaelgoode
 */
public class Contract {
    private String contract = "";
    private String dateModified = "";
    private String checksum = "";
    private String checksum2 = "";
    private int count;
    private Stroke stroke;
    private String status;
    private String raiseStatus;
    private String dept;
    private boolean checksumChanged;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public boolean isChecksumChanged() {
        return checksumChanged;
    }

    public void setChecksumChanged(boolean checksumChanged) {
        this.checksumChanged = checksumChanged;
    }
    
    

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }
    

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String hash) {
        this.checksum = hash;
    }

    public String getChecksum2() {
        return checksum2;
    }

    public void setChecksum2(String checksum2) {
        this.checksum2 = checksum2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRaiseStatus() {
        return raiseStatus;
    }

    public void setRaiseStatus(String raiseStatus) {
        this.raiseStatus = raiseStatus;
    }
}
