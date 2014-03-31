package org.montanafoodhub.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.Item;

import java.util.ArrayList;
import java.util.List;

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
