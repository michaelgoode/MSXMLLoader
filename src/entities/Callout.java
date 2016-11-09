/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import labels.FibreContentLabel;
import labels.CalloutLabel;
import labels.MadeInLabel;
import java.io.Serializable;
import labels.ZCodeLabel;
import labels.Label;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

/**
 *
 * @author michaelgoode
 */
public class Callout implements Serializable {

    String productCallout;
    String SetName;
    String calloutType;
    
    ArrayList<Label> fibres = new ArrayList();
    ArrayList<Label> careInstructions = new ArrayList();
    ArrayList<Label> zCodes = new ArrayList();
    ArrayList<UPC> upcList = new ArrayList();
    ArrayList<Label> fireWarnings = new ArrayList();
    ArrayList<Label> madeInLst = new ArrayList();

    
    

    public String getProductCallout() {
        return productCallout;
    }

    public void setProductCallout(String productCallout) {
        this.productCallout = productCallout;
    }

    public String getSetName() {
        return SetName;
    }

    public void setSetName(String setName) {
        this.SetName = setName;
    }

    public String getCalloutType() {
        return calloutType;
    }

    public void setCalloutType(String calloutType) {
        this.calloutType = calloutType;
    }
    
    
    
    public void addFibres( HashSet lstfibres ) {    
        Iterator iter = lstfibres.iterator();
        while (iter.hasNext()) {
            FibreContentLabel fibre = (FibreContentLabel) iter.next();
            if (fibre.getSetName().equals(this.SetName)) {
                 fibres.add(fibre);
            }
        }
    }
    
    public void addCareInstructions( HashSet lstCares ) {    
        Iterator iter = lstCares.iterator();
        while (iter.hasNext()) {
            CalloutLabel ci = (CalloutLabel) iter.next();
            if (ci.getSetName().equals(this.SetName)) {
                 careInstructions.add(ci);
            }
        }
    }
    
    public void addMadeIn( HashSet lstMadeIn ) {    
        Iterator iter = lstMadeIn.iterator();
        while (iter.hasNext()) {
            MadeInLabel mi = (MadeInLabel) iter.next();
            if (mi.getSetName().equals(this.SetName)) {
                 madeInLst.add(mi);
            }
        }
    }
    
    public void addZCodes( HashSet lstZCode ) {    
        Iterator iter = lstZCode.iterator();
        while (iter.hasNext()) {
           ZCodeLabel zc = (ZCodeLabel) iter.next();
            if (zc.getSetName().equals(this.SetName)) {
                 zCodes.add(zc);
            }
        }
    }
    
}



