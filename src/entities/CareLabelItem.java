/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.HashSet;
import java.util.Objects;

/**
 *
 * @author michaelgoode
 */
public class CareLabelItem { 
    Callout callout;
    LabelDetails labelDetails;
    public CareLabelItem() {   
    }
    public void addCallout( Callout callout ) {
        this.callout = callout;
    }
    public void setDetails( LabelDetails labelDetails ) {
        this.labelDetails = labelDetails;
    }

    

    
    
    
}
