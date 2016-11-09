/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import Utilities.References;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import labels.CalloutLabel;
import labels.Label;
import labels.LabelSet;
import managers.LabelListManager;
import managers.SetManager;
import managers.CalloutLists;
import managers.OutputManager;
import managers.PrintLine;

/**
 *
 * @author michaelgoode
 */
public class Entity implements Serializable {

    protected final static String _DELIM = "|";
    protected ArrayList<Label> labelList = new ArrayList();
    protected ArrayList<Callout> callouts = new ArrayList();
    protected ArrayList<CalloutLabel> labelCallouts = new ArrayList(); // keep in order
    protected String sizeCode = "";
    private String multiPartSet = "";
    private String boxQuantity = "0";

    private boolean printIt;

    public ArrayList<Label> getLabelList() {
        return labelList;
    }

    public void listCallouts() {
        if (!labelList.isEmpty()) {
            LabelListManager llp = new LabelListManager();
        }
    }

    public void addEntityCallout(CalloutLabel calloutLabel) {
        labelCallouts.add(calloutLabel);
    }

    public void buildLabelLevelDetails(SetManager setmanager, boolean isHosiery, String level, String colourDesc) {
        if (!labelList.isEmpty()) {
            LabelListManager llp = new LabelListManager();
            llp.buildSets(labelList, setmanager, isHosiery, level);
        }
    }

    public void buildCalloutLabels(LabelSet calloutLabels, SetManager setmanager, String dept) {
        if (!labelList.isEmpty()) {
            LabelListManager llp = new LabelListManager();
            llp.buildCalloutList(this, calloutLabels, labelList, dept);
        }
    }

