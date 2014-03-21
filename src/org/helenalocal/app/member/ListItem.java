/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app.member;

import android.view.View;

public abstract class ListItem {

    public abstract int getViewId();
    public abstract void loadView(View view);
}
