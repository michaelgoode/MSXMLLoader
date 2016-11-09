/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import Utilities.MD5;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import labels.CalloutLabel;
import labels.FibreContentLabel;
import labels.FireWarningLabel;
import labels.Label;
import labels.LabelSet;
import labels.MadeInLabel;
import labels.StrokeLabel;
import labels.ZCodeLabel;
import managers.CSVDataManager;
import managers.CalloutLists;
import managers.InternationalManager;
import managers.SetManager;
import managers.SizeCodeManager;

/**
 *
 * @author michaelgoode
 */
public class Stroke extends Entity implements Serializable {

    private static final Logger LOG = Logger.getLogger(Stroke.class.getName());

    //private String dateModified;
    private String strokeNumber;
    private String strokeDescription;
    private String contractNumber;
    private String contractStatus;
    private String departmentNumber;
    private String season;
    private String supplierSeries;
    private String countryCode;
    private String productDesc;
    private String dateModified;
    private String status;
    public boolean isHosiery = false;

    private ArrayList<String> rawItems = new ArrayList<String>();

    public ArrayList<Colour> colourList = new ArrayList();
    ArrayList<SetDetails> washcareItems = new ArrayList();

    public ArrayList<String> validCallouts = new ArrayList();
    public ArrayList<String> rfidCalloutList = new ArrayList();
    public ArrayList<String> upcMatchList = new ArrayList();

    //StringBuilder rawItems = new StringBuilder();
    public void addRawItem(String rawData) {
        if (!rawData.trim().equals("")) {
            rawItems.add(rawData);
        }
    }

    private String getRawString() {
        Collections.sort(rawItems);
        return rawItems.toString();
    }

    public String getChecksum2() {
        MD5 md5 = MD5.getInstance();
        return md5.hash(this.getRawString());
    }

    public void buildSizeCode(LabelSet calloutLabels, CalloutLists calloutLists, SizeCodeManager sizeManager) {
        CalloutLabel aCalloutLabel = null;
        Iterator calloutIter = calloutLabels.iterator();
        while (calloutIter.hasNext()) {
            aCalloutLabel = (CalloutLabel) calloutIter.next();
            if (!calloutLists.internationalProducts.contains(aCalloutLabel.getLabelRef())) {
                if ((aCalloutLabel.getLabelType().equals("K")) && (this.getSizeCode().equals("")) && (aCalloutLabel.getLabelRef().contains("/"))) {
                    String sizeCode = aCalloutLabel.getLabelRef();
                    sizeCode = sizeCode.substring(sizeCode.indexOf("/") + 1, sizeCode.length());
                    this.setSizeCode(sizeCode);
                }
            }
        }
        if (!sizeManager.sizeCodeExists(sizeCode)) {
            this.setSizeCode(this.departmentNumber);
        }
        if (this.getSizeCode().equals("")) {
            this.setSizeCode(this.departmentNumber);
        }
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public ArrayList<SetDetails> getWashcareItems() {
        return washcareItems;
    }

    /*
     public void getCallouts() {
        
     if (callouts.size() == 0) {
     getStrokeLabelLevel();
     } else if (callouts.size() == 0) {
     getColourLabelLevel();
     } else if (callouts.size() == 0) {
     // must be at upcLabel
     }
     // build the data to represent the stroke label
     //getStrokeLabelLevel();
     //getColourLabelLevel();
     }
     */
    /*
     public ItemSetDetails getItem(String callout, String setname) {
     ItemSetDetails item = null;
     for (ItemSetDetails listItem : washcareItems) {
     if ((listItem.getCallout().equals(callout)) && (listItem.getSetNames().contains(setname))) {
     item = listItem;
     }
     }
     return item;
     }*/
    public void showZCodes(StringBuilder sb) {
        for (int i = 0; i < washcareItems.size(); i++) {
            washcareItems.get(i).getZCodesText(sb);
        }
    }

    public void getZCodes(StringBuilder sb) {
        for (int i = 0; i < washcareItems.size(); i++) {
            washcareItems.get(i).getZCodesText(sb);
        }
    }

    public void showCares(StringBuilder sb) {
        for (int i = 0; i < washcareItems.size(); i++) {
            washcareItems.get(i).getCaresText(sb);
        }
    }

    public void showWashCareFibres(StringBuilder sb) {
        for (int i = 0; i < washcareItems.size(); i++) {
            washcareItems.get(i).getCaresText(sb);
        }
    }

    public void getLabel(ArrayList<Label> labels, ArrayList labelList, String category, String type) {
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            StrokeLabel strokeLabel = (StrokeLabel) iter.next();
            if ((strokeLabel.getLabelCategory().equals(category)) && (strokeLabel.getLabelType().equals(type))) {
                /*
                 Label alabel = new Label();
                 alabel.setLabelRef(strokeLabel.getLabelRef());
                 alabel.setLabelCategory(strokeLabel.getLabelCategory());
                 alabel.setLabelType(strokeLabel.getLabelType());
                 alabel.setSetName(strokeLabel.getSetName());
                 alabel.setLabelOrder(strokeLabel.getLabelOrder());
                 labels.add(alabel);
                 */
            }
        }
    }

