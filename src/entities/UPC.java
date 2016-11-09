/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import labels.UPCLabel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import labels.UPCLabel;
import managers.LabelListManager;

/**
 *
 * @author michaelgoode
 */
public class UPC extends Entity {
    
    private String upcNumber;
    private String secondarySize;
    private String sellingPrice;
    
    
    ArrayList<SetDetails> washcareItems = new ArrayList();
    
    private Size size;
    
    public UPC( Size size ) {
        this.size = size;
    }


    
    
    
   
    public ArrayList<String> getProductCallouts() {
        ArrayList<String> callouts = new ArrayList();
        Iterator iter = this.getLabelList().iterator();
        while (iter.hasNext()) {
            UPCLabel upcLabel = (UPCLabel) iter.next();
            if ((upcLabel.getLabelCategory().equals("Care Label (Format\\Size)")) && (upcLabel.getLabelType().equals("K"))){
                callouts.add(upcLabel.getLabelRef());   
            }
        }
        return callouts;
    }

    

    
    
    public String getUpcNumber() {
        return upcNumber;
    }

    public void setUpcNumber(String upcNumber) {
        this.upcNumber = upcNumber;
    }

    public String getSecondarySize() {
        return secondarySize;
    }

    public void setSecondarySize(String secondarySize) {
        this.secondarySize = secondarySize;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @Override
    public String toString() {
        return upcNumber + _DELIM + secondarySize + _DELIM + sellingPrice + _DELIM + this.getBoxQuantity() + _DELIM;
    }
    
   
    
}