    public ArrayList<Callout> getCallouts() {
        return callouts;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public ArrayList<CalloutLabel> getEntityCallouts() {
        return labelCallouts;
    }

    public String getBoxQuantity() {
        return boxQuantity;
    }

    public void setBoxQuantity(String boxQty) {
        this.boxQuantity = boxQty;
    }

    public void printData(Stroke stroke, Colour colour, Size size, Size internationalSize, UPC upc, SetManager setManager, CalloutLists callouts, OutputManager outputManager) {

        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        if (this.getEntityCallouts().size() > 0) { // we have callouts at the entity level...
            Collections.sort(this.getEntityCallouts());
            SetDetails details = null;
            Iterator calloutIter = this.getEntityCallouts().iterator();
            while (calloutIter.hasNext()) {
                CalloutLabel aCalloutLabel = (CalloutLabel) calloutIter.next();
                if (callouts.isValidCallout(aCalloutLabel.getLabelRef().toUpperCase().trim())) {

                    if (aCalloutLabel.getSetName().equals("")) {
                        aCalloutLabel.setSetName(setManager.GetNameTopName());
                    }
                    if (!aCalloutLabel.getSetName().equals("")) {
                        if (!setManager.containsName(aCalloutLabel.getSetName())) {
                            details = setManager.get(References.VoidSetName); // assign dummy set
                        } else {
                            details = setManager.get(aCalloutLabel.getSetName());
                        }
                    }
                    //sb.append(aCalloutLabel.getLabelRef() + "|");
                    sb.append(stroke.toString());
                    sb.append(colour.toString());
                    if (!callouts.internationalProducts.contains(aCalloutLabel.getLabelRef())) {
                        sb.append(size.toString());
                        sb.append(upc.toString());
                    } else {
                        InternationalUPC internationalUPC = new InternationalUPC(internationalSize, upc.getSecondarySize());
                        sb.append(internationalSize.toString());
                        sb.append(internationalUPC.toString());
                    }

                    if (details != null) {
                        sb.append(details.toString());
                    }
                    sb.append("\n");
                    outputManager.addLine(new PrintLine(aCalloutLabel.getLabelRef(), sb.toString()));
                    sb.setLength(0);
                }
            }
        }
    }

    public void printData(Stroke stroke, Colour colour, Size size, Size internationalSize, UPC upc, SetManager setManager, CalloutLists callouts, OutputManager outputManager, LabelSet calloutLabels) {

        // get the set name of the type K item
        boolean found = false;
        String RFIDSetName = "";
        CalloutLabel callout = null;
        Iterator iter = calloutLabels.iterator();
        while ((iter.hasNext()) && (!found)) {
            callout = (CalloutLabel) iter.next();
            if ((callout.getLabelType().equalsIgnoreCase("K"))){
                found = true;
                RFIDSetName = callout.getSetName();
            }
        }
        
        CalloutLabel c = null;
        found = false;
        Iterator siter = labelCallouts.iterator();
        while ((siter.hasNext())) {
            c = (CalloutLabel) siter.next();
            if ((c.getLabelType().equalsIgnoreCase("C")) || (c.getLabelType().equalsIgnoreCase("T"))){
                c.setSetName("");
            }
        }
        
        boolean isHosiery = false;
        if (setManager.containsName("HOSIERY")) {
            isHosiery = true;
        }
        
        
        
        
        // check the calloutLabels against this.getEntityCallouts
        // use callouts to determin the correct set name
        CalloutLabel rfidcallout = null;
        found = false;
        Iterator rfiditer = labelCallouts.iterator();
        while ((rfiditer.hasNext())) {
            rfidcallout = (CalloutLabel) rfiditer.next();
            if (rfidcallout.getSetName().isEmpty()) {
                found = true;
                rfidcallout.setSetName(RFIDSetName);
            } 
        }

        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        if (this.getEntityCallouts().size() > 0) { // we have callouts at the entity level...
            if (!this.getEntityCallouts().contains(rfidcallout)) {
                stroke.labelCallouts.remove(rfidcallout);
                this.getEntityCallouts().add(rfidcallout);
            }
            SetDetails details = null;
            Iterator calloutIter = this.getEntityCallouts().iterator();
            while (calloutIter.hasNext()) {
                CalloutLabel aCalloutLabel = (CalloutLabel) calloutIter.next();
                if (callouts.isValidCallout(aCalloutLabel.getLabelRef().toUpperCase().trim())) {

                    if (aCalloutLabel.getLabelRef().endsWith("RF")) {
                        if (aCalloutLabel.getSetName().equals("")) {
                            aCalloutLabel.setSetName(setManager.GetNameTopName());
                        }
                    } else {
                        if (aCalloutLabel.getSetName().equals("")) {
                            aCalloutLabel.setSetName(setManager.GetNameTopName());
                        }
                    }
                    
                    if (isHosiery) {
                        aCalloutLabel.setSetName("HOSIERY");
                    }
                    if (!aCalloutLabel.getSetName().equals("")) {
                        if (!setManager.containsName(aCalloutLabel.getSetName())) {
                            details = setManager.get(References.VoidSetName); // assign dummy set
                        } else {
                            details = setManager.get(aCalloutLabel.getSetName());
                        }
                    }
                    
                    
                    
                    sb.append(stroke.toString());
                    sb.append(colour.toString());
                    if (!callouts.internationalProducts.contains(aCalloutLabel.getLabelRef())) {
                        sb.append(size.toString());
                        sb.append(upc.toString());
                    } else {
                        InternationalUPC internationalUPC = new InternationalUPC(internationalSize, upc.getSecondarySize());
                        sb.append(internationalSize.toString());
                        sb.append(internationalUPC.toString());
                    }

                    if (details != null) {
                        sb.append(details.toString());
                    }
                    
                    sb.append("\n");
                    if (!stroke.upcMatchList.contains(aCalloutLabel.getLabelRef() + upc.getUpcNumber())) {
                        outputManager.addLine(new PrintLine(aCalloutLabel.getLabelRef(), sb.toString()));
                        stroke.upcMatchList.add(aCalloutLabel.getLabelRef() + upc.getUpcNumber());
                    }
                    sb.setLength(0);
                }
            }
        }
    }

    public boolean isPrintIt() {
        return printIt;
    }

    public void setPrintIt(boolean printIt) {
        this.printIt = printIt;
    }

}
