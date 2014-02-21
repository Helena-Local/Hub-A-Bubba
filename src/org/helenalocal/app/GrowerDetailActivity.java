/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Certification;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Producer;
import org.helenalocal.utils.ImageCache;

import java.net.URLEncoder;

public class GrowerDetailActivity extends Activity {

    public static final String EXTRA_PRODUCER_ID = "org.helenalocal.extra.producer_id";

    private static final String Tag = "GrowerDetailActivity";

    private Producer _producer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_detail_activity);

        String producerId = getIntent().getStringExtra(EXTRA_PRODUCER_ID);
        _producer = Hub.producerMap.get(producerId);

        // image
        ImageCache imageCache = ((HubApplication)getApplication()).getImageCache();
        ImageView imageView = (ImageView)findViewById(R.id.producerImageView);
        imageCache.loadImage(imageView, _producer.getIconUrl(), R.drawable.default_producer);

        // name
        TextView textView = (TextView) findViewById(R.id.nameTextView);
        textView.setText(_producer.getName());

        // address
        textView = (TextView)findViewById(R.id.addressTextView);
        textView.setText(_producer.getLocation());

        // quote
        textView = (TextView)findViewById(R.id.quoteTextView);
        if (_producer.getQuote().isEmpty() == false) {
            textView.setText(String.format("\"%s\"", _producer.getQuote()));
        }
        else {
            textView.setVisibility(View.GONE);
        }

        // photo
        imageView = (ImageView)findViewById(R.id.photoImageView);
        if (_producer.getPhotoUrl().isEmpty() == true) {
            imageView.setVisibility(View.GONE);
        }
        else {
            imageCache.loadImage(imageView, _producer.getPhotoUrl(), R.drawable.ic_contact_picture);
        }

        loadCertifications();
    }

    private void loadCertifications() {

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.certificationLayout);
        linearLayout.removeAllViews();

        ImageCache imageCache = ((HubApplication)getApplication()).getImageCache();
        CertItemClickListener clickListener = new CertItemClickListener();

        for (Certification cert : _producer.getCertifications()) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.certification, null);
            relativeLayout.setOnClickListener(clickListener);
            relativeLayout.setTag(cert);

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.imageView);
            imageCache.loadImage(imageView, cert.getIconUrl(), R.drawable.default_certification);

            TextView textView = (TextView)relativeLayout.findViewById(R.id.textView);
            textView.setText(cert.getDisplayName());

            linearLayout.addView(relativeLayout);
        }
    }

    public void onClickEmail(View view) {
        Log.w(Tag, "onClickEmail()");
        if (_producer.getContactEmail().contains("@")) {
            Log.w(Tag, "got an email");
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + _producer.getContactEmail() + "," + HubInit.getHubEmailTo()));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hub Request For: " + _producer.getName());
            emailIntent.putExtra(Intent.EXTRA_TEXT, "<grower love here...>");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else {
            Log.w(Tag, "got a phone");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + _producer.getContactEmail()));
            startActivity(intent);
        }
    }

    public void onClickMap(View view) {
        // todo - verify that the maps application is installed (apparently it is not on a kindle)

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String data = String.format("geo:0,0?q=%s", URLEncoder.encode(_producer.getLocation(), "UTF-8"));

            intent.setData(Uri.parse(data));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    public void onClickUrl(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(_producer.getWebsiteUrl()));
        startActivity(intent);
    }

    private class CertItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Certification cert = (Certification)v.getTag();

            if (cert.getWebsiteUrl().isEmpty() == false) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(cert.getWebsiteUrl()));
                startActivity(intent);
            }
        }
    }
}