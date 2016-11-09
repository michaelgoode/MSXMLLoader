/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.util.Objects;


/**
 *
 * @author michaelgoode
 */
public class SetName {
    private String setName = "";
    private String derivedSetName = "";
    

    public SetName() {
    }
    
    public SetName(String sSet, String sDerivedSet ) {
        this.setName = sSet;
        this.derivedSetName = sDerivedSet;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }

    public String getDerivedSetName() {
        return derivedSetName;
    }

    public void setDerivedSetName(String derivedSetName) {
        this.derivedSetName = derivedSetName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.setName);
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
        final SetName other = (SetName) obj;
        if (!Objects.equals(this.setName, other.setName)) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
