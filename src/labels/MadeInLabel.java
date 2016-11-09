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
public class MadeInLabel extends Label {
    
    public MadeInLabel(){
        
    }
    public MadeInLabel( String ref, String cat, String type, int order, String set, String boxQty ) {
        super(ref,cat,type,order,set,boxQty);
    }
    
}
