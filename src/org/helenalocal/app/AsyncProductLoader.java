package org.helenalocal.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;
import org.helenalocal.base.get.BuyerHub;
import org.helenalocal.base.get.ItemHub;
import org.helenalocal.base.get.OrderHub;
import org.helenalocal.base.get.ProducerHub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncProductLoader extends AsyncTaskLoader<List<Item>> {
    private static final String Tag = "AsyncProductLoader";
    private List<Item> _itemList;

    public AsyncProductLoader(Context context) {
        super(context);
    }

    @Override
    public List<Item> loadInBackground() {
            return new ArrayList<Item>(Hub.itemMap.values());
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
