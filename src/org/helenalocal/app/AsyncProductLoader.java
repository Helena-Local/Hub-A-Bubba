package org.helenalocal.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubFactory;
import org.helenalocal.base.IHub;
import org.helenalocal.base.Product;

import java.util.List;

public class AsyncProductLoader extends AsyncTaskLoader<List<Product>> {
    private final String logTag;
    private List<Product> _productList;
    private Hub.Type _type;

    public AsyncProductLoader(Context context, Hub.Type type) {
        super(context);

        _type = type;

        logTag = new StringBuilder("AsyncProductLoader ").append(_type).toString();
    }

    @Override
    public List<Product> loadInBackground() {

        Log.w(logTag, String.format("**** Starting fetch for %s....", _type));
        try {
            IHub myHub = HubFactory.buildHubFetch(_type);
            _productList = myHub.getProductList(getContext());

            Log.w(logTag, "Data last refreshed on " + myHub.getLastRefreshTS().getTime());

            for (Product p : _productList) {
                Log.w(logTag, p.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(logTag, String.format("**** Ending fetch for %s....", _type));

        return _productList;
    }

    @Override
    public void deliverResult(List<Product> data) {
        if (isReset()) {
            // ignore the results. Release resources as needed.
            return;
        }

        List<Product> oldList = _productList;
        _productList = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (_productList != null) {
            // we already have data so send it off.
            deliverResult(_productList);
        }
        else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        _productList = null;
    }
}
