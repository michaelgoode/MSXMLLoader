/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package labels;

import java.util.Objects;
import labels.Label;

/**
 *
 * @author michaelgoode
 */
public class FibreContentLabel extends Label {

    String code;
    float percent;

    public FibreContentLabel() {
    }

    public FibreContentLabel(Label l) {
        super(l);
    }

    public FibreContentLabel(String ref, String cat, String type, int order, String set, String boxQty) throws LabelException {
        super(ref, cat, type, order, set, boxQty);
        ref = ref.trim();
        try {
            if (ref.contains(" ")) {
                String[] values = ref.split(" ");
                // split the lookup code from the percentage content
                if (values.length == 2) {
                    code = values[0];
                    percent = Float.parseFloat(values[1]);
                }
            } else {
                if (!ref.isEmpty()) {
                    percent = Float.parseFloat(ref);
                    code = "";
                } else {
                    percent = 0.0F;
                    code = "";
                }
            }
        } catch (Exception e) {
            throw new LabelException();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        if (percent > 0) {
            return percent + "% (" + code + ")";
        } else {
            return "";
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.code);
        hash = 17 * hash + Float.floatToIntBits(this.percent);
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
        final FibreContentLabel other = (FibreContentLabel) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        if (Float.floatToIntBits(this.percent) != Float.floatToIntBits(other.percent)) {
            return false;
        }
        return true;
    }

}
