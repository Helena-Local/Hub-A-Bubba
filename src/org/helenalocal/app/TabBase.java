/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;

public class TabBase extends Fragment {

    private HubInit.HubType _hubType;
    private BroadcastReceiver _broadcastReceiver;
    private IntentFilter _intentFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
        onRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    protected void onRefresh() {
    }


    protected void initializeReceiver(HubInit.HubType type) {
        _hubType = type;

        _intentFilter = new IntentFilter(Hub.HUB_DATA_REFRESH);
        _intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                HubInit.HubType type = (HubInit.HubType)intent.getSerializableExtra(Hub.EXTRA_HUB_TYPE);
                if (type == _hubType) {
                    onRefresh();
                }
            }
        };
    }

    private void registerReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(_broadcastReceiver, _intentFilter);
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(_broadcastReceiver);
    }
}