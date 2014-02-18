/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

import java.util.Calendar;

/**
 * Created by abbie on 2/2/14.
 */
public class Order {
    private Calendar date;
    private String OID = "";
    private String IID = "";
    private String PID = "";
    private String BID = "";
    private String buyerUrl = "";

    public Order() {
        setDate(null);
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        if (date == null) {
            date = Calendar.getInstance();
            date.set(Calendar.DAY_OF_YEAR, 1);
            date.set(Calendar.YEAR, 1970);
        }
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
