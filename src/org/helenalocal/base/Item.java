/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by abbie on 1/24/14.
 */
public class Item {
    private String IID = "";
    private String PID = "";
    private boolean inCsaThisWeek = false;
    private String category = "";
    private String productDesc = "";
    private String productUrl = "";
    private String productImageUrl = "";
    private String recipeUrl = "";
    private Integer unitsAvailable = 1;
    private String unitDesc = "";
    private Double unitPrice = 0.0;
    private Calendar deliveryDate;
    private String note = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Item() {
        setDeliveryDate(null);
    }

    public Item(String IID, String PID, boolean inCsaThisWeek, String category, String productDesc, String productUrl, String productImageUrl, String recipeUrl, Integer unitsAvailable,
                String unitDesc, Double unitPrice, Calendar deliveryDate, String note) {
        this.IID = IID;
        this.PID = PID;
        this.inCsaThisWeek = inCsaThisWeek;
        this.category = category;
        this.productDesc = productDesc;
        this.productUrl = productUrl;
        this.productImageUrl = productImageUrl;
        this.recipeUrl = recipeUrl;
        this.unitsAvailable = unitsAvailable;
        this.unitPrice = unitPrice;
        this.unitDesc = unitDesc;
        setDeliveryDate(deliveryDate);
        this.note = note;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public String getIID() {
        return IID;
    }

    public void setIID(String IID) {
        this.IID = IID;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public boolean isInCsaThisWeek() {
        return inCsaThisWeek;
    }

    public void setInCsaThisWeek(boolean inCsaThisWeek) {
        this.inCsaThisWeek = inCsaThisWeek;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public Integer getUnitsAvailable() {
        return unitsAvailable;
    }

    public void setUnitsAvailable(Integer unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDeliveryDate(Calendar deliveryDate) {
        if (deliveryDate == null) {
            deliveryDate = Calendar.getInstance();
            deliveryDate.set(Calendar.DAY_OF_YEAR, 1);
            deliveryDate.set(Calendar.YEAR, 1970);
        }
        this.deliveryDate = deliveryDate;
    }

    public Calendar getDeliveryDate() {
        return deliveryDate;
    }

    @Override
    public String toString() {
        dateFormat.setCalendar(deliveryDate);
        return "IID = " + IID + "; PID = " + PID +
                "; inCsaThisWeek = " + this.inCsaThisWeek + "; category = " +
                this.category + "; productDesc = " +
                this.productDesc + "; productUrl = " +
                this.productUrl + "; productImageUrl = " +
                this.productImageUrl + "; recipeUrl = " +
                this.recipeUrl + "; unitsAvailable = " +
                this.unitsAvailable + "; unitDesc = " +
                this.unitDesc + "; unitPrice = " +
                this.unitPrice + "; deliveryDate = " +
                dateFormat.format(deliveryDate.getTime()) + "; note =" +
                this.note;
    }

    public String toEmail(String growerAgreementId) {
        dateFormat.setCalendar(deliveryDate);
        return "\nIID = " + IID + "; \nPID = " + PID +
                "; \ncategory = " + this.category + "; \nproductDesc = " +
                this.productDesc + "; \nunitsAvailable = " +
                this.unitsAvailable + "; \nunitDesc = " +
                this.unitDesc + "; \ndeliveryDate = " +
                dateFormat.format(deliveryDate.getTime()) + "; \nnote = " +
                this.note + "; \ngrowerAgreementId = " +
                growerAgreementId + ";";
    }

    public String toCSV() {
        dateFormat.setCalendar(deliveryDate);
        return IID + "," + PID +
                "," + this.inCsaThisWeek + "," +
                this.category + "," +
                this.productDesc + "," +
                this.productUrl + "," +
                this.productImageUrl + "," +
                this.recipeUrl + "," +
                this.unitsAvailable + "," +
                this.unitDesc + "," +
                this.unitPrice + "," +
                dateFormat.format(deliveryDate.getTime()) + "," +
                this.note;
    }
}
