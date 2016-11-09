/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import Utilities.References;
import entities.Entity;
import labels.Label;
import labels.CalloutLabel;
import labels.FibreContentLabel;
import labels.FireWarningLabel;
import entities.SetDetails;
import labels.ZCodeLabel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import labels.LabelSet;

/**
 *
 * @author michaelgoode
 */
public class LabelListManager {

    public class LabelOrderComparator implements Comparator<Label> {

        @Override
        public int compare(Label l1, Label l2) {
            int result = l1.getSetName().compareTo(l2.getSetName());
            if (result == 0) {
                result = (int) (l1.getLabelOrder() - l2.getLabelOrder());
            }
            return result;
        }
    }

    public class FibreComparator implements Comparator<FibreContentLabel> {
        // order by label order first, then by percentage

        @Override
        public int compare(FibreContentLabel f1, FibreContentLabel f2) {
            int i = (int) (f1.getLabelOrder() - f2.getLabelOrder());
            if (i == 0) {
                i = (int) (f2.getPercent() - f1.getPercent());
            }
            return i;
        }
    }
    static final String MADEIN_TAG = "Made in... (Country Codes)";
    static final String CARELABEL_TAG = "Care Label (Format\\Size)";
    static final String FIBRE_TAG = "Fibres & Compositions";
    static final String CARE_TAG = "Care Instructions (CL Codes)";
    static final String ADD_CARE_TAG = "Additional Care Instr. (Text)";
    static final String FIRE_TAG = "Fire Warning \\ Caution";
    static final String ZCODE_TAG = "Other Words\\Phrases (Z Codes)";
    static final String UPC_TAG = "UPC / Barcode & Price Labels & Tickets";
    static final String PIPS_TAG = "Size Pips, Hanger & Hook Size Labels";
    static final String MULTIPART_SET = "Multi part sets";
    static final String HANGING_TAG = "Hanging";
    static final String WOVEN_TAG = "Woven labels (WL)";
    static final String BOX_TAG = "Modules (Boxed)";
    static final String WASHCARE_LABEL_TAG = "K";
    static final String CORPORATE_TAG = "C";
    static final String TRANSIT_TAG = "T";

    private void sortByLabelOrder(ArrayList<Label> list) {
        Collections.sort(list, new LabelOrderComparator());
    }

