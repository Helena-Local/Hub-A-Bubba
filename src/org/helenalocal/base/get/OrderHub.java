package org.helenalocal.base.get;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Order;
import org.helenalocal.base.Producer;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class OrderHub extends Hub {
    String fileName = "HL-OrderHub.csv";
    protected String dataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=2&output=csv";


    public OrderHub() {
        logTag = "OrderHub ";
    }

    private void parseCSV(HashMap<String, Order> myOrderMap, InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        TextUtils.SimpleStringSplitter simpleStringSplitter = new TextUtils.SimpleStringSplitter(',');
        String receiveString = "";
        boolean firstTime = true;
        while ( (receiveString = bufferedReader.readLine()) != null ) {
            if (firstTime) {
                // remove header
                firstTime = false;
            } else {
                // build Order
                Order order = new Order();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // Date	OID (Order ID)	IID (Item ID)	PID (Producer ID)	BID (Buyer ID)	Buyer Url
                if (iterator.hasNext()) {
                    String date = iterator.next();
                    if (! date.equals("")) {
                        order.setDate(date);
                    }
                }if (iterator.hasNext()) {
                    String orderId = iterator.next();
                    if (! orderId.equals("")) {
                        order.setOrderID(orderId);
                    }
                }
                if (iterator.hasNext()) {
                    String itemId = iterator.next();
                    if (! itemId.equals("")) {
                        order.setItemID(itemId);
                    }
                }
                if (iterator.hasNext()) {
                    String producerId = iterator.next();
                    if (! producerId.equals("")) {
                        order.setProducerID(producerId);
                    }
                }
                if (iterator.hasNext()) {
                    String buyerId = iterator.next();
                    if (! buyerId.equals("")) {
                        order.setBuyerID(buyerId);
                    }
                }
                if (iterator.hasNext()) {
                    String buyerUrl = iterator.next();
                    if (! buyerUrl.equals("")) {
                        order.setBuyerUrl(buyerUrl);
                    }
                }
                myOrderMap.put(order.getOrderID(), order);
            }
        }
    }

    protected HashMap<String, Order> readFromFile(Context context) {
        HashMap<String, Order> myOrderMap = new HashMap<String, Order>();
        try {
            // getItem the time the file was last changed here
            File myFile = new File(context.getFilesDir() +"/" + fileName);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String lastRefreshTSStr = sdf.format(myFile.lastModified());
            Log.w(Hub.logTag, "Using file (" + fileName + ") last modified on : " + lastRefreshTSStr);
            lastRefreshTS = sdf.getCalendar();

            // create products from the file here
            InputStream inputStream = context.openFileInput(fileName);
            if ( inputStream != null ) {
                parseCSV(myOrderMap, inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(Hub.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(Hub.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(Hub.logTag, "Number of producers loaded: " + myOrderMap.size());
        return myOrderMap;
    }

    public HashMap<String, Order> getOrderMap(Context context) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(dataUrl);
        try {
            // first try the net
            HttpResponse response = client.execute(request);
            Log.w(Hub.logTag, "HTTP execute Response.getStatusLine() = " + response.getStatusLine());

            // make net version local
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            writeToFile(context, rd, fileName);
            Log.w(Hub.logTag, "Wrote file from the net to device...");
        } catch (UnknownHostException e) {
            Log.w(Hub.logTag, "Couldn't getItem the file from the net just using file from device... ");
        }

        // regardless of net work with file
        return readFromFile(context);
    }
}
