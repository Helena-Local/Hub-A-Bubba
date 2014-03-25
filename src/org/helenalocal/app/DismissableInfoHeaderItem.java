/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.view.View;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;

public class DismissableInfoHeaderItem extends ListItem implements View.OnClickListener{

    public interface IInfoHeaderDismissedListener {
        public void onDismiss(DismissableInfoHeaderItem item);
    }

    private IInfoHeaderDismissedListener _listener;
    private int _messageId;

    public DismissableInfoHeaderItem(IInfoHeaderDismissedListener listener, int messageId) {
        if (listener == null) {
            throw new IllegalArgumentException("IInfoHeaderDismissedListener is required");
        }

        _listener = listener;
        _messageId = messageId;
    }

    @Override
    public int getViewId() {
        return R.layout.activity_info_header_dismissable;
    }

    @Override
    public void loadView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.msgTextView);
        textView.setText(_messageId);

        textView = (TextView) view.findViewById(R.id.clearItemTextView);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        _listener.onDismiss(this);
    }
}
