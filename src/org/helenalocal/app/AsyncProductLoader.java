package org.helenalocal.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubFactory;
import org.helenalocal.base.IHub;
import org.helenalocal.base.Item;

import java.util.List;

public class AsyncProductLoader extends AsyncTaskLoader<List<Item>> {
    private final String logTag;
    private List<Item> _itemList;
    private Hub.HubType _Hub_type;

    public AsyncProductLoader(Context context, Hub.HubType hubType) {
        super(context);

        _Hub_type = hubType;

        logTag = new StringBuilder("AsyncProductLoader ").append(_Hub_type).toString();
    }

    @Override
    public List<Item> loadInBackground() {

        Log.w(logTag, String.format("**** Starting fetch for %s....", _Hub_type));
        try {
            IHub myHub = HubFactory.buildHubFetch(_Hub_type);
            _itemList = myHub.getProduct(getContext());

            Log.w(logTag, "Data last refreshed on " + myHub.getLastRefreshTS().getTime());

            for (Item p : _itemList) {
                Log.w(logTag, p.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(logTag, String.format("**** Ending fetch for %s....", _Hub_type));

        return _itemList;
    }

    @Override
    public void deliverResult(List<Item> data) {
        if (isReset()) {
            // ignore the results. Release resources as needed.
            return;
        }

        List<Item> oldList = _itemList;
        _itemList = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (_itemList != null) {
            // we already have data so send it off.
            deliverResult(_itemList);
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
        _itemList = null;
    }
}
