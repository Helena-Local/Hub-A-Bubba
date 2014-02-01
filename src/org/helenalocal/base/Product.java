package org.helenalocal.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by abbie on 1/24/14.
 */
public class Product {
    private String farmName = "";
    private String farmEmailAddress = "";
    private String farmUrl = "";
    private String category = "";
    private String productDesc = "";
    private String growerAgreementId = "";

    private Integer unitsAvailable = 1;
    private String unitDesc = "";
    private Calendar deliveryDate;
    private Double unitPrice = 0.0;
    private String note = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Product() {
        setDeliveryDate(null);
    }

    public Product(String farmName, String farmEmailAddress, String farmUrl, String category, String productDesc, String growerAgreementId, Integer unitsAvailable,
                   String unitDesc, Calendar deliveryDate, Double unitPrice, String note) {
        setDeliveryDate(null);
        this.farmName = farmName;
        this.farmEmailAddress = farmEmailAddress;
        this.farmUrl = farmUrl;
        this.category = category;
        this.productDesc = productDesc;
        this.growerAgreementId = growerAgreementId;
        this.unitsAvailable = unitsAvailable;
        this.unitDesc = unitDesc;
        this.deliveryDate = deliveryDate;
        this.unitPrice = unitPrice;
        this.note = note;
    }

    // CSV from spreadsheet
    //Timestamp,Farm Name,Farm/Response Email Address,Product Category,Product Description,Grower Agreement ID,Unit Description,Units Available,Delivery Date,Product Notes

    public String getFarmName() {
        return farmName;
    }

    public String getFarmEmailAddress() {
        return farmEmailAddress;
    }

    public String getFarmUrl() {
        return farmUrl;
    }

    public String getGrowerAgreementId() {
        return growerAgreementId;
    }

    public Calendar getDeliveryDate() {
        return deliveryDate;
    }

    public String getNote() {
        return note;
    }

    public void setGrowerAgreementId(String growerAgreementId) {
        this.growerAgreementId = growerAgreementId;
    }

    public String getCategory() {
        return category;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public Integer getUnitsAvailable() {
        return unitsAvailable;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setFarmEmailAddress(String farmEmailAddress) {
        this.farmEmailAddress = farmEmailAddress;
    }

    public void setFarmUrl(String farmUrl) {
        this.farmUrl = farmUrl;
    }

    public void setDeliveryDate(Calendar deliveryDate) {
        if (deliveryDate == null) {
            deliveryDate = Calendar.getInstance();
            deliveryDate.set(Calendar.DAY_OF_YEAR, 1);
            deliveryDate.set(Calendar.YEAR, 1970);
        }
        this.deliveryDate = deliveryDate;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setUnitsAvailable(Integer unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        dateFormat.setCalendar(deliveryDate);
        return " farmName = " + this.farmName + "; farmEmailAddress = " +
                this.farmEmailAddress + "; farmUrl = " +
                this.farmUrl + "; category = " +
                this.category + "; productDesc = " +
                this.productDesc + "; growerAgreementId = " +
                this.growerAgreementId + "; unitsAvailable = " +
                this.unitsAvailable + "; unitDesc = " +
                this.unitDesc + "; deliveryDate = " +
                dateFormat.format(deliveryDate.getTime()) + "; unitPrice = " +
                this.unitPrice + "; note =" +
                this.note;
    }

    public String toCSV() {
        dateFormat.setCalendar(deliveryDate);
        return this.farmName + "," +
                this.farmEmailAddress + "," +
                this.farmUrl + "," +
                this.category + "," +
                this.productDesc + "," +
                this.growerAgreementId + "," +
                this.unitsAvailable + "," +
                this.unitDesc + "," +
                dateFormat.format(deliveryDate.getTime()) + "," +
                this.unitPrice + "," +
                this.note;
    }
}
