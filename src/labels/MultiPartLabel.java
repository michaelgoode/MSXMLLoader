/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

import labels.Label;

/**
 *
 * @author michaelgoode
 */
public class MultiPartLabel extends Label {
    
    public MultiPartLabel(){
        
    }
    public MultiPartLabel( String ref, String cat, String type, int order, String set, String boxQty ) {
        super(ref,cat,type,order,set,boxQty);
    }
    
}
