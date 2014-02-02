package org.helenalocal.base;

/**
 * Created by abbie on 2/2/14.
 */
public class Order {
    private String timeStamp = "";
    private String OID = "";
    private String IID = "";
    private String PID = "";
    private String BID = "";

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getOrderID() {
        return OID;
    }

    public void setOrderID(String OID) {
        this.OID = OID;
    }

    public String getItemID() {
        return IID;
    }

    public void setItemID(String IID) {
        this.IID = IID;
    }

    public String getProducerID() {
        return PID;
    }

    public void setProducerID(String PID) {
        this.PID = PID;
    }

    public String getBuyerID() {
        return BID;
    }

    public void setBuyerID(String BID) {
        this.BID = BID;
    }
}
