/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.Size;
import entities.Callout;
import entities.SetDetails;
import labels.ColourLabel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import labels.CalloutLabel;
import labels.ColourLabel;
import labels.FibreContentLabel;
import labels.FireWarningLabel;
import labels.MadeInLabel;
import labels.ZCodeLabel;
import managers.LabelListManager;

/**
 *
 * @author michaelgoode
 */
public class Colour extends Entity {

    private String colourName;
    private String colourDescription;
    private String storyDescription;
    //public ArrayList<ColourLabel> colourLabelList = new ArrayList();
    public ArrayList<Size> sizeList = new ArrayList();
    public ArrayList<Size> internationalSizeList = new ArrayList();
    ArrayList<SetDetails> washcareItems = new ArrayList();

    

    

    public void getCallouts(ArrayList callouts) {

        HashSet<FibreContentLabel> fibreList = new HashSet<FibreContentLabel>();
        HashSet<FireWarningLabel> fireList = new HashSet<FireWarningLabel>();
        HashSet<ZCodeLabel> zCodeList = new HashSet<ZCodeLabel>();
        HashSet<CalloutLabel> careList = new HashSet<CalloutLabel>();
        HashSet<MadeInLabel> madeInList = new HashSet<MadeInLabel>();


        Iterator iter = this.getLabelList().iterator();
        while (iter.hasNext()) {

            ColourLabel colourLabel = (ColourLabel) iter.next();

            if ((colourLabel.getLabelCategory().equals("Care Label (Format\\Size)")) && (colourLabel.getLabelType().equals("K"))) {
                Callout callout = new Callout();
                callout.setProductCallout(colourLabel.getLabelRef());
                callout.setSetName(colourLabel.getSetName());
                callouts.add(callout);
            } else if ((colourLabel.getLabelCategory().equals("Fibres & Compositions")) && (colourLabel.getLabelType().equals("K"))) {
                FibreContentLabel fibre = new FibreContentLabel();
                fibre.setLabelRef(colourLabel.getLabelRef());
                fibre.setLabelCategory(colourLabel.getLabelCategory());
                fibre.setLabelType(colourLabel.getLabelType());
                fibre.setLabelOrder(colourLabel.getLabelOrder());
                fibre.setSetName(colourLabel.getSetName());
                fibreList.add(fibre);
            } else if ((colourLabel.getLabelCategory().equals("Fire Warning \\ Caution")) && (colourLabel.getLabelType().equals("K"))) {
                FireWarningLabel fire = new FireWarningLabel();
                fire.setLabelRef(colourLabel.getLabelRef());
                fire.setLabelCategory(colourLabel.getLabelCategory());
                fire.setLabelType(colourLabel.getLabelType());
                fire.setLabelOrder(colourLabel.getLabelOrder());
                fire.setSetName(colourLabel.getSetName());
                fireList.add(fire);
            } else if ((colourLabel.getLabelCategory().equals("Other Words\\Phrases (Z Codes)")) && (colourLabel.getLabelType().equals("K"))) {
                ZCodeLabel zcode = new ZCodeLabel();
                zcode.setLabelRef(colourLabel.getLabelRef());
                zcode.setLabelCategory(colourLabel.getLabelCategory());
                zcode.setLabelType(colourLabel.getLabelType());
                zcode.setLabelOrder(colourLabel.getLabelOrder());
                zcode.setSetName(colourLabel.getSetName());
                zCodeList.add(zcode);
            } else if ((colourLabel.getLabelCategory().equals("Care Instructions (CL Codes)")) && (colourLabel.getLabelType().equals("K"))) {
                CalloutLabel care = new CalloutLabel();
                care.setLabelRef(colourLabel.getLabelRef());
                care.setLabelCategory(colourLabel.getLabelCategory());
                care.setLabelType(colourLabel.getLabelType());
                care.setLabelOrder(colourLabel.getLabelOrder());
                care.setSetName(colourLabel.getSetName());
                careList.add(care);
            } else if ((colourLabel.getLabelCategory().equals("Made in... (Country Codes)")) && (colourLabel.getLabelType().equals("K"))) {
                MadeInLabel madeIn = new MadeInLabel();
                madeIn.setLabelRef(colourLabel.getLabelRef());
                madeIn.setLabelCategory(colourLabel.getLabelCategory());
                madeIn.setLabelType(colourLabel.getLabelType());
                madeIn.setLabelOrder(colourLabel.getLabelOrder());
                madeIn.setSetName(colourLabel.getSetName());
                madeInList.add(madeIn);
            }

        }

        Iterator calloutIter = callouts.iterator();
        while (calloutIter.hasNext()) {
            Callout callout = (Callout) calloutIter.next();
            callout.addFibres(fibreList);
        }

        Iterator sizeIter = sizeList.iterator();
        while (sizeIter.hasNext()) {
            Size size = (Size) sizeIter.next();
            size.getCallouts(callouts);
        }

    }
    
    public void addSize( Size size ) {
        this.sizeList.add(size);
        if (this.internationalSizeList.isEmpty()) {
            this.internationalSizeList.add(size);
        }
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public String getColourDescription() {
        return colourDescription;
    }

    public void setColourDescription(String colourDescription) {
        this.colourDescription = colourDescription;
    }

    public String getStoryDescription() {
        return storyDescription;
    }

    public void setStoryDescription(String storyDescription) {
        this.storyDescription = storyDescription;
    }

    @Override
    public String toString() {
        //return "Colour{" + colourName + "," + colourDescription + "," + storyDescription + '}';
        return colourName + _DELIM + colourDescription + _DELIM + storyDescription + _DELIM + this.getBoxQuantity() + _DELIM;
    }

   
}
