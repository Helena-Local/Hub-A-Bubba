package org.helenalocal.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by abbie on 1/24/14.
 */
public class Item {
    private String IID = "";
    private Producer producer;
    private boolean inCsaThisWeek = false;
    private String category = "";
    private String productDesc = "";
    private String productUrl = "";
    private String productImageUrl = "";
    private Integer unitsAvailable = 1;
    private Double unitPrice = 0.0;
    private String unitDesc = "";
    private String note = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Item() {
        setDeliveryDate(null);
    }

    public Item(boolean inCSA, String category, String productDesc, String productUrl, String growerAgreementId, Integer unitsAvailable,
                String unitDesc, Calendar deliveryDate, Double unitPrice, String note) {
        setDeliveryDate(null);
        this.inCSA = inCSA;
        this.category = category;
        this.productDesc = productDesc;
        this.productUrl = productUrl;
        this.growerAgreementId = growerAgreementId;
        this.unitsAvailable = unitsAvailable;
        this.unitDesc = unitDesc;
        this.deliveryDate = deliveryDate;
        this.unitPrice = unitPrice;
        this.note = note;
    }

    // CSV from spreadsheet
    //Timestamp,Producer Name,Producer/Response Email Address,Item Category,Item Description,Grower Agreement ID,Unit Description,Units Available,Delivery Date,Item Notes

    public boolean isInCSA() {
        return inCSA;
    }

    public String getProductImageUrl() {
        return productImageUrl;
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

    public String getProductUrl() {
        return productUrl;
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

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public void setDeliveryDate(Calendar deliveryDate) {
        if (deliveryDate == null) {
            deliveryDate = Calendar.getInstance();
            deliveryDate.set(Calendar.DAY_OF_YEAR, 1);
            deliveryDate.set(Calendar.YEAR, 1970);
        }
        this.deliveryDate = deliveryDate;
    }

    public void setInCSA(boolean inCSA) {
        this.inCSA = inCSA;
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

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    @Override
    public String toString() {
        dateFormat.setCalendar(deliveryDate);
        return "pid = " + producer.getPID() + "; pName = " +
                this.producer.getName() + "; pContactEmail = " +
                this.producer.getContactEmail() + "; pPhotoUrl = " +
                this.producer.getPhotoUrl() + "; isInCSA = " +
                this.isInCSA() + "; category = " +
                this.category + "; productDesc = " +
                this.productDesc + "; productUrl = " +
                this.productUrl + "; growerAgreementId = " +
                this.growerAgreementId + "; unitsAvailable = " +
                this.unitsAvailable + "; unitDesc = " +
                this.unitDesc + "; deliveryDate = " +
                dateFormat.format(deliveryDate.getTime()) + "; unitPrice = " +
                this.unitPrice + "; note =" +
                this.note;
    }

    public String toEmail() {
        dateFormat.setCalendar(deliveryDate);
        return "farmName = " + producer.getFarmName() + "; \nfarmEmailAddress = " +
                producer.getFarmEmailAddress() + "; \nfarmUrl = " +
                producer.getFarmUrl() + "; \ncategory = " +
                this.category + "; \nproductDesc = " +
                this.productDesc + "; \nproductUrl = " +
                this.productUrl + "; \ngrowerAgreementId = " +
                this.growerAgreementId + "; \nunitsAvailable = " +
                this.unitsAvailable + "; \nunitDesc = " +
                this.unitDesc + "; \ndeliveryDate = " +
                dateFormat.format(deliveryDate.getTime()) + "; \nunitPrice = " +
                this.unitPrice + "; \nnote =" +
                this.note;
    }

    public String toCSV() {
        dateFormat.setCalendar(deliveryDate);
        return producer.getFarmName() + "," +
                producer.getFarmEmailAddress() + "," +
                this.producer.getFarmUrl() + "," +
                this.category + "," +
                this.productDesc + "," +
                this.productUrl + "," +
                this.growerAgreementId + "," +
                this.unitsAvailable + "," +
                this.unitDesc + "," +
                dateFormat.format(deliveryDate.getTime()) + "," +
                this.unitPrice + "," +
                this.note;
    }
}
