package org.helenalocal.base.test;

import android.content.Context;
import org.helenalocal.base.Product;
import org.helenalocal.base.IHub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class MockHub implements IHub {
    @Override
    public List<Product> getProductList(Context context) {
        // CSV from spreadsheet
        // Product Description,Units Available,Unit Price,Units Desc,Notes

        ArrayList<Product> myProducts = new ArrayList<Product>();
        myProducts.add(new Product("Onion",20,10.01,"25 lb case",""));

        myProducts.add(new Product("Beet",29,10.02,""," call "));
        myProducts.add(new Product("Carrot",28,10.03, "20 lb case",""));
        myProducts.add(new Product("Potato",27,10.04, "25 lb case",""));
        myProducts.add(new Product("Lettuce",26,10.05, "10 units / case",""));
        myProducts.add(new Product("Kale",25,10.06, "25 lb case",""));
        myProducts.add(new Product("Chard",24,100.07, "25 lb case",""));

        return myProducts;
    }
}
