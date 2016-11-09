/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import labels.CalloutLabel;
import labels.FireWarningLabel;
import labels.ZCodeLabel;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author michaelgoode
 */
public class SetDetails implements Serializable {
    
    final static int MAX_FIBRES = 8;
    
    String level = "";
    String SetName      = "";
    String multiPartSet = "";
    String madeIn       = "";
    
    public ArrayList<ZCodeLabel> zcodes = new ArrayList();
    public ArrayList<CalloutLabel> cares = new ArrayList();
    public ArrayList<FireWarningLabel> fires = new ArrayList();
    
    public SetDetails( String name, String level ) {
        this.SetName = name;
        this.level = level;
    }

    public String getSetName() {
        return SetName;
    }

    public void setSetName(String SetName) {
        this.SetName = SetName;
    }

    public String getMultiPartSet() {
        return multiPartSet;
    }

    public void setMultiPartSet(String multiPartSet) {
        this.multiPartSet = multiPartSet;
    }

    public String getMadeIn() {
        return madeIn;
    }

    public void setMadeIn(String madeIn) {
        this.madeIn = madeIn;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<ZCodeLabel> getZcodes() {
        return zcodes;
    }

    public String getZCodesText(StringBuilder sb) {
        for (int i = 0; i < this.zcodes.size(); i++) {
            sb.append(zcodes.get(i).toString());
        }
        return sb.toString();
    }
    
    public String getFibresText(StringBuilder sb) {
        return sb.toString();
    }
    
    public String getCaresText(StringBuilder sb) {
        
        for (int i = 0; i < this.cares.size(); i++) {
            sb.append(cares.get(i).toString());
        }
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append( buildTag("", zcodes,8, "|"));
        sb.append( buildTag("",cares,8,"|"));
        sb.append(this.madeIn + "|");
        sb.append( buildTag("",fires,8,"|"));
        sb.append( this.multiPartSet + "|" );
        sb.append( this.SetName );
        return sb.toString();  
    }
    
    private String buildTag( String ID, ArrayList items, int _MAX, String _DELIM ) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            //if ( i < items.size()-1 ) 
                sb.append(ID + items.get(i).toString() + _DELIM);
            //else sb.append(ID + items.get(i).toString());
        }       
        if ( items.size() < _MAX ) {          
            for ( int i = items.size(); i < _MAX; i++ ) {
                
                    sb.append(ID + _DELIM);
                
            }          
        }
        return sb.toString();
    }
    
    private String buildFibreTag( ArrayList items, String beginTag, String endTag, int _MAX,String _DELIM ) {
        StringBuilder sb = new StringBuilder();
        //sb.append(beginTag);
        for (int i = 0; i < items.size(); i++) {
            if ( i < items.size()-1 ) 
                sb.append(items.get(i).toString() + _DELIM);
            else sb.append(items.get(i).toString());
        }
        
        if ( items.size() < _MAX ) {
            
            for ( int i = items.size(); i < _MAX; i++ ) {
                sb.append(_DELIM);
            }
            
            
        }
        
        sb.append("|");

        return sb.toString();
    }

    

    
    
    
}
