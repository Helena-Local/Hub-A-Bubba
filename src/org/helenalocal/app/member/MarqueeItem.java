/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.member;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.utils.ActivityUtils;

public class MarqueeItem extends ListItem implements View.OnClickListener {

    private static final String LogTag = "MarqueeItem";
    private Context _context;

    public MarqueeItem(Context context) {
        _context = context;
    }

    @Override
    public int getViewId() {
        return R.layout.product_share_listview_marquee;
    }

    @Override
    public void loadView(View view) {

        // info click listener
        TextView textView = (TextView) view.findViewById(R.id.infoTextView);
        textView.setOnClickListener(this);

        // buy click listener
        textView = (TextView) view.findViewById(R.id.buyTextView);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.infoTextView) {
            onClickInfo();
        }
        else if (v.getId() == R.id.buyTextView) {
            onClickBuy();
        }
    }

    private void onClickInfo() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://sites.google.com/site/helenalocalcsa2/about"));
        ActivityUtils.startImplicitActivity(_context, intent, R.string.no_web_browser_application, LogTag);
    }

    private void onClickBuy() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://buy.helenalocal.org"));
        ActivityUtils.startImplicitActivity(_context, intent, R.string.no_web_browser_application, LogTag);
    }
}