    private void sortOrphanedZCodes(ArrayList<Label> list) {
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) { // ensure its not the final item in the list
                Label l = (Label) list.get(i);

                if ((l.getLabelRef().startsWith("Z23")) && (l.getLabelCategory().equals(ADD_CARE_TAG))) {
                    l.setLabelCategory(ZCODE_TAG);
                    l.setLabelOrder(l.getLabelOrder() + 100);
                }

                if ((l.getLabelCategory().equals(ZCODE_TAG)) && (!list.get(i + 1).getLabelCategory().equals(FIBRE_TAG)
                        && (l.getLabelOrder() < 100) && (l.getSetName().equals(list.get(i + 1).getSetName())))) {
                    l.setLabelOrder(0); // set the zcode order ref to a zero to promote it to the top of the list
                }
            }
        }
    }

    private void sortSuffixZCodes(ArrayList<Label> list) {
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) { // ensure its not the final item in the list
                if ((list.get(i).getLabelCategory().equals(ZCODE_TAG)) && (!list.get(i + 1).getLabelCategory().equals(FIBRE_TAG)) && ((list.get(i).getLabelRef().endsWith("B")) || (list.get(i).getLabelRef().endsWith("F")))) {
                    int pos = list.get(i).getLabelOrder();
                    list.get(i).setLabelOrder(pos + 100); // set the zcode order ref to pos + 100 demote it to the bottom of the list
                }
            }
        }
    }

    // sort labels by label order
    public void sort(ArrayList list) {
        // sort into label order ref
        sortByLabelOrder(list);
        // look for orphaned z codes
        // any orphaned z codes then set the label order ref to 0 so that it rises to the top
        sortOrphanedZCodes(list);

        sortSuffixZCodes(list);
        // re-sort to push any rearranged zcodes to the top
        sortByLabelOrder(list);
    }

    public void showList(ArrayList list) {

    }

    // getCares
    private void getCares(ArrayList labelList, SetDetails details) {
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            Label label = (Label) iter.next();
            String fullSetName = label.getSetName();
            if (fullSetName.equals(details.getSetName())) { // correct set name
                if ((label.getLabelCategory().equals(CARE_TAG)) || (label.getLabelCategory().equals(ADD_CARE_TAG)) && (label.getLabelType().equals(WASHCARE_LABEL_TAG))) {
                    if (label.getLabelRef().startsWith("Z23")) {
                        details.zcodes.add(new ZCodeLabel(label.getLabelRef(), ZCODE_TAG, label.getLabelType(), label.getLabelOrder() + 100, label.getSetName(), label.getBoxQty()));
                    } else {
                        CalloutLabel calloutLabel = new CalloutLabel(label.getLabelRef(), label.getLabelCategory(), label.getLabelType(), label.getLabelOrder(), fullSetName, label.getBoxQty());
                        // override the label order by the 'type' in 
                        if (label.getLabelCategory().equals(ADD_CARE_TAG)) {
                            CareTextManager ctm = CareTextManager.getInstance();
                            calloutLabel.setLabelOrder(ctm.getCareTextPosition(calloutLabel.getLabelRef()));
                            details.cares.add(calloutLabel);

                        } else {
                            details.cares.add(calloutLabel);
                        }
                        Collections.sort(details.cares, new LabelOrderComparator());
                    }
                }
            }
        }
        this.sort(labelList);
    }
    // firewarnings

    private void getFireWarnings(ArrayList labelList, SetDetails details) {
        // fire
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            Label label = (Label) iter.next();
            String fullSetName = label.getSetName();
            if (fullSetName.equals(details.getSetName())) {
                if ((label.getLabelCategory().equals(FIRE_TAG)) && (label.getLabelType().equals(WASHCARE_LABEL_TAG))) {
                    details.fires.add(new FireWarningLabel(label.getLabelRef(), label.getLabelCategory(), label.getLabelType(), label.getLabelOrder(), fullSetName, label.getBoxQty()));
                }
            }
        }
    }
    // made in

    private void getMadeIn(ArrayList labelList, SetDetails details) {
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            Label label = (Label) iter.next();
            String fullSetName = label.getSetName();
            if (fullSetName.equals(details.getSetName())) {
                if ((label.getLabelCategory().equals(MADEIN_TAG)) && (label.getLabelType().equals(WASHCARE_LABEL_TAG))) {
                    //details.madeIn.add(new MadeInLabel(label.getLabelRef(), label.getLabelCategory(), label.getLabelType(), label.getLabelOrder(), label.getSetName(), label.getBoxQty()));
                    details.setMadeIn(label.getLabelRef());
                }
            }
        }
    }

    private void getMultiPartSet(ArrayList labelList, SetDetails details) {
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            Label label = (Label) iter.next();
            String fullSetName = label.getSetName();
            if (fullSetName.equals(details.getSetName())) {
                if ((label.getLabelCategory().equals(MULTIPART_SET)) && (label.getLabelType().equals(WASHCARE_LABEL_TAG))) {
                    //details.multiPartSets.add(new MultiPartLabel(label.getLabelRef(), label.getLabelCategory(), label.getLabelType(), label.getLabelOrder(), label.getSetName(), label.getBoxQty()));
                    details.setMultiPartSet(label.getLabelRef());
                }
            }
        }
    }

    // get zcodes and fibres
    private void getZCodes(ArrayList labelList, SetDetails details) {
        // zcodes

        sort(labelList);
        boolean ZCodeFound = false;

        // create a blank zcode (BLANK) if the first item is not a zcode. The first item will then be blank so add reference 'BLANK' to the look up table 
        try {
            String fullSetName = "";
            for (int i = 0; i < labelList.size(); i++) {
                Label aLabel = (Label) labelList.get(i);
                if (!aLabel.getLabelRef().equals("BLANK")) {
                    fullSetName = aLabel.getSetName();
                    aLabel.setSetName(fullSetName);
                }
                if ((aLabel.getLabelCategory().equals(ZCODE_TAG)) && (fullSetName.equals(details.getSetName()))) {
                    ZCodeFound = true;
                }
                if ((aLabel.getLabelCategory().equals(FIBRE_TAG)) && (!ZCodeFound) && (fullSetName.equals(details.getSetName()))) {
                    labelList.add(new ZCodeLabel("BLANK", ZCODE_TAG, aLabel.getLabelType(), 0, fullSetName, aLabel.getBoxQty()));
                    ZCodeFound = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sort(labelList);
        try {
            ZCodeLabel currZCode = null;
            for (int i = 0; i < labelList.size(); i++) {
                Label aLabel = (Label) labelList.get(i);
                String fullSetName = aLabel.getSetName();
                if ((aLabel.getLabelCategory().equals(ZCODE_TAG)) && (fullSetName.equals(details.getSetName()))) {
                    currZCode = new ZCodeLabel((Label) labelList.get(i));
                    details.zcodes.add(currZCode);
                }
                if ((aLabel.getLabelCategory().equals(FIBRE_TAG)) && (fullSetName.equals(details.getSetName()))) {
                    if (currZCode == null) {
                        currZCode = new ZCodeLabel((Label) labelList.get(i));
                    }
                    currZCode.addFibre(new FibreContentLabel(aLabel.getLabelRef(), aLabel.getLabelCategory(), aLabel.getLabelType(), aLabel.getLabelOrder(), fullSetName, aLabel.getBoxQty()));
                    Collections.sort(currZCode.fibreLabels, new FibreComparator());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildSets(ArrayList<Label> list, SetManager setManager, boolean isHosiery, String level) {
        if (!list.isEmpty()) {
            this.sort(list);
            // build a list of all the setnames
            ArrayList<SetName> setNameList = new ArrayList<SetName>();

            for (Label aLabel : list) {
                SetName aSetName = null;

                aSetName = new SetName(aLabel.getSetName(), aLabel.getSetName());
                
                if ((setNameList.contains(aSetName) && (aLabel.getLabelCategory().equals(CARELABEL_TAG)))) {
                    //SetName sname = new SetName(aLabel.getSetName(), String.format("%s_%d", aLabel.getSetName(), setNameList.size()));
                    SetName sname = new SetName(aLabel.getSetName(), aLabel.getSetName());
                    setNameList.add(sname);
                    for (SetName sn : setNameList) {
                        if (sn.getSetName().equals(sname.getSetName())) {
                            sn.setDerivedSetName(sname.getDerivedSetName());
                        }
                    }
                } else if ((!setNameList.contains(aSetName)) && (!aLabel.getSetName().equals(""))) {
                    if (aLabel.getLabelCategory().equals(CARELABEL_TAG)) {                   
                            setNameList.add(new SetName(aLabel.getSetName(), aLabel.getSetName()));   
                    }
                }
            }
            // we have a list of all distinct set names
            // for each set name process the list and grab the relevant items
            Iterator setIter = setNameList.iterator();
            while (setIter.hasNext()) {
                SetName aSetName = (SetName) setIter.next();
                // construct an empty set object
                SetDetails aSetDetails = new SetDetails(aSetName.getDerivedSetName(), level);
                // get cares
                getCares(list, aSetDetails);
                // firewarnings
                getFireWarnings(list, aSetDetails);
                // made in
                getMadeIn(list, aSetDetails);
                // zcodes
                getZCodes(list, aSetDetails);
                // multipart set
                getMultiPartSet(list, aSetDetails);

                setManager.addSet(aSetDetails); // index by set name
            }

            setManager.addSet(new SetDetails(References.VoidSetName, "")); // add a dummy set name 
        }
    }

    public void buildCalloutList(Entity entity, LabelSet calloutLabels, ArrayList labelList, String dept) {
        //calloutLabels.clear(); // clear the list ??????
        
        // void any K items so that they are not included in the list or the RFID item is set incorrectly
        Iterator voiditer = calloutLabels.iterator();
        while (voiditer.hasNext()) {
            CalloutLabel label = (CalloutLabel) voiditer.next();
            if (label.getLabelType().equalsIgnoreCase("K")) {
                label.setLabelType("X");
                label.setLabelRef("void");
            }
        }
        
        
        
        
        getCallouts(entity, labelList, CARELABEL_TAG, WASHCARE_LABEL_TAG, calloutLabels, dept);
        getCallouts(entity, labelList, PIPS_TAG, CORPORATE_TAG, calloutLabels, dept);
        getCallouts(entity, labelList, UPC_TAG, CORPORATE_TAG, calloutLabels, dept);
        getCallouts(entity, labelList, WOVEN_TAG, CORPORATE_TAG, calloutLabels, dept);
        getCallouts(entity, labelList, HANGING_TAG, TRANSIT_TAG, calloutLabels, dept);
        getCallouts(entity, labelList, BOX_TAG, TRANSIT_TAG, calloutLabels, dept);

        String setname = "";

        Iterator iter = calloutLabels.iterator();
        while (iter.hasNext()) {
            CalloutLabel label = (CalloutLabel) iter.next();
            if (setname.equals("")) {
                setname = label.getSetName();
            }
        }

        iter = calloutLabels.iterator();
        while (iter.hasNext()) {
            CalloutLabel calloutLabel = (CalloutLabel) iter.next();
            if ((!setname.equals("")) && (calloutLabel.getSetName().equals(""))) {
                calloutLabel.setSetName(setname);
            }
        }
    }

    private void getCallouts(Entity entity, ArrayList labelList, String labelcat, String labeltype, HashSet<CalloutLabel> calloutLst, String dept) {
        RFIDDepartments depts = RFIDDepartments.getInstance();
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            Label label = (Label) iter.next();
            if ((!label.getLabelRef().startsWith("MRK"))) {
                if ((label.getLabelCategory().equals(labelcat)) && (label.getLabelType().equals(labeltype))) {

                    //if ((!label.getLabelRef().endsWith("RF")) || (label.getLabelRef().endsWith("RF") && depts.contains(dept))) {
                    //if (!calloutLst.contains(label.getLabelRef())) {
                    Iterator citer = calloutLst.iterator();
                    while (citer.hasNext()) {
                        CalloutLabel c = (CalloutLabel) citer.next();
                        if (c.getLabelRef().equals(label.getLabelRef())) {
                            c.setSetName(label.getSetName());
                        }
                    }
                    
                    
                    
                    
                    
                    Iterator calloutIter = calloutLst.iterator();
                    while (calloutIter.hasNext()) {
                        CalloutLabel c = (CalloutLabel) calloutIter.next();
                        if (c.getLabelRef().equalsIgnoreCase(label.getLabelRef())) {
                            calloutIter.remove();
                        }
                    }
                    
                    
                    
                    
                    
                    
                    
                    

                    calloutLst.add(new CalloutLabel(label.getLabelRef(), label.getLabelCategory(), label.getLabelType(), label.getLabelOrder(), label.getSetName(), label.getBoxQty()));
                    entity.addEntityCallout(new CalloutLabel(label.getLabelRef(), label.getLabelCategory(), label.getLabelType(), label.getLabelOrder(), label.getSetName(), label.getBoxQty()));
                    //} 
                    //}
                }
            }
        }
    }

    private ArrayList<String> getNoneRequireSetNames(ArrayList labelList) {

        // run through the list and build list of none require set names
        // sort the list by numerical value of the set
        ArrayList<String> noneRequireSetNames = new ArrayList();
        Iterator iter = labelList.iterator();
        while (iter.hasNext()) {
            Label label = (Label) iter.next();
            if ((label.getLabelRef().equalsIgnoreCase("None Require")) || (label.getLabelRef().equalsIgnoreCase("Not Required"))){
                noneRequireSetNames.add(label.getSetName());
            }
        }

        return noneRequireSetNames;

    }
}
