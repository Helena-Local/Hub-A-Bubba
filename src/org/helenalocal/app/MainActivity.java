package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Product>> {

    private static final String Tag = "MainActivity";
    public static final String HUB_TYPE_KEY = "hubType";

    private static final int LoaderIdSales = 0;
    private static final int LoaderIdCSA = 1;

    private List<Product> _productList;
    private ProductItemAdapter _arrayAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        _productList = new ArrayList<Product>();
        _arrayAdapter = new ProductItemAdapter(this, R.layout.product_view, _productList);

        FragmentManager fm = getSupportFragmentManager();
        ProductListFragment productListFragment = (ProductListFragment) fm.findFragmentById(R.id.productListFragment);
        productListFragment.setListAdapter(_arrayAdapter);

        Bundle b = new Bundle();
        b.putString(HUB_TYPE_KEY, Hub.Type.SALES.name());
        getSupportLoaderManager().initLoader(LoaderIdSales, b, this);

        b = new Bundle();
        b.putString(HUB_TYPE_KEY, Hub.Type.CSA.name());
        getSupportLoaderManager().initLoader(LoaderIdCSA, b, this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.w(Tag, "onResume");
//        getSupportLoaderManager().restartLoader(LoaderIdSales, null, this);
//        getSupportLoaderManager().restartLoader(LoaderIdCSA, null, this);
    }



    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Hub.Type type = Hub.Type.valueOf(bundle.getString(HUB_TYPE_KEY));
        AsyncProductLoader loader = new AsyncProductLoader(this, type);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> listLoader, List<Product> products) {
        _productList.clear();
        _productList.addAll(products);
        _arrayAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}