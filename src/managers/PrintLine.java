/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

/**
 *
 * @author michaelgoode
 */
public class PrintLine {
    private String callout;
    private String line;
    
    public PrintLine(String callout, String s) {
        this.callout = callout;
        this.line = s;
    }

    public String getCallout() {
        return callout;
    }

    public void setCallout(String callout) {
        this.callout = callout;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return  line;
    }
    
    public String printLine() {
        return callout.trim() + "|" + line;
    }
    
    
    
    
    
}
