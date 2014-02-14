/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

/**
 * Created by abbie on 2/13/14.
 */
public class Ad {
    private String imageUrl = "";
    private String BID = "";

    public Ad() {
    }

    public Ad(String imageUrl, String BID) {
        this.imageUrl = imageUrl;
        this.BID = BID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBID() {
        return BID;
    }

    public void setBID(String BID) {
        this.BID = BID;
    }

    @Override
    public String toString() {
        return "this.imageUrl = " + imageUrl + "; this.BID = " + BID;
    }
}
