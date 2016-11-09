/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import Utilities.References;
import entities.Size;
import entities.UPC;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import entities.Colour;
import entities.Entity;
import entities.Stroke;
import entities.SetDetails;
import java.sql.Connection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import labels.CalloutLabel;
import labels.LabelSet;

/**
 *
 * @author michaelgoode
 */
public class SKUOutputManager {

    private static final Logger LOG = Logger.getLogger(SKUOutputManager.class.getName());

    public void writeCSV(String filename, ArrayList<Stroke> strokes, CalloutLists callouts, SizeCodeManager sizeManager, String route) {
        BufferedWriter out = null;
        LOG.info("writeCSV");
        int strokeCount = 0;
        if (strokes.size() > 0) {
            ContractCache cc = ContractCache.getInstance();
            try {
                out = new BufferedWriter(new FileWriter(filename));
                OutputManager outputManager = new OutputManager();
                out.write(References.FieldHeader);
                for (int i = 0; i < strokes.size(); i++) {
                    Stroke aStroke = strokes.get(i);
                    buildStroke(aStroke, callouts, sizeManager, outputManager);
                    boolean validated = CSVDataManager.getInstance().strokeCompare(aStroke, route);
                    outputManager.buildLines(aStroke.rfidCalloutList, validated);
                    Contract currContract = cc.getCurrentContract(aStroke.getContractNumber());
                    String status = "";
                    if (aStroke.rfidCalloutList.contains("NORAISE_") && (validated)) {
                        if (currContract.getStatus().equals("NEW")) {
                            status = cc.updateRaiseStatus(aStroke.getContractNumber(), "NORAISE");
                            outputManager.writeOutput(out, aStroke.getRfidCalloutList(), aStroke.getDepartmentNumber());
                        } else if (currContract.getStatus().equals("NORAISE")) {
                            if (currContract.isChecksumChanged()) {
                                status = cc.updateRaiseStatus(aStroke.getContractNumber(), "NORAISE");
                                outputManager.writeOutput(out, aStroke.getRfidCalloutList(), aStroke.getDepartmentNumber());
                            } else {
                                status = cc.updateRaiseStatus(aStroke.getContractNumber(), "NORAISE");
                            }
                        } else if (currContract.getStatus().equals("COMPLETE")) {
                            status = cc.updateRaiseStatus(aStroke.getContractNumber(), "COMPLETE");
                            if (currContract.isChecksumChanged()) {
                                outputManager.writeOutput(out, aStroke.getRfidCalloutList(), aStroke.getDepartmentNumber());
                            }
                        }
                    } else if (aStroke.rfidCalloutList.contains("NORAISE_") && (!validated)) {
                        if (currContract.getStatus().equals("NEW")) {
                            status = cc.updateRaiseStatus(aStroke.getContractNumber(), "NORAISE");
                            outputManager.writeOutput(out, aStroke.getRfidCalloutList(), aStroke.getDepartmentNumber());
                        } else {
                            status = "NORAISE";
                        }
                    } else {
                        // all callouts exist and the contract was successfully validated
                        if (currContract.getStatus().equals("NEW")) {
                            status = cc.updateRaiseStatus(aStroke.getContractNumber(), "COMPLETE");
                            outputManager.writeOutput(out, aStroke.getRfidCalloutList(), aStroke.getDepartmentNumber());
                        } else if (currContract.getStatus().equals("NORAISE")) {
                            status = cc.updateRaiseStatus(aStroke.getContractNumber(), "COMPLETE");
                            outputManager.writeOutput(out, aStroke.getRfidCalloutList(), aStroke.getDepartmentNumber());
                        } else if ((currContract.getStatus().equals("COMPLETE")) && (currContract.isChecksumChanged())) {
                            status = cc.updateRaiseStatus(aStroke.getContractNumber(), "COMPLETE");
                            outputManager.writeOutput(out, aStroke.getRfidCalloutList(), aStroke.getDepartmentNumber());
                        }
                    }
                    outputManager.clear();
                    aStroke.upcMatchList.clear(); // clear list of output label + upc number
                    aStroke.setStatus(status);
                    strokeCount++;
                }
                out.close();
                LOG.log(Level.INFO, null, "Generated stroke count=" + strokeCount);

            } catch (Exception ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    private void buildStroke(Stroke stroke, CalloutLists callouts, SizeCodeManager sizeManager, OutputManager outputManager) {

        if (References.SOCK_DEPARTMENTS.toUpperCase().contains(stroke.getDepartmentNumber().toUpperCase())
                && (References.HOSIERY_DESCRIPTIONS.toUpperCase().contains(stroke.getProductDesc().toUpperCase()))) {

            stroke.isHosiery = true;

        }

        StringBuilder sbWashData = new StringBuilder();
        //ArrayList<CalloutLabel> calloutLabels = new ArrayList<CalloutLabel>();
        LabelSet calloutLabels = new LabelSet();
        ArrayList<SetDetails> washcareItems = new ArrayList<SetDetails>();
        SetManager setManager = new SetManager();
        String washcareData = "";
        if (stroke.getLabelList().size() > 0 && washcareData.isEmpty()) {
            stroke.buildLabelLevelDetails(setManager, stroke.isHosiery, "stroke", "");
            stroke.buildCalloutLabels(calloutLabels, setManager, stroke.getDepartmentNumber());
        }
        stroke.manageInternational(callouts, stroke.getEntityCallouts());
        if (stroke.colourList.size() > 0) { // we have some colours
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < stroke.colourList.size(); i++) {
                Colour aColour = stroke.colourList.get(i);
                if (aColour.getLabelList().size() > 0 && washcareData.isEmpty()) {
                    aColour.buildLabelLevelDetails(setManager, stroke.isHosiery, aColour.getColourDescription(), aColour.getColourDescription());
                    
                    Iterator iter = calloutLabels.iterator();
                    while (iter.hasNext()) {
                        CalloutLabel aLabel = (CalloutLabel) iter.next();
                        if (aLabel.getLabelRef().endsWith("RF")) {
                            aLabel.setSetName(setManager.GetNameTopName(aColour.getColourDescription()));
                        }
                    }
                    aColour.buildCalloutLabels(calloutLabels, setManager, stroke.getDepartmentNumber());
                } /*else if (!calloutLabels.isEmpty()) {
                    // check for an rfid product
                    // if an rfid product is in the callout list at stroke level then add it to colourlevel
                    CalloutLabel rfidLabel = null;
                    Iterator iter = calloutLabels.iterator();
                    while (iter.hasNext()) {
                        CalloutLabel aLabel = (CalloutLabel) iter.next();
                        if ((aLabel.getLabelRef().endsWith("RF")) && (rfidLabel == null)) {
                            rfidLabel = new CalloutLabel();
                            rfidLabel.setLabelRef(aLabel.getLabelRef());
                            rfidLabel.setLabelType(aLabel.getLabelType());
                            rfidLabel.setLabelOrder(aLabel.getLabelOrder());
                            rfidLabel.setBoxQty(aLabel.getBoxQty());
                        }
                    }
                    //CalloutLabel rfidLabel = new CalloutLabel("UPC/W05185RF", "UPC / Barcode & Price Labels & Tickets", "C", 0, "longline shirt", "");
                    aColour.buildLabelLevelDetails(setManager, stroke.isHosiery);
                    aColour.buildCalloutLabels(calloutLabels, setManager, stroke.getDepartmentNumber());
                }*/
                stroke.manageInternational(callouts, aColour.getEntityCallouts());
                if (aColour.sizeList.size() > 0) {
                    for (int j = 0; j < aColour.sizeList.size(); j++) {
                        Size aSize = aColour.sizeList.get(j);
                        Size internationalSize = aColour.internationalSizeList.get(0);
                        if (aSize.getLabelList().size() > 0 && washcareData.isEmpty()) {
                            aSize.buildLabelLevelDetails(setManager, stroke.isHosiery, "size", aColour.getColourDescription());
                            aSize.buildCalloutLabels(calloutLabels, setManager, stroke.getDepartmentNumber());
                        }
                        stroke.manageInternational(callouts, aSize.getEntityCallouts());
                        if (aSize.upcList.size() > 0) {
                            for (int k = 0; k < aSize.upcList.size(); k++) {
                                UPC aUPC = aSize.upcList.get(k);

                                if (aUPC.getLabelList().size() > 0 && washcareData.isEmpty()) {
                                    aUPC.buildLabelLevelDetails(setManager, stroke.isHosiery, "upc", aColour.getColourDescription());
                                    aUPC.buildCalloutLabels(calloutLabels, setManager, stroke.getDepartmentNumber());

                                }
                                stroke.manageInternational(callouts, aUPC.getEntityCallouts());
                                stroke.buildSizeCode(calloutLabels, callouts, sizeManager);

                                if (stroke.isHosiery) {
                                    setManager.ChangeHosiery();
                                }

                                printData(aUPC, stroke, aColour, aSize, internationalSize, aUPC, setManager, callouts, outputManager, calloutLabels);
                                printData(aSize, stroke, aColour, aSize, internationalSize, aUPC, setManager, callouts, outputManager, calloutLabels);
                                printData(aColour, stroke, aColour, aSize, internationalSize, aUPC, setManager, callouts, outputManager, calloutLabels);
                                printData(stroke, stroke, aColour, aSize, internationalSize, aUPC, setManager, callouts, outputManager, calloutLabels);
                                //setManager.clear();
                            }
                        }
                    }
                }
            }
        }
    }

    private void printData(Entity entity, Stroke stroke, Colour colour, Size size, Size internationalSize, UPC upc, SetManager setManager, CalloutLists validCallouts, OutputManager outputManager) {
        entity.printData(stroke, colour, size, internationalSize, upc, setManager, validCallouts, outputManager);
    }
    
    private void printData( Entity entity, Stroke stroke, Colour colour, Size size, Size internationalSize, UPC upc, SetManager setManager, CalloutLists validCallouts, OutputManager outputManager, LabelSet calloutLabels ) {
        entity.printData(stroke, colour, size, internationalSize, upc, setManager, validCallouts, outputManager, calloutLabels);    
            
    }

}
