package org.helenalocal.base;

/**
 * Created by abbie on 1/24/14.
 */
public class Product {
    private String category = "";
    private String productDesc = "";
    private Integer unitsAvailable = 1;
    private Double unitPrice = 0.0;
    private String unitDesc = "";
    private String note = "";

    public Product() {}

    public Product(String productDesc, Integer unitsAvailable, Double unitPrice, String unitDesc, String note) {
        this.productDesc = productDesc;
        this.unitsAvailable = unitsAvailable;
        this.unitPrice = unitPrice;
        this.unitDesc = unitDesc;
        this.note = note;
    }

    // CSV from spreadsheet
    //Product Description,Units Available,Unit Price,Units Desc,Notes

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

    public String getNotes() {
        return note;
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
        return "category = " + category + "; productDesc = " + productDesc + "; unitsAvailable = " + unitsAvailable + "; unitPrice = " + unitPrice + "; unitDesc = " + unitDesc + "; note = " + note;
    }
}
