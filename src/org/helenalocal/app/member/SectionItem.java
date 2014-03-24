/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.member;

import android.view.View;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.ListItem;

public class SectionItem extends ListItem {

    private String _title;

    public SectionItem(String title) {
        _title = title;
    }

    @Override
    public int getViewId() {
        return R.layout.product_share_listview_section;
    }

    @Override
    public void loadView(View view) {
        TextView textView = (TextView)view.findViewById(R.id.textView);
        textView.setText(_title);
    }
}