    public void getColourLabelLevel() {

        Iterator iter = colourList.iterator();
        while (iter.hasNext()) {
            Colour colour = (Colour) iter.next();
            colour.getCallouts(callouts);
        }

    }

    public String getStrokeNumber() {
        return strokeNumber;
    }

    public void setStrokeNumber(String strokeNumber) {
        this.strokeNumber = strokeNumber;
    }

    public String getStrokeDescription() {
        return strokeDescription;
    }

    public void setStrokeDescription(String strokeDescription) {
        this.strokeDescription = strokeDescription;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSupplierSeries() {
        return supplierSeries;
    }

    public void setSupplierSeries(String supplierSeries) {
        this.supplierSeries = supplierSeries;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    @Override
    public String toString() {
        //return "Stroke{"  + strokeNumber + "," + strokeDescription + "," + contractNumber + "," + contractStatus + "," + departmentNumber + "," + season + "," + supplierSeries + "," + countryCode + "," + productDesc + ",}";

        return strokeNumber + _DELIM + strokeDescription + _DELIM + contractNumber + _DELIM + contractStatus + _DELIM + departmentNumber + _DELIM + season + _DELIM + supplierSeries + _DELIM + countryCode + _DELIM + productDesc + _DELIM + sizeCode + _DELIM + this.getBoxQuantity() + _DELIM;

    }

    public void getCallouts(ArrayList callouts) {

        // processCareLabels();
        processKData();

    }

    public void processKData() {

        ArrayList<CareLabelItem> careLabels = new ArrayList<CareLabelItem>();
        CareLabelItem careLabel = new CareLabelItem();
        LabelDetails details = new LabelDetails();
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            StrokeLabel strokeLabel = (StrokeLabel) iter.next();
            if ((strokeLabel.getLabelCategory().equals("Care Label (Format\\Size)")) && (strokeLabel.getLabelType().equals("K"))) {
                Callout callout = new Callout();
                callout.setProductCallout(strokeLabel.getLabelRef());
                callout.setSetName(strokeLabel.getSetName());
                careLabel = new CareLabelItem();
                careLabel.addCallout(callout);
                careLabels.add(careLabel);
            } else if ((strokeLabel.getLabelCategory().equals("Fibres & Compositions")) && (strokeLabel.getLabelType().equals("K"))) {
                FibreContentLabel fibre = new FibreContentLabel();
                fibre.setLabelRef(strokeLabel.getLabelRef());
                fibre.setLabelCategory(strokeLabel.getLabelCategory());
                fibre.setLabelType(strokeLabel.getLabelType());
                fibre.setLabelOrder(strokeLabel.getLabelOrder());
                fibre.setSetName(strokeLabel.getSetName());
                details.addFibre(fibre);
            } else if ((strokeLabel.getLabelCategory().equals("Fire Warning \\ Caution")) && (strokeLabel.getLabelType().equals("K"))) {
                FireWarningLabel fire = new FireWarningLabel();
                fire.setLabelRef(strokeLabel.getLabelRef());
                fire.setLabelCategory(strokeLabel.getLabelCategory());
                fire.setLabelType(strokeLabel.getLabelType());
                fire.setLabelOrder(strokeLabel.getLabelOrder());
                fire.setSetName(strokeLabel.getSetName());
                details.addFireWarning(fire);
            } else if ((strokeLabel.getLabelCategory().equals("Other Words\\Phrases (Z Codes)")) && (strokeLabel.getLabelType().equals("K"))) {
                ZCodeLabel zcode = new ZCodeLabel();
                zcode.setLabelRef(strokeLabel.getLabelRef());
                zcode.setLabelCategory(strokeLabel.getLabelCategory());
                zcode.setLabelType(strokeLabel.getLabelType());
                zcode.setLabelOrder(strokeLabel.getLabelOrder());
                zcode.setSetName(strokeLabel.getSetName());
                details.addZCode(zcode);
            } else if (((strokeLabel.getLabelCategory().equals("Care Instructions (CL Codes)")) || (strokeLabel.getLabelCategory().equals("Additional Care Instr. (Text)"))) && (strokeLabel.getLabelType().equals("K"))) {
                CalloutLabel care = new CalloutLabel();
                care.setLabelRef(strokeLabel.getLabelRef());
                care.setLabelCategory(strokeLabel.getLabelCategory());
                care.setLabelType(strokeLabel.getLabelType());
                care.setLabelOrder(strokeLabel.getLabelOrder());
                care.setSetName(strokeLabel.getSetName());
                details.addCareInstruction(care);
            } else if ((strokeLabel.getLabelCategory().equals("Made in... (Country Codes)")) && (strokeLabel.getLabelType().equals("K"))) {
                MadeInLabel madeIn = new MadeInLabel();
                madeIn.setLabelRef(strokeLabel.getLabelRef());
                madeIn.setLabelCategory(strokeLabel.getLabelCategory());
                madeIn.setLabelType(strokeLabel.getLabelType());
                madeIn.setLabelOrder(strokeLabel.getLabelOrder());
                madeIn.setSetName(strokeLabel.getSetName());
                details.addMadeInList(madeIn);
            }

        }

        Iterator careLabelsIter = careLabels.iterator();
        while (careLabelsIter.hasNext()) {
            CareLabelItem aCareLabel = (CareLabelItem) careLabelsIter.next();
            aCareLabel.setDetails(details);
        }

        int x = 0;

    }

    private void processCareLabels() {

        // grab anything of type k
        HashSet<FibreContentLabel> fibreList = new HashSet<FibreContentLabel>();
        HashSet<FireWarningLabel> fireList = new HashSet<FireWarningLabel>();
        HashSet<ZCodeLabel> zCodeList = new HashSet<ZCodeLabel>();
        HashSet<CalloutLabel> careList = new HashSet<CalloutLabel>();
        HashSet<MadeInLabel> madeInList = new HashSet<MadeInLabel>();

        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {

            StrokeLabel strokeLabel = (StrokeLabel) iter.next();

            if ((strokeLabel.getLabelCategory().equals("Care Label (Format\\Size)")) && (strokeLabel.getLabelType().equals("K"))) {
                Callout callout = new Callout();
                callout.setProductCallout(strokeLabel.getLabelRef());
                callout.setSetName(strokeLabel.getSetName());
                callouts.add(callout);
            } else if ((strokeLabel.getLabelCategory().equals("Fibres & Compositions")) && (strokeLabel.getLabelType().equals("K"))) {
                FibreContentLabel fibre = new FibreContentLabel();
                fibre.setLabelRef(strokeLabel.getLabelRef());
                fibre.setLabelCategory(strokeLabel.getLabelCategory());
                fibre.setLabelType(strokeLabel.getLabelType());
                fibre.setLabelOrder(strokeLabel.getLabelOrder());
                fibre.setSetName(strokeLabel.getSetName());
                fibreList.add(fibre);
            } else if ((strokeLabel.getLabelCategory().equals("Fire Warning \\ Caution")) && (strokeLabel.getLabelType().equals("K"))) {
                FireWarningLabel fire = new FireWarningLabel();
                fire.setLabelRef(strokeLabel.getLabelRef());
                fire.setLabelCategory(strokeLabel.getLabelCategory());
                fire.setLabelType(strokeLabel.getLabelType());
                fire.setLabelOrder(strokeLabel.getLabelOrder());
                fire.setSetName(strokeLabel.getSetName());
                fireList.add(fire);
            } else if ((strokeLabel.getLabelCategory().equals("Other Words\\Phrases (Z Codes)")) && (strokeLabel.getLabelType().equals("K"))) {
                ZCodeLabel zcode = new ZCodeLabel();
                zcode.setLabelRef(strokeLabel.getLabelRef());
                zcode.setLabelCategory(strokeLabel.getLabelCategory());
                zcode.setLabelType(strokeLabel.getLabelType());
                zcode.setLabelOrder(strokeLabel.getLabelOrder());
                zcode.setSetName(strokeLabel.getSetName());
                zCodeList.add(zcode);
            } else if (((strokeLabel.getLabelCategory().equals("Care Instructions (CL Codes)")) || (strokeLabel.getLabelCategory().equals("Additional Care Instr. (Text)"))) && (strokeLabel.getLabelType().equals("K"))) {
                CalloutLabel care = new CalloutLabel();
                care.setLabelRef(strokeLabel.getLabelRef());
                care.setLabelCategory(strokeLabel.getLabelCategory());
                care.setLabelType(strokeLabel.getLabelType());
                care.setLabelOrder(strokeLabel.getLabelOrder());
                care.setSetName(strokeLabel.getSetName());
                careList.add(care);
            } else if ((strokeLabel.getLabelCategory().equals("Made in... (Country Codes)")) && (strokeLabel.getLabelType().equals("K"))) {
                MadeInLabel madeIn = new MadeInLabel();
                madeIn.setLabelRef(strokeLabel.getLabelRef());
                madeIn.setLabelCategory(strokeLabel.getLabelCategory());
                madeIn.setLabelType(strokeLabel.getLabelType());
                madeIn.setLabelOrder(strokeLabel.getLabelOrder());
                madeIn.setSetName(strokeLabel.getSetName());
                madeInList.add(madeIn);
            }

        }

        Iterator calloutIter = callouts.iterator();
        while (calloutIter.hasNext()) {
            Callout callout = (Callout) calloutIter.next();
            callout.addFibres(fibreList);
            callout.addCareInstructions(careList);
            callout.addMadeIn(madeInList);
            callout.addZCodes(zCodeList);
        }
    }

    public void writeCSV(BufferedWriter out) {
        try {

            for (int i = 0; i < colourList.size(); i++) {
            }

            for (int i = 0; i < washcareItems.size(); i++) {

                out.write(washcareItems.get(i).toString());
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }

    public void manageInternational(CalloutLists calloutLists, ArrayList<CalloutLabel> labelCallouts) {
        InternationalManager internationalManager = new InternationalManager();
        internationalManager.expandInternationalCallouts(calloutLists, labelCallouts);
    }

    public String getCheckSum() {

        String s = "";

        SetManager setManager = new SetManager();

        this.buildLabelLevelDetails(setManager, this.isHosiery, "stroke", "");

        s = s + this.toString();

        for (Colour colour : colourList) {
            colour.buildLabelLevelDetails(setManager, this.isHosiery, "colour", "");
            s = s + colour.toString();

            for (Size size : colour.sizeList) {
                size.buildLabelLevelDetails(setManager, this.isHosiery, "size", "");
                s = s + size.toString();

                for (UPC upc : size.upcList) {
                    upc.buildLabelLevelDetails(setManager, this.isHosiery, "upc", "");
                    s = s + upc.toString();

                }

            }

        }

        s = s + setManager.getString();

        return MD5.getInstance().hash(s);

    }

    public boolean validate(CSVDataManager csvdatamanager) {
        return true;
    }

    public ArrayList<String> getRFIDCallouts() {
        ArrayList<String> list = new ArrayList<String>();
        Iterator colorIter = this.colourList.iterator();
        while (colorIter.hasNext()) {
            Colour colour = (Colour) colorIter.next();

            Iterator calloutIter = colour.labelCallouts.iterator();
            while (calloutIter.hasNext()) {
                CalloutLabel cl = (CalloutLabel) calloutIter.next();
                if ((cl.getLabelCategory().equalsIgnoreCase("UPC / Barcode & Price Labels & Tickets")) && (cl.getLabelRef().endsWith("RF"))) {
                    if (!list.contains(cl.getLabelRef())) {
                        list.add(cl.getLabelRef());
                    }
                }
            }

            Iterator sizeIter = colour.sizeList.iterator();
            while (sizeIter.hasNext()) {
                Size size = (Size) sizeIter.next();
                Iterator sizeCalloutIter = size.labelCallouts.iterator();
                while (sizeCalloutIter.hasNext()) {
                    CalloutLabel cl = (CalloutLabel) sizeCalloutIter.next();
                    if ((cl.getLabelCategory().equalsIgnoreCase("UPC / Barcode & Price Labels & Tickets")) && (cl.getLabelRef().endsWith("RF"))) {
                        if (!list.contains(cl.getLabelRef())) {
                            list.add(cl.getLabelRef());
                        }
                    }
                }
            }
        }

        Iterator iter = this.labelCallouts.iterator();
        while (iter.hasNext()) {
            CalloutLabel cl = (CalloutLabel) iter.next();
            if ((cl.getLabelCategory().equalsIgnoreCase("UPC / Barcode & Price Labels & Tickets")) && (cl.getLabelRef().endsWith("RF"))) {
                if (!list.contains(cl.getLabelRef())) {
                    list.add(cl.getLabelRef());
                }

            }
        }
        return list;
    }

    public ArrayList<String> getUPCList() {

        ArrayList<String> list = new ArrayList<>();

        Iterator<Colour> colourIter = colourList.iterator();
        while (colourIter.hasNext()) {
            Colour aColour = colourIter.next();
            Iterator sizeIter = aColour.sizeList.iterator();
            while (sizeIter.hasNext()) {
                Size aSize = (Size) sizeIter.next();
                Iterator upcIter = aSize.upcList.iterator();
                while (upcIter.hasNext()) {
                    UPC aUPC = (UPC) upcIter.next();
                    list.add(aUPC.getUpcNumber());
                }
            }
        }
        return list;
    }

    public ArrayList<String> getRfidCalloutList() {
        return rfidCalloutList;
    }

    public void setRfidCalloutList(ArrayList<String> rfidCallouts) {
        this.rfidCalloutList = rfidCallouts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    

}
