package org.helenalocal.base.test;

import org.helenalocal.base.Product;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubFactory;

import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("**** Starting fetch test for MOCK....");
        try {
            List<Product> myProducts = HubFactory.buildHubFetch(Hub.Type.MOCK).getProductList(null);
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
