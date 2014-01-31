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
import org.helenalocal.utils.ViewServer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Product>> {

    private static final String Tag = "MainActivity";
    public static final String HUB_TYPE_KEY = "hubType";

    private static final int LoaderIdCSA = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Bundle b = new Bundle();
        b.putString(HUB_TYPE_KEY, Hub.Type.CSA.name());
        getSupportLoaderManager().initLoader(LoaderIdCSA, b, this);

        ViewServer.get(this).addWindow(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Hub.Type type = Hub.Type.valueOf(bundle.getString(HUB_TYPE_KEY));
        AsyncProductLoader loader = new AsyncProductLoader(this, type);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> listLoader, List<Product> products) {
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}