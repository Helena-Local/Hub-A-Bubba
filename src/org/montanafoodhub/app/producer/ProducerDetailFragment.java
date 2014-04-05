/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.producer;

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
import org.montanafoodhub.Helena_Hub.R;
import org.montanafoodhub.app.HubApplication;
import org.montanafoodhub.app.utils.ActivityUtils;
import org.montanafoodhub.app.utils.ImageCache;
import org.montanafoodhub.base.Certification;
import org.montanafoodhub.base.Hub;
import org.montanafoodhub.base.HubInit;
import org.montanafoodhub.base.Producer;

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
        TextView textView = (TextView) view.findViewById(R.id.producerNameTextView);
        textView.setText(_producer.getName());

        // address
        textView = (TextView) view.findViewById(R.id.producerAddressTextView);
        textView.setText(_producer.getLocation());

        // phone / email action - default is email.
        if (hasEmail(_producer) == false) {
            imageView = (ImageView) view.findViewById(R.id.contactImageView);
            imageView.setImageResource(R.drawable.ic_action_call_blue);

            textView = (TextView) view.findViewById(R.id.contactTextView);
            textView.setText(getResources().getText(R.string.producer_call_action));
        }


        // contact click listener
        View actionView = view.findViewById(R.id.contactActionLayout);
        actionView.setOnClickListener(this);

        // map click listener
        actionView = view.findViewById(R.id.mapActionLayout);
        actionView.setOnClickListener(this);

        // url click listener
        actionView = view.findViewById(R.id.urlActionLayout);
        actionView.setOnClickListener(this);


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

            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.certificationImageview);
            imageCache.loadImage(imageView, cert.getIconUrl(), R.drawable.default_certification);

            TextView textView = (TextView)relativeLayout.findViewById(R.id.certificationText);
            textView.setText(cert.getDisplayName());

            linearLayout.addView(relativeLayout);
        }
    }

    private boolean hasEmail(Producer producer) {
        return producer.getContactEmail().contains("@");
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.contactActionLayout) {
            onClickContact();
        }
        else if (v.getId() == R.id.mapActionLayout) {
            onClickMap();
        }
        else if (v.getId() == R.id.urlActionLayout) {
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
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + _producer.getContactEmail() + "," + HubInit.getHubEmailTo()));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Hub Request For: " + _producer.getName());
            intent.putExtra(Intent.EXTRA_TEXT, "<grower love here...>");
            ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_email_application, LogTag);
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + _producer.getContactEmail()));
            ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_phone_application, LogTag);
        }
    }

    private void onClickMap() {
        try {

            // various ways to check if 'the' google maps is installed. getApplicationInfo & getPackageInfo will throw
            // PackageManager.NameNotFoundException if maps is not installed while getLaunchIntentForPackage will return
            // null
            //            ApplicationInfo aInfo = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            //            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo("com.google.android.apps.maps", PackageManager.GET_ACTIVITIES);
            //            Intent i = getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");

            // vaious ways to check if there is an activity that can handle the desired intent. Either an empty list (queryIntentActivities)
            // or null (resolveActivity) wil be returned
            //            List<ResolveInfo> l = getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            //            ResolveInfo r = getActivity().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            //            ComponentName cn = intent.resolveActivity(getActivity().getPackageManager());


            Intent intent = new Intent(Intent.ACTION_VIEW);
            String data = String.format("geo:0,0?q=%s", URLEncoder.encode(_producer.getLocation(), "UTF-8"));
            intent.setData(Uri.parse(data));
            ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_maps_application, LogTag);
        }
        catch (Exception e) {
            Log.w(LogTag, e.toString());
        }
    }

    private void onClickUrl() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_producer.getWebsiteUrl()));
            ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_web_browser_application, LogTag);
        }
        catch (Exception e) {
            Log.w(LogTag, e.toString());
        }
    }

    private void onClickCertification(Certification cert) {
        if (cert.getWebsiteUrl().isEmpty() == false) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cert.getWebsiteUrl()));
            ActivityUtils.startImplicitActivity(getActivity(), intent, R.string.no_web_browser_application, LogTag);
        }
    }
}