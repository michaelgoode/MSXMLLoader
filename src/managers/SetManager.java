/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import Utilities.References;
import entities.SetDetails;
import java.util.Iterator;
import java.util.TreeMap;
import labels.ZCodeLabel;

/**
 *
 * @author michaelgoode
 */
public class SetManager {

    private static final SetManager instance = new SetManager();
    private TreeMap<String, SetDetails> items = new TreeMap<String, SetDetails>(); // map of set names against item details

    public static SetManager getInstance() {
        return instance;
    }

    public SetManager() {
    }

    public void clear() {
        items.clear();
    }

    public int count() {
        return items.size();
    }

    public void addSet(SetDetails sd) {

        items.put(sd.getSetName(), sd);

    }

    public SetDetails get(String setname) {
        return items.get(setname);
    }

    public String GetNameTopName() {
        String s = "";
        if (items.size() > 0) {
            Iterator iter = items.keySet().iterator();

            while (iter.hasNext() && s.equals("")) {

                String item = (String) iter.next();
                if (!item.equals(References.VoidSetName)) {
                    s = item;
                }
            }
            return s;
        } else {
            return "";
        }
    }

    public String GetNameTopName(String level) {
        String s = "";
        if (items.size() > 0) {
            Iterator iter = items.values().iterator();
            while (iter.hasNext() && s.equals("")) {
                SetDetails item = (SetDetails) iter.next();
                if (item.getLevel().equalsIgnoreCase(level)) {
                    s = item.getSetName();
                }
            }
            return s;
        } else {
            return "";
        }
    }

    public boolean containsName(String name) {
        return items.containsKey(name);
    }

    public String getString() {
        String s = "";
        Iterator iter = items.values().iterator();
        while (iter.hasNext()) {

            SetDetails sd = (SetDetails) iter.next();
            s = s + sd.toString();

        }

        return s;

    }

    public void ChangeHosiery() {
        // rebuild set for hosiery reasons
        // no need to run scenario if only a single setname exists
        try {
            SetDetails sd = (SetDetails) items.firstEntry().getValue();
            if ((!sd.getSetName().equalsIgnoreCase(References.HOSIERY_DEFAULT_SETNAME)) && (items.size() > 2)) {

                Iterator iter = items.values().iterator();
                while (iter.hasNext()) {
                    while (iter.hasNext()) {
                        sd = (SetDetails) iter.next();
                        if ((!sd.getSetName().equals(References.VoidSetName)) && (!sd.getSetName().equals(""))) {
                            ZCodeLabel zl = sd.zcodes.get(0);
                            sd.setSetName(sd.getSetName().replaceAll(" ", ""));
                            sd.setSetName(sd.getSetName().replaceAll(",", ""));
                            zl.setLabelRef(sd.getSetName());
                        }
                    }
                }

                if (items.size() > 1) {
                    iter = items.values().iterator();
                    SetDetails topSet = (SetDetails) iter.next();
                    topSet.setSetName(References.HOSIERY_DEFAULT_SETNAME);

                    while (iter.hasNext()) {
                        sd = (SetDetails) iter.next();
                        if ((!sd.getSetName().equals(References.VoidSetName)) && (!sd.getSetName().equals(""))) {
                            topSet.zcodes.add(sd.zcodes.get(0));
                        }
                    }
                    TreeMap<String, SetDetails> tempMap = new TreeMap<String, SetDetails>();
                    iter = items.values().iterator();
                    while (iter.hasNext()) {
                        sd = (SetDetails) iter.next();
                        if ((sd.getSetName().equals(References.HOSIERY_DEFAULT_SETNAME))) {
                            tempMap.put(References.HOSIERY_DEFAULT_SETNAME, sd);
                        }
                    }
                    items = tempMap;

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
