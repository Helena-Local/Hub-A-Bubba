package org.helenalocal.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import org.helenalocal.base.Item;
import org.helenalocal.base.get.ItemHub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncProductLoader extends AsyncTaskLoader<List<Item>> {
    private final String logTag;
    private List<Item> _itemList;

    public AsyncProductLoader(Context context) {
        super(context);
        logTag = "AsyncProductLoader - Item ";
    }

    @Override
    public List<Item> loadInBackground() {

        Log.w(logTag, "**** Starting fetch for items....");
        try {
            ItemHub itemHub = new ItemHub();
            HashMap<String, Item> itemMap = itemHub.getItemMap(getContext());
            _itemList = new ArrayList<Item>(itemMap.values());

            Log.w(logTag, "Data last refreshed on " + itemHub.getLastRefreshTS().getTime());

            for (Item p : _itemList) {
                Log.w(logTag, p.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(logTag,"**** Ending fetch for items");

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
