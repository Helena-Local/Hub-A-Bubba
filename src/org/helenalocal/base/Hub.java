package org.helenalocal.base;

import android.content.Context;
import android.util.Log;
import org.helenalocal.base.post.GrowerHub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * Created by abbie on 1/24/14.
 */
public abstract class Hub {
    protected static String logTag = "Hub ";
    public static final String HUB_EMAIL_TO = "info@helenalocal.org";
    public static final String HUB_EMAIL_SUBJECT = "HL Hub - Request email...";

    // the delay between the termination of one execution and the commencement of the next
    // defaults set below
    public static long buyerDelay = 2;
    public static HashMap<String, Buyer> buyerMap = new HashMap<String, Buyer>();

    public static long itemDelay = 1;
    public static HashMap<String, Item> itemMap = new HashMap<String, Item>();

    public static long orderDelay = 2;
    public static HashMap<String, Order> orderMap = new HashMap<String, Order>();

    public static long producerDelay = 2;
    public static HashMap<String, Producer> producerMap = new HashMap<String, Producer>();

    public static GrowerHub growerHub = new GrowerHub();


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

    public static void setItemDelay(long itemDelay) {
        Hub.itemDelay = itemDelay;
    }

    public static void setBuyerDelay(long buyerDelay) {
        Hub.buyerDelay = buyerDelay;
    }

    public static void setOrderDelay(long orderDelay) {
        Hub.orderDelay = orderDelay;
    }

    public static void setProducerDelay(long producerDelay) {
        Hub.producerDelay = producerDelay;
    }

}
