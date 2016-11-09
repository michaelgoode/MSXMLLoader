/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

import entities.SetDetails;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author michaelgoode
 */
public class UPCLabel extends Label {
    
    String labelRef;
    String labelType;
    String labelCategory;
    int labelOrder;
    
    
    
    ArrayList<SetDetails> washcareItems = new ArrayList();

    public String getLabelRef() {
        return labelRef;
    }

    public void setLabelRef(String labelRef) {
        this.labelRef = labelRef;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getLabelCategory() {
        return labelCategory;
    }

    public void setLabelCategory(String labelCategory) {
        this.labelCategory = labelCategory;
    }

    public int getLabelOrder() {
        return labelOrder;
    }

    public void setLabelOrder(int labelOrder) {
        this.labelOrder = labelOrder;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.labelRef);
        hash = 41 * hash + Objects.hashCode(this.labelType);
        hash = 41 * hash + Objects.hashCode(this.labelCategory);
        hash = 41 * hash + this.labelOrder;
        hash = 41 * hash + Objects.hashCode(this.washcareItems);
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
        final UPCLabel other = (UPCLabel) obj;
        if (!Objects.equals(this.labelRef, other.labelRef)) {
            return false;
        }
        if (!Objects.equals(this.labelType, other.labelType)) {
            return false;
        }
        if (!Objects.equals(this.labelCategory, other.labelCategory)) {
            return false;
        }
        if (this.labelOrder != other.labelOrder) {
            return false;
        }
        if (!Objects.equals(this.washcareItems, other.washcareItems)) {
            return false;
        }
        return true;
    }

    

  
   

    @Override
    public String toString() {
        //return "UPCLabel{" + "labelRef=" + labelRef + ", labelType=" + labelType + ", labelCategory=" + labelCategory + ", labelOrder=" + labelOrder + ", setName=" + setName + ", boxQuantity=" + boxQuantity + "}";
        return "";
    }
     
}
