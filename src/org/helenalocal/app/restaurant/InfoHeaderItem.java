/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.restaurant;

import android.view.View;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.ListItem;

public class InfoHeaderItem extends ListItem  implements View.OnClickListener {
    public interface IInfoHeaderDismissedListener {
        public void onDismiss(InfoHeaderItem item);
    }

    private IInfoHeaderDismissedListener _listener;

    public InfoHeaderItem(IInfoHeaderDismissedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("IHeaderItemClearedLister is required");
        }

        _listener = listener;
    }

    @Override
    public int getViewId() {
        return R.layout.activity_info_header_dismissable;
    }

    @Override
    public void loadView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.msgTextView);
        textView.setText(R.string.restaurant_fragment_welcome);

        textView = (TextView) view.findViewById(R.id.clearItemTextView);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        _listener.onDismiss(this);
    }
}
