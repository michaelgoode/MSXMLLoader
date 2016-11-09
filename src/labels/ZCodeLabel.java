/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author michaelgoode
 */
public class ZCodeLabel extends Label {
    
    public ArrayList<FibreContentLabel> fibreLabels = new ArrayList<FibreContentLabel>();
    
    public ZCodeLabel(){
        
    }
    
    public ZCodeLabel( String ref, String cat, String type, int order, String set, String boxQty ) {
        super(ref,cat,type,order,set,boxQty);
    }

    
    
    public ZCodeLabel( Label l ) {     
        this(l.getLabelRef(),l.getLabelCategory(),l.getLabelType(),l.getLabelOrder(),l.getSetName(),l.getBoxQty());
    }
    
    public void addFibre( FibreContentLabel label ) {
        fibreLabels.add(label);
    }

    @Override
    public String toString() {
        return this.labelRef + "~" + fibreLabels.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.fibreLabels);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ZCodeLabel other = (ZCodeLabel) obj;
        if (!Objects.equals(this.fibreLabels, other.fibreLabels)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
