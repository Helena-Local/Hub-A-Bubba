package org.helenalocal.app;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import org.helenalocal.base.Producer;
import org.helenalocal.base.get.ProducerHub;

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

        Log.w(Tag, "**** Starting fetch for items....");

        try {
            ProducerHub producerHub = new ProducerHub();
            _growerList = new ArrayList<Producer>(producerHub.getProducerMap(getContext()).values());

            Log.w(Tag, "Data last refreshed on " + producerHub.getLastRefreshTS().getTime());

            for (Producer p : _growerList) {
                Log.w(Tag, p.toString());
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w(Tag,"**** Ending fetch for items");

        return _growerList;
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
