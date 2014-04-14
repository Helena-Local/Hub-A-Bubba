/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.csa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.app.ListItem;
import org.montanafoodhub.app.controls.ActionBar;
import org.montanafoodhub.app.utils.ActivityUtils;
import org.montanafoodhub.base.Buyer;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.Order;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;

public class MarqueeItem extends ListItem implements ActionBar.ActionBarClickListener {

    private static final String LogTag = MarqueeItem.class.getSimpleName();
    private final Buyer _buyer;
    private final Context _context;

    public MarqueeItem(Context context, Buyer buyer) {
        _context = context;
        _buyer = buyer;
    }

    @Override
    public int getViewId() {
        return R.layout.csa_detail_listview_marquee;
    }

    @Override
    public void loadView(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
        String csaDateStr = "";

        for (int i=0; i < Hub.orderArr.size(); i++) {
            Order o = Hub.orderArr.get(i);
            if (o.getBuyerID().equals(_buyer.getBID())) {
                csaDateStr = dateFormat.format(o.getDate().getTime());
                break;
            }
        }
        TextView textHeader = (TextView) view.findViewById(R.id.textViewHeader);
        textHeader.setText(String.format(_context.getResources().getString(R.string.csa_header_date_text), csaDateStr));

        ActionBar actionBar = (ActionBar) view.findViewById(R.id.actionBar);
        actionBar.setOnClickActionListener(this);
    }

    @Override
    public void onActionClicked(ActionBar.Action action) {
        if (action == ActionBar.Action.Left) {
            onClickCall();
        }
        else if (action == ActionBar.Action.Center) {
            onClickMap();
        }
        else if (action == ActionBar.Action.Right) {
            onClickUrl();
        }
    }

    private void onClickCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + _buyer.getPhone()));
        ActivityUtils.startImplicitActivity(_context, intent, R.string.no_phone_application, LogTag);
    }

    private void onClickMap() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String data = String.format("geo:0,0?q=%s", URLEncoder.encode(_buyer.getLocation(), "UTF-8"));
            intent.setData(Uri.parse(data));
            ActivityUtils.startImplicitActivity(_context, intent, R.string.no_maps_application, LogTag);
        } catch (Exception e) {
        }
    }

    private void onClickUrl() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(_buyer.getWebsiteUrl()));
        ActivityUtils.startImplicitActivity(_context, intent, R.string.no_web_browser_application, LogTag);
    }
}
