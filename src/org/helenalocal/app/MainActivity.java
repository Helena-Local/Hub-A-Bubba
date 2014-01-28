package org.helenalocal.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubFactory;
import org.helenalocal.base.IHub;
import org.helenalocal.base.Product;

import java.util.List;

public class MainActivity extends FragmentActivity {

    private List<Product> _productList;
    private ProductItemAdapter _arrayAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Context context = getApplicationContext();

        Log.w(Hub.FRONTEND, "**** Starting fetch test for UNIT....");
        try {
            IHub myHub = HubFactory.buildHubFetch(Hub.SALES);
            _productList = myHub.getProductList(context);
            Log.w(Hub.FRONTEND,"Data last refreshed on : " + myHub.getLastRefreshTS().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(Hub.FRONTEND, "**** Ending fetch test for UNIT....");

        _arrayAdapter = new ProductItemAdapter(this, R.layout.product_view, _productList);

        FragmentManager fm = getSupportFragmentManager();
        ProductListFragment productListFragment = (ProductListFragment) fm.findFragmentById(R.id.productListFragment);
        productListFragment.setListAdapter(_arrayAdapter);





//        Log.w(Hub.FRONTEND, "**** Starting fetch test for CSA....");
//        try {
//            IHub myHub = HubFactory.buildHubFetch(Hub.CSA);
//            List<Product> myProducts = myHub.getProductList(context);
//            for (int i = 0; i < myProducts.size(); i++) {
//                Product product = myProducts.get(i);
//                // Shane @ this point you can display each product from here!
//                Log.w(Hub.FRONTEND, product.toString());
//            }
//            Log.w(Hub.FRONTEND,"Data last refreshed on : " + myHub.getLastRefreshTS().getTime());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.w(Hub.FRONTEND, "**** Ending fetch test for CSA....");
    }
}