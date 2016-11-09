/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Objects;

/**
 *
 * @author michaelgoode
 */
public class UKProduct {
    
    String Callout;
    String internationalCallout;

    public UKProduct(String Callout, String internationalCallout) {
        this.Callout = Callout;
        this.internationalCallout = internationalCallout;
    }

    public UKProduct() {
    }

    public String getCallout() {
        return Callout;
    }

    public void setCallout(String Callout) {
        this.Callout = Callout;
    }

    public String getInternationalCallout() {
        return internationalCallout;
    }

    public void setInternationalCallout(String internationalCallout) {
        this.internationalCallout = internationalCallout;
    }

    

    
    
    
}
