package org.helenalocal.base.fetch.test;

import org.helenalocal.base.Product;
import org.helenalocal.base.fetch.HubFetch;
import org.helenalocal.base.fetch.HubFetchFactory;

import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("**** Starting fetch test for MOCK....");
        try {
            List<Product> myProducts = HubFetchFactory.buildHubFetch(HubFetch.MOCK).getProductList(null);
            for (int i = 0; i < myProducts.size(); i++) {
                Product product = myProducts.get(i);
                System.out.println(product.toString());
            }
        } catch (Exception e) {
            System.out.println("No MOCK products found....");
        }
        System.out.println("**** Ending fetch test for MOCK....");
    }
}
