package org.helenalocal.base.test;

import android.content.Context;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Product;
import org.helenalocal.base.IHub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class MockHub extends Hub implements IHub {
    @Override
    public List<Product> getProductList(Context context) {
        // CSV from spreadsheet
        // Product Description,Units Available,Unit Price,Units Desc,Notes

        ArrayList<Product> myProducts = new ArrayList<Product>();
        // String farmName, String farmEmailAddress, String category,
        // String productDesc, String growerAgreementId, Integer unitsAvailable,
        // String unitDesc, Calendar deliveryDate, Double unitPrice, String note

        myProducts.add(new Product("Kevin's Farm","foo@home.com", "http://my.farm.com", "Produce", "Onion","N/A",20,"lbs", Calendar.getInstance(),10.01,"note 1"));
        myProducts.add(new Product("Kevin's Farm","foo@home.com", "http://my.farm.com", "Produce", "Beet","2014-13",29,"lbs", Calendar.getInstance(),10.02,"note 2"));
        myProducts.add(new Product("Kevin's Farm","foo@home.com", "http://my.farm.com", "Produce", "Potato","N/A",28,"lbs", Calendar.getInstance(),10.03,"note 3"));
        myProducts.add(new Product("Kevin's Farm","foo@home.com", "http://my.farm.com", "Produce", "Lettuce","N/A",27,"lbs", Calendar.getInstance(),10.04,"note 4"));
        myProducts.add(new Product("Kevin's Farm","foo@home.com", "http://my.farm.com", "Produce", "Chard","N/A",26,"lbs", Calendar.getInstance(),10.05,"note 5"));

        return myProducts;
    }

    @Override
    public void setProduct(Context context, Product product) throws IOException {

    }
}
