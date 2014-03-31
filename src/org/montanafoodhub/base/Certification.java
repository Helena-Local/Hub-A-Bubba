/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.base;

/**
 * Created by abbie on 2/6/14.
 */
public class Certification {
    // Certification ID List	Display Name	Website URL
    private String CID = "";
    private String displayName = "";
    private String websiteUrl = "";
    private String iconUrl = "";

    public Certification() {

    }

    public Certification(String cid, String displayName, String websiteUrl, String iconUrl) {
        this.CID = cid;
        this.displayName = displayName;
        this.websiteUrl = websiteUrl;
        this.iconUrl = iconUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String toString() {
        return "CID = " + CID + "; displayName = " + displayName + "; websiteUrl = " + websiteUrl + "; iconUrl = " + iconUrl;
    }
}
