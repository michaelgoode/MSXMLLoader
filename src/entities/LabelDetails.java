/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;
import labels.CalloutLabel;
import labels.FibreContentLabel;
import labels.FireWarningLabel;
import labels.MadeInLabel;
import labels.ZCodeLabel;

/**
 *
 * @author michaelgoode
 */
public class LabelDetails {
    
    TreeSet<FibreContentLabel> fibreList = new TreeSet<FibreContentLabel>();
    HashSet<FireWarningLabel> fireList = new HashSet<FireWarningLabel>();
    HashSet<ZCodeLabel> zCodeList = new HashSet<ZCodeLabel>();
    HashSet<CalloutLabel> careList = new HashSet<CalloutLabel>();
    HashSet<MadeInLabel> madeInList = new HashSet<MadeInLabel>();
    
    public void addFibre( FibreContentLabel fibre ){
        fibreList.add(fibre);
    }
    public void addFireWarning( FireWarningLabel fireWarning ) {
        fireList.add(fireWarning);
    }
    public void addZCode( ZCodeLabel zcode ) {
        zCodeList.add(zcode);
    }
    public void addCareInstruction( CalloutLabel care ) {
        careList.add(care);
    }
    public void addMadeInList( MadeInLabel madeIn ) {
        madeInList.add(madeIn);
    }

    

    
    
}
