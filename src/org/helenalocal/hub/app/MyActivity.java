package org.helenalocal.hub.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Product;
import org.helenalocal.base.fetch.HubFetch;
import org.helenalocal.base.fetch.HubFetchFactory;

import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();

        System.out.println("**** Starting fetch test for SALES....");
        try {
            List<Product> myProducts = HubFetchFactory.buildHubFetch(HubFetch.SALES).getProductList(context);
            for (int i = 0; i < myProducts.size(); i++) {
                Product product = myProducts.get(i);
                // Shane @ this point you can display each product from here!
                System.out.println(product.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("**** Ending fetch test for SALES....");

        // draw the display!
        setContentView(R.layout.main);
    }
}
