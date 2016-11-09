/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import Utilities.MD5;
import entities.Stroke;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author michaelgoode
 */
public class OutputManager {

    private ArrayList<PrintLine> lines = new ArrayList<PrintLine>();

    public void addLine(String callout, String sline) {
        try {
            lines.add(new PrintLine(callout, sline));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLine(PrintLine pline) {
        try {
            lines.add(pline);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildLines(ArrayList validRFIDCallouts, boolean validated) {
        for (PrintLine p : lines) {
            if (p.getCallout().contains("RF")) { // output only valid rfid items
                if (!validated) {
                    if (!validRFIDCallouts.contains(p.getCallout())) { // is it an RFID product ?
                        p.setCallout("NORAISE_"); // raise a default item if rfid item does not exist
                        validRFIDCallouts.add("NORAISE_");
                    }
                } else {
                    validRFIDCallouts.add(p.getCallout());
                }
            }
        }
    }

    public void writeOutput(BufferedWriter out, ArrayList validRFIDCallouts, String dept) throws IOException {
        RFIDDepartments rfidDepartments = RFIDDepartments.getInstance();
        for (PrintLine p : lines) {
            if (p.getCallout().contains("RF")) { // output only valid rfid items
                if (validRFIDCallouts.contains(p.getCallout()) && (rfidDepartments.contains(dept))) { // is it an RFID product ?
                    out.write(p.printLine());
                } else if (rfidDepartments.contains(dept)) {
                    p.setCallout("NORAISE_"); // raise a default item if rfid item does not exist
                    //validRFIDCallouts.add("NORAISE_");
                    out.write(p.printLine());
                }
            } else {
                // catches non rfid items and outputs them unconditionally
                out.write(p.printLine());
            }
        }
        this.clear();
    }

    public String getChecksum() {
        return MD5.getInstance().hash(lines.toString());
    }

    public void clear() {
        lines.clear();
    }

}
