/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app;

import android.view.View;
import android.widget.TextView;
import org.montanafoodhub.Helena_Hub.R;

public class InfoHeaderItem extends ListItem implements View.OnClickListener{

    public interface IInfoHeaderDismissedListener {
        public void onDismiss(InfoHeaderItem item);
    }

    public void setOnDismissedListener(IInfoHeaderDismissedListener listener) {
        _listener = listener;
    }

    private IInfoHeaderDismissedListener _listener;
    private int _messageId;

    public InfoHeaderItem(int messageId) {
        _messageId = messageId;
    }

    @Override
    public int getViewId() {
        return R.layout.activity_info_header;
    }

    @Override
    public void loadView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.msgTextView);
        textView.setText(_messageId);

        if (_listener == null) {
            View dismissView = view.findViewById(R.id.dismissContainer);
            dismissView.setVisibility(View.GONE);
        }
        else {
            textView = (TextView) view.findViewById(R.id.clearItemTextView);
            textView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        _listener.onDismiss(this);
    }
}
