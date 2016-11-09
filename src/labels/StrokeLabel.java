/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

import java.io.Serializable;

/**
 *
 * @author michaelgoode
 */
public class StrokeLabel extends Label implements Serializable {

    public StrokeLabel() {
    }

    public StrokeLabel(String ref, String cat, String type, int order, String set, String boxQty) {
        super(ref, cat, type, order, set, boxQty);

    }
}
