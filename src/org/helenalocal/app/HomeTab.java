package org.helenalocal.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.helenalocal.Helena_Local_Hub.R;

public class HomeTab extends Fragment {

    private static final String Tag = "HomeTab";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_tab, container, false);
        return v;
    }
}