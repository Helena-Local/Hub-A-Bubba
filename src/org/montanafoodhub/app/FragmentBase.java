/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.HubInit;

abstract public class FragmentBase extends Fragment {

    private HubInit.HubType _hubType;
    private BroadcastReceiver _broadcastReceiver;
    private IntentFilter _intentFilter;
    private boolean _receiverInitialized = false;
    private boolean _receiverRegistered = false;

    abstract public int getTitleId();

    @Override
    public void onResume() {
        super.onResume();

        if (_receiverInitialized == true) {
            registerReceiver();
            onRefresh();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (_receiverInitialized == true) {
            unregisterReceiver();
        }
    }

    protected void onRefresh() {
    }


    protected void initializeReceiver(HubInit.HubType type) {
        _hubType = type;

        _intentFilter = new IntentFilter(Hub.HUB_DATA_REFRESH);
        _intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        _broadcastReceiver = getBroadcastReceiver();
        _receiverInitialized = true;
    }

    private BroadcastReceiver getBroadcastReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                HubInit.HubType type = (HubInit.HubType)intent.getSerializableExtra(Hub.EXTRA_HUB_TYPE);

                // due to the async nature of the broadcast message it is possible to receive this after we have unregistered
                if ((type == _hubType) && (_receiverRegistered == true)) {
                        onRefresh();
                }
            }
        };
    }

    private void registerReceiver() {
        _receiverRegistered = true;
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(_broadcastReceiver, _intentFilter);
    }

    private void unregisterReceiver() {
        _receiverRegistered = false;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(_broadcastReceiver);
    }
}