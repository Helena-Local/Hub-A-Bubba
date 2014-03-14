/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ProducerDetailFragment extends Fragment implements View.OnClickListener {

    private static final String LogTag = "ProducerDetailFragment";
    private Producer _producer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.producer_detail_fragment, container, false);

        Bundle args = getArguments();
        String producerId = args.getString(ProducerDetailActivity.EXTRA_PRODUCER_ID);
        _producer = Hub.producerMap.get(producerId);

        // image
        ImageCache imageCache = ((HubApplication) getActivity().getApplication()).getImageCache();
        ImageView imageView = (ImageView) view.findViewById(R.id.producerImageView);
        imageCache.loadImage(imageView, _producer.getIconUrl(), R.drawable.default_producer);

        // name
        TextView textView = (TextView) view.findViewById(R.id.nameTextView);
        textView.setText(_producer.getName());

        // address
        textView = (TextView) view.findViewById(R.id.addressTextView);
        textView.setText(_producer.getLocation());

        // phone / email action - default is email.
        if (hasEmail(_producer) == false) {
            imageView = (ImageView) view.findViewById(R.id.contactImageView);
            imageView.setImageResource(R.drawable.ic_action_call);

            textView = (TextView) view.findViewById(R.id.contactTextView);
            textView.setText(getResources().getText(R.string.producer_call_action));
        }


        // contact click listener
        textView = (TextView) view.findViewById(R.id.contactTextView);
        textView.setOnClickListener(this);

        // map click listener
        textView = (TextView) view.findViewById(R.id.mapTextView);
        textView.setOnClickListener(this);

        // url click listener
        textView = (TextView) view.findViewById(R.id.urlTextView);
        textView.setOnClickListener(this);


        // quote
        textView = (TextView) view.findViewById(R.id.quoteTextView);
        if (_producer.getQuote().isEmpty() == false) {
            textView.setText(String.format("\"%s\"", _producer.getQuote()));
        }
        else {
            textView.setVisibility(View.GONE);
        }

        // photo
        imageView = (ImageView) view.findViewById(R.id.photoImageView);
        if (_producer.getPhotoUrl().isEmpty() == true) {
            imageView.setVisibility(View.GONE);
        }
        else {
            imageCache.loadImage(imageView, _producer.getPhotoUrl(), R.drawable.ic_contact_picture);
        }

        loadCertifications(view);

        return view;
    }

    private void loadCertifications(View view) {

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.certificationLayout);
        linearLayout.removeAllViews();

        ImageCache imageCache = ((HubApplication) getActivity().getApplication()).getImageCache();

        for (Certification cert : _producer.getCertifications()) {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.certification, null);
            relativeLayout.setOnClickListener(this);
            relativeLayout.setTag(cert);

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.imageView);
            imageCache.loadImage(imageView, cert.getIconUrl(), R.drawable.default_certification);

            TextView textView = (TextView)relativeLayout.findViewById(R.id.textView);
            textView.setText(cert.getDisplayName());

            linearLayout.addView(relativeLayout);
        }
    }

    private boolean hasEmail(Producer producer) {
        return _producer.getContactEmail().contains("@");
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.contactTextView) {
            onClickContact();
        }
        else if (v.getId() == R.id.mapTextView) {
            onClickMap();
        }
        else if (v.getId() == R.id.urlTextView) {
            onClickUrl();
        }
        else {
            Object tag = v.getTag();
            if (tag instanceof Certification) {
                onClickCertification((Certification) tag);
            }
        }
    }

    private void onClickContact() {
        if (hasEmail(_producer)) {
            Log.w(LogTag, "got an email");
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + _producer.getContactEmail() + "," + HubInit.getHubEmailTo()));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hub Request For: " + _producer.getName());
            emailIntent.putExtra(Intent.EXTRA_TEXT, "<grower love here...>");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } else {
            Log.w(LogTag, "got a phone");
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + _producer.getContactEmail()));
            startActivity(intent);
        }
    }

    private void onClickMap() {
        // todo - verify that the maps application is installed (apparently it is not on a kindle)

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String data = String.format("geo:0,0?q=%s", URLEncoder.encode(_producer.getLocation(), "UTF-8"));

            intent.setData(Uri.parse(data));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    private void onClickUrl() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(_producer.getWebsiteUrl()));
        startActivity(intent);
    }

    private void onClickCertification(Certification cert) {
        if (cert.getWebsiteUrl().isEmpty() == false) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(cert.getWebsiteUrl()));
            startActivity(intent);
        }
    }
}