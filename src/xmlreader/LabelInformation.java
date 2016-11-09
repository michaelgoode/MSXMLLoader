/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlreader;

/**
 *
 * @author michaelgoode
 */
public class LabelInformation {
    
    private String date;
    private String time;
    private int recordCount;
    private String runNumber;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(String runNumber) {
        this.runNumber = runNumber;
    }

    @Override
    public String toString() {
        return "LabelInformation{" + "date=" + date + ", time=" + time + ", recordCount=" + recordCount + ", runNumber=" + runNumber + '}';
    }
    
}
