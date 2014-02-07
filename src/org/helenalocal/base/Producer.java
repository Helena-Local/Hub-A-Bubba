/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;


/**
 * Created by abbie on 2/2/14.
 */
public class Producer {
    private String PID = "";
    private String name = "";
    private String contactEmail = "";
    private String websiteUrl = "";
    private String photoUrl = "";
    private String location = "";
    private String certificationID = "";
    private String quote = "";

    public Producer() {
    }

    public Producer(String PID, String name, String contactEmail, String websiteUrl, String photoUrl, String location, String certificationID, String quote) {
        this.PID = PID;
        this.name = name;
        this.contactEmail = contactEmail;
        this.websiteUrl = websiteUrl;
        this.photoUrl = photoUrl;
        this.location = location;
        this.certificationID = certificationID;
        this.quote = quote;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPID() {
        return PID;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getCertificationID() {
        return certificationID;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setCertificationID(String certificationID) {
        this.certificationID = certificationID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setPID(String PID) {
        this.PID = PID;
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

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String toString() {
        return "producer.PID = " + this.PID + "; producer.name = " + this.name + "; producer.contactEmail = " +
                this.contactEmail + "; producer.websiteUrl = " + this.websiteUrl + "; producer.photoUrl = " +
                this.photoUrl + "; producer.location = " + this.location + "; producer.certificationID" + certificationID
                + "; quote = " + quote;
    }

    public String toEmail() {
        return "\nproducer.PID = " + this.PID + "; \nproducer.name = " + this.name + "; \nproducer.contactEmail = " + this.contactEmail + ";";
    }

    public String toCSV() {
        return this.PID + "," + this.name + "," + this.contactEmail + "," + this.websiteUrl + "," +
                this.photoUrl + "," + this.location + "," + certificationID + "," + quote;
    }
}
