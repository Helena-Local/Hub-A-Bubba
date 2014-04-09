/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class Feedback implements ServiceConnection {

    private static final String LogTag = Feedback.class.getSimpleName();
    private Window _window;

    private Feedback(Window window) {
        _window = window;
    }

    public static void sendFeedBack(Context context, Window window) {

        Feedback fb = new Feedback(window);
        Intent intent = new Intent(Intent.ACTION_BUG_REPORT);

        context.bindService(intent, fb, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {}

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            Parcel parcel = Parcel.obtain();
            Bitmap bitmap = getScreenshot();
            if (bitmap != null) {
                bitmap.writeToParcel(parcel, 0);
            }

            service.transact(Binder.FIRST_CALL_TRANSACTION, parcel, null, 0);
            parcel.recycle();

        } catch (RemoteException e) {
            Log.e(LogTag, "RemoteException", e);
        }

    }

    private Bitmap getScreenshot() {

        Bitmap screenShot = null;

        try {
            View rootView = _window.getDecorView().getRootView();
            rootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = rootView.getDrawingCache();
            if (bitmap != null)
            {
                double height = bitmap.getHeight();
                double width = bitmap.getWidth();
                double ratio = Math.min(600 / width, 600 / height);
                screenShot = Bitmap.createScaledBitmap(bitmap, (int)Math.round(width * ratio), (int)Math.round(height * ratio), true);
            }
        } catch (Exception e) {
            Log.w(LogTag, "Error getting current screenshot: ", e);
        }

        return screenShot;
    }
}
