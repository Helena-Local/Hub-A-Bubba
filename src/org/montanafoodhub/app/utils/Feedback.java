/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import org.montanafoodhub.Helena_Hub.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Feedback {

    private static final String LogTag = Feedback.class.getSimpleName();
    private static final String Extension = "png";

    private Context _context;
    private Window _window;
    private String _filename;

    public Feedback(Context context, Window window) {
        _context = context;
        _window = window;
    }

    public void finish() {
        _context.deleteFile(_filename);
    }

    public Intent getFeedbackIntent() {
        String uriString = String.format("mailto:%s", _context.getResources().getString(R.string.feedback_email_address));
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uriString));
        intent.putExtra(Intent.EXTRA_SUBJECT, _context.getResources().getString(R.string.feedback_email_subject));

        Bitmap bitmap = getScreenshot();
        if (bitmap != null) {
            FileOutputStream fileOutStream = null;
            File pngFile = null;

            // NOTE: MODE_WORLD_READABLE is necessary so that the email application can attach the screen shot.
            // Yes, MODE_WORLD_READABLE is deprecated due to security concerns however this is just a screen shot. If
            // there is any concern then we can skip attaching a screen shot.

            try {
                _filename = generateFilename();
                fileOutStream = _context.openFileOutput(_filename, Context.MODE_WORLD_READABLE);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutStream);
                fileOutStream.flush();
                pngFile = _context.getFileStreamPath(_filename);
            }
            catch (Exception e) {
                Log.w(LogTag, e.toString());
            }
            finally {
                try {
                    if (fileOutStream != null) {
                        fileOutStream.close();
                    }
                }
                catch (Exception e)
                {
                    Log.w(LogTag, e.toString());
                }
            }

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pngFile));
        }

        return intent;
    }

    private String generateFilename() {
        // Feedback_YYYY-MM-DD-HH-MM-SS.png
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String dateString = dateFormat.format(Calendar.getInstance().getTime());

        String filenameBase = _context.getResources().getString(R.string.feedback_filename_base);
        String filename = String.format("%s_%s.%s", filenameBase, dateString, Extension);

        return filename;
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
            rootView.setDrawingCacheEnabled(false);

        } catch (Exception e) {
            Log.w(LogTag, e.toString());
        }

        return screenShot;
    }
}
