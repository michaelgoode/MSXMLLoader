/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entities.Callout;
import entities.UKProduct;
import java.util.ArrayList;
import java.util.Iterator;
import labels.CalloutLabel;

/**
 *
 * @author michaelgoode
 */
public class InternationalManager {

    public InternationalManager() {
    }

    public void expandInternationalCallouts(CalloutLists calloutLists, ArrayList<CalloutLabel> entityCallouts) {
        // check all entity callouts and apply to international rule
        ArrayList<CalloutLabel> internationalCallouts = new ArrayList();
        boolean internationalExists = false;
        Iterator iter = entityCallouts.iterator();
        while (iter.hasNext()) {
            CalloutLabel label = (CalloutLabel) iter.next();
            if (calloutLists.internationalProducts.contains(label.getLabelRef())) {
                internationalExists = true;
            }
        }

        if (!internationalExists) {
            iter = entityCallouts.iterator();
            while (iter.hasNext()) {
                CalloutLabel calloutLabel = (CalloutLabel) iter.next();
                String callout = calloutLabel.getLabelRef().toUpperCase();
                UKProduct p = calloutLists.getUKProduct(callout); // get the international label from the list
                if (p != null) {
                    if ((!p.getInternationalCallout().equals(callout)) && (!p.getInternationalCallout().equals("")) && (!p.getInternationalCallout().equals("NONE"))) {
                        if (!entityCallouts.contains(p.getInternationalCallout())) {
                            CalloutLabel cl = new CalloutLabel();
                            cl.setAddedInternational(true);
                            cl.setLabelRef(p.getInternationalCallout());
                            cl.setSetName(calloutLabel.getSetName());
                            internationalCallouts.add(cl);
                        }
                    }
                }
            }
            if (internationalCallouts.size() > 0) {
                entityCallouts.addAll(internationalCallouts);
            }
        }
    }
}
