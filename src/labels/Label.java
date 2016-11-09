/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author michaelgoode
 */
public abstract class Label implements Comparable<Label>, Serializable {

    final static String _DELIM = "^";
    String labelRef;
    String labelCategory;
    String labelType;
    int labelOrder;
    String boxQty = "0";

    String SetName;

    public Label() {
        this.labelRef = "";
        this.labelCategory = "";
        this.labelType = "";
        this.labelOrder = 0;
        this.SetName = "";

    }

    public Label(String ref, String cat, String type, int order, ArrayList<String> setNames) {
        this.labelRef = ref;
        this.labelCategory = cat;
        this.labelType = type;
        this.labelOrder = order;

    }

    public Label(String ref, String cat, String type, int order, String setName, String boxQty) {
        this.labelRef = ref;
        this.labelCategory = cat;
        this.labelType = type;
        this.labelOrder = order;
        this.boxQty = boxQty;

        if (setName == null) {
            this.SetName = "";
        } else {
            this.SetName = setName;
        }
    }

    public Label(Label l) {
        this.labelRef = l.labelRef;
        this.labelCategory = l.labelCategory;
        this.labelType = l.labelType;
        this.labelOrder = l.labelOrder;
        this.SetName = l.SetName;
        this.boxQty = l.boxQty;
    }

    
    
    
    
    

    @Override
    public int compareTo(Label label) {
        
        
        return label.SetName.compareTo(this.SetName);
        
        
        
        
        
        /*
        
        
        int labelOrder = ((Label) label).getLabelOrder();
        int result = 0;
        
        String setName = ((Label) label).getSetName();
        
        if (setName.compareTo(this.getSetName()) == 0) {
            
            
            
            
            
        } else {
            
            return (int) (labelOrder - this.labelOrder);
            
        }
        
        
        
        if (labelOrder == this.getLabelOrder()) {
            //result = labelRef.compareTo(this.getLabelRef());
            //if (result == 0) {
                result = setName.compareTo(this.getSetName());
            //}
            return result;
        } else {
            return (int) (labelOrder - this.labelOrder);
        }
                
                */
    }

    public String getLabelRef() {
        return labelRef;
    }

    public void setLabelRef(String labelRef) {
        this.labelRef = labelRef;
    }

    public String getLabelCategory() {
        return labelCategory;
    }

    public void setLabelCategory(String labelCategory) {
        this.labelCategory = labelCategory;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public int getLabelOrder() {
        return labelOrder;
    }

    public void setLabelOrder(int labelOrder) {
        this.labelOrder = labelOrder;
    }

    public String getSetName() {
        if (SetName == null) {
            SetName = "";
        }
        return SetName;
    }

    public void setSetName(String SetName) {
        this.SetName = SetName;
    }

    public String getBoxQty() {
        return boxQty;
    }

    public void setBoxQty(String boxQty) {
        this.boxQty = boxQty;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.labelRef);
        hash = 67 * hash + Objects.hashCode(this.labelCategory);
        hash = 67 * hash + Objects.hashCode(this.labelType);
        hash = 67 * hash + this.labelOrder;
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
        final Label other = (Label) obj;
        if (!Objects.equals(this.labelRef, other.labelRef)) {
            return false;
        }
        if (!Objects.equals(this.labelCategory, other.labelCategory)) {
            return false;
        }
        if (!Objects.equals(this.labelType, other.labelType)) {
            return false;
        }
        if (this.labelOrder != other.labelOrder) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return labelRef;

        //return "Label{" + "labelRef=" + labelRef + ", labelCategory=" + labelCategory + ", labelType=" + labelType + ", labelOrder=" + labelOrder + ", setName=" + setName + '}';
    }

}
