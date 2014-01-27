package org.helenalocal.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Product;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubFactory;

import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        Log.w(Hub.FRONTEND,"**** Starting fetch test for SALES....");
        try {
            List<Product> myProducts = HubFactory.buildHubFetch(Hub.SALES).getProductList(context);
            for (int i = 0; i < myProducts.size(); i++) {
                Product product = myProducts.get(i);
                // Shane @ this point you can display each product from here!
                Log.w(Hub.FRONTEND, product.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(Hub.FRONTEND,"**** Ending fetch test for SALES....");

        // draw the display!
        setContentView(R.layout.main);
    }
}
