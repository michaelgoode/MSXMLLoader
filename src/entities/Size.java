/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.UPC;
import labels.SizeLabel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import labels.SizeLabel;
import managers.LabelListManager;

/**
 *
 * @author michaelgoode
 */
public class Size extends Entity {

    

    private Colour colour;
    private String primarySize;
    
    //public ArrayList<SizeLabel> sizeLabelList = new ArrayList();
    public ArrayList<UPC> upcList = new ArrayList();
    public ArrayList<UPC> internationalUPCList = new ArrayList();
    ArrayList<SetDetails> washcareItems = new ArrayList();

    public Size(Colour colour) {
        this.colour = colour;
    }

    public void getCallouts(ArrayList<Callout> callouts) {

        Iterator iter = this.getLabelList().iterator();
        while (iter.hasNext()) {

            SizeLabel sizeLabel = (SizeLabel) iter.next();
            if ((sizeLabel.getLabelCategory().equals("Care Label (Format\\Size)")) && (sizeLabel.getLabelType().equals("K"))) {
                Callout callout = new Callout();
                callout.setProductCallout(sizeLabel.getLabelRef());
                callout.setSetName(sizeLabel.getSetName());
                callouts.add(callout);
            }

        }

    }

    public String getPrimarySize() {
        return primarySize;
    }

    public void setPrimarySize(String primarySize) {
        this.primarySize = primarySize;
    }

    @Override
    public String toString() {
        //return "Size{" + primarySize + '}';
        return primarySize + _DELIM + this.getBoxQuantity() + _DELIM;
    }


}
