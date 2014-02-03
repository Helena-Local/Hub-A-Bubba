package org.helenalocal.base;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public abstract class Hub {
    protected Calendar lastRefreshTS;
    protected static String logTag = "Hub ";
    public static final String HUB_EMAIL_TO = "info@helenalocal.org";
    public static final String HUB_EMAIL_SUBJECT = "HL Hub - Request email...";

    protected void writeToFile(Context context, String data, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(Hub.logTag, "File (" + fileName + ") write failed: " + e.toString());
        }
    }

    protected void writeToFile(Context context,BufferedReader rd, String fileName) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            String line = "";
            while ((line = rd.readLine()) != null) {
                outputStreamWriter.write(line +'\n');
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(Hub.logTag, "File (" + fileName + ") write failed: " + e.toString());
        }
    }

    public Calendar getLastRefreshTS() {
        return lastRefreshTS;
    }
}
