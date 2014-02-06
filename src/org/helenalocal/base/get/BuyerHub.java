package org.helenalocal.base.get;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.helenalocal.base.Buyer;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Producer;

import java.io.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by abbie on 1/24/14.
 */
public class BuyerHub extends Hub implements Runnable {
    private static Context context;
    private static Calendar lastRefreshTS;
    private String fileName = "HL-BuyerHub.csv";
    protected String dataUrl = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdF8yUzVSNHJMUzhnYV9ULW1xdDR2SUE&single=true&gid=4&output=csv";


    public BuyerHub(Context context) {
        this.context = context;
        logTag = "BuyerHub ";
    }

    private void parseCSV(HashMap<String, Buyer> myBuyerMap, InputStream inputStream) throws IOException {
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
                // build item
                Buyer buyer = new Buyer();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // BID (Buyer ID)	Name	ContactEmail	Hours	Phone	WebsiteUrl	PhotoUrl	Location	Service Level (0-off, 1-on, 2-premium)
                if (iterator.hasNext()) {
                    String buyerId = iterator.next();
                    if (! buyerId.equals("")) {
                        buyer.setBID(buyerId);
                    }
                }
                if (iterator.hasNext()) {
                    String name = iterator.next();
                    if (! name.equals("")) {
                        buyer.setName(name);
                    }
                }
                if (iterator.hasNext()) {
                    String contactEmail = iterator.next();
                    if (! contactEmail.equals("")) {
                        buyer.setContactEmail(contactEmail);
                    }
                }
                if (iterator.hasNext()) {
                    String hours = iterator.next();
                    if (! hours.equals("")) {
                        buyer.setHours(hours);
                    }
                }
                if (iterator.hasNext()) {
                    String phone = iterator.next();
                    if (! phone.equals("")) {
                        buyer.setPhone(phone);
                    }
                }
                if (iterator.hasNext()) {
                    String websiteUrl = iterator.next();
                    if (! websiteUrl.equals("")) {
                        buyer.setWebsiteUrl(websiteUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String photoUrl = iterator.next();
                    if (! photoUrl.equals("")) {
                        buyer.setPhotoUrl(photoUrl);
                    }
                }
                if (iterator.hasNext()) {
                    String location = iterator.next();
                    if (! location.equals("")) {
                        buyer.setLocation(location);
                    }
                }
                if (iterator.hasNext()) {
                    String serviceLevel = iterator.next();
                    if (! serviceLevel.equals("")) {
                        buyer.setServiceLevel(serviceLevel);
                    }
                }
                myBuyerMap.put(buyer.getBID(), buyer);
            }
        }
    }

    protected HashMap<String, Buyer> readFromFile(Context context) {
        HashMap<String, Buyer> myBuyerMap = new HashMap<String, Buyer>();
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
                parseCSV(myBuyerMap, inputStream);
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(Hub.logTag, "File  (" + fileName + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(Hub.logTag, "Can not read file  (" + fileName + ") : " + e.toString());
        }
        Log.w(Hub.logTag, "Number of producers loaded: " + myBuyerMap.size());
        return myBuyerMap;
    }

    public HashMap<String, Buyer> getBuyerMap() throws IOException {
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

    public static Calendar getLastRefreshTS() {
        return lastRefreshTS;
    }

    @Override
    public void run() {
        try {
            Hub.buyerMap = new BuyerHub(context).getBuyerMap();
            Log.w(logTag,"BuyerHub().getBuyerMap loaded...");
        } catch (IOException e) {
            Log.w(logTag,"BuyerHub().getBuyerMap couldn't be loaded...");
        }

    }
}
