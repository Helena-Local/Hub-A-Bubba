package org.helenalocal.base;

/**
 * Created by abbie on 2/2/14.
 */
public class Order {
    private String date = "";
    private String OID = "";
    private String IID = "";
    private String PID = "";
    private String BID = "";
    private String buyerUrl = "";

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getBuyerUrl() {
        return buyerUrl;
    }

    public void setBuyerUrl(String buyerUrl) {
        this.buyerUrl = buyerUrl;
    }

    public String toString() {
        return "date = " + date + "; OID = " + OID + "; IID = " + IID + "; PID = " + PID + "; BID = " + BID + "; buyerUrl = " + buyerUrl;
    }

}
