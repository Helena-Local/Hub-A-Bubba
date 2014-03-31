package org.montanafoodhub.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.Producer;

import java.util.ArrayList;
import java.util.List;


public class AsyncGrowerLoader extends AsyncTaskLoader<List<Producer>> {

    private final String Tag = "AsyncGrowerLoader";
    private List<Producer> _growerList;

    public AsyncGrowerLoader(Context context) {
        super(context);
    }

    @Override
    public List<Producer> loadInBackground() {
        return new ArrayList<Producer>(Hub.producerMap.values());
    }

    @Override
    public void deliverResult(List<Producer> data) {
        if (isReset()) {
            // ignore the results. Release resources as needed.
            return;
        }

        List<Producer> oldList = _growerList;
        _growerList = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (_growerList != null) {
            // we already have data so send it off.
            deliverResult(_growerList);
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
        _growerList = null;
    }
}
