/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

/**
 * Created by abbie on 2/2/14.
 *
 */
public class Buyer {
    private String BID = "";
    private String name = "";
    private String contactEmail = "";
    private String hours = "";
    private String phone = "";
    private String websiteUrl = "";
    private String photoUrl = "";
    private String location = "";
    private String serviceLevel = "";
    private String lastOrderDate = "";

    public Buyer() {

    }

    public Buyer(String BID, String name, String contactEmail, String hours, String phone, String websiteUrl, String photoUrl, String location, String lastOrderDate) {
        this.BID = BID;
        this.name = name;
        this.contactEmail = contactEmail;
        this.hours = hours;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
        this.photoUrl = photoUrl;
        this.location = location;
        this.lastOrderDate = lastOrderDate;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getLastOrderDate() {
        return lastOrderDate;
    }

    public void setLastOrderDate(String lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String toString() {
        return "BID = " + BID + "; name = " + name + "; contactEmail = " + contactEmail
                + "; hours = " + hours + "; phone = " + phone + "; websiteUrl = " + websiteUrl + "; photoUrl = "
                + photoUrl + "; location = " + location + "; lastOrderDate = " + lastOrderDate;
    }
}
