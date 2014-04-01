/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.csa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import org.montanafoodhub.Helena_Local_Hub.R;
import org.montanafoodhub.app.ListItem;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.HubInit;
import org.montanafoodhub.base.Order;
import org.montanafoodhub.app.utils.ActivityUtils;

import java.text.SimpleDateFormat;

public class MarqueeItem extends ListItem implements View.OnClickListener {

    private static final String LogTag = "MarqueeItem";
    private Context _context;

    public MarqueeItem(Context context) {
        _context = context;
    }

    @Override
    public int getViewId() {
        return R.layout.csa_detail_listview_marquee;
    }

    @Override
    public void loadView(View view) {
        // TODO not sure this goes here...
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
        String csaDateStr = "";

        for (int i=0; i < Hub.orderArr.size(); i++) {
            Order o = Hub.orderArr.get(i);
            if (o.getBuyerID().equals(HubInit.HELENA_LOCAL_BUYER_ID)) {
                csaDateStr = dateFormat.format(o.getDate().getTime());
                break;
            }
        }
        TextView textHeader = (TextView) view.findViewById(R.id.textViewHeader);
        textHeader.setText("Shares for the week of " + csaDateStr);

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
        intent.setData(Uri.parse(Hub.buyerMap.get(HubInit.HELENA_LOCAL_BUYER_ID).getContactEmail()));
        ActivityUtils.startImplicitActivity(_context, intent, R.string.no_web_browser_application, LogTag);
    }

    private void onClickBuy() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Hub.buyerMap.get(HubInit.HELENA_LOCAL_BUYER_ID).getWebsiteUrl()));
        ActivityUtils.startImplicitActivity(_context, intent, R.string.no_web_browser_application, LogTag);
    }
}
