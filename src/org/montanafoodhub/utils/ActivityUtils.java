/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.montanafoodhub.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import org.montanafoodhub.Helena_Local_Hub.R;

public class ActivityUtils {

    public static void startImplicitActivity(Context context, Intent intent, int activityNotFoundId, String logTag) {

        try {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(context, context.getResources().getText(activityNotFoundId), Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Log.w(logTag, e.toString());
            Toast.makeText(context, R.string.unable_to_start_activity, Toast.LENGTH_LONG).show();
        }
    }
}
