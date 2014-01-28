package org.helenalocal.hub.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.ArrayAdapter;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Product;
import org.helenalocal.base.fetch.HubFetch;
import org.helenalocal.base.fetch.HubFetchFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private List<Product> _productList;
    private ProductItemAdapter _arrayAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Context context = getApplicationContext();

        System.out.println("**** Starting fetch test for SALES....");
        try {
            _productList = HubFetchFactory.buildHubFetch(HubFetch.SALES).getProductList(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("**** Ending fetch test for SALES....");

        _arrayAdapter = new ProductItemAdapter(this, R.layout.product_view, _productList);

        FragmentManager fm = getSupportFragmentManager();
        ProductListFragment productListFragment = (ProductListFragment) fm.findFragmentById(R.id.productListFragment);
        productListFragment.setListAdapter(_arrayAdapter);

    }
}