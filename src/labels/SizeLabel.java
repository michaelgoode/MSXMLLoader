/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

/**
 *
 * @author michaelgoode
 */
public class SizeLabel extends Label {
    
    
    
    public SizeLabel() {
        
    }
    
    public SizeLabel( String ref, String cat, String type, int order, String set, String boxQty ) {
        super(ref,cat,type,order,set,boxQty);
        
    }

    @Override
    public String toString() {
        return "SizeLabel{" + "labelRef=" + labelRef + ", labelType=" + labelType + ", labelCategory=" + labelCategory + ", labelOrder=" + labelOrder + ", setName=" + SetName + ", boxQuantity=" + boxQty + '}';
    }
    
    
    
}
