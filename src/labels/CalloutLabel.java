/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

/**
 *
 * @author michaelgoode
 */
public class CalloutLabel extends Label {
    
    private boolean addedInternational = false;
    
    public CalloutLabel(){
        
    }
    public CalloutLabel( String ref, String cat, String type, int order, String set, String boxQty ) {
        super(ref,cat,type,order,set,boxQty);
    }
    
    public boolean IsInternational() {
        return false;
    }

    public boolean isAddedInternational() {
        return addedInternational;
    }

    public void setAddedInternational(boolean addedInternational) {
        this.addedInternational = addedInternational;
    }
    
}
