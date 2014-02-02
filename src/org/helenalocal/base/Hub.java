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
public abstract class Hub implements IHub {
    private String filename ="hl-out.txt";
    protected String dataUrl = "";
    protected Calendar lastRefreshTS;
    protected static String logTag = "Hub ";
    public static final String HUB_EMAIL_TO = "info@helenalocal.org";
    public static final String HUB_EMAIL_SUBJECT = "HL Hub - Request email...";

    public enum HubType {
        GROWER, ITEM, RESTAURANT, FARM, ORDER, MOCK
    }

    public enum FileType {
        CSV, RSS
    }

    private void parseCSV(ArrayList<Item> myItems, InputStream inputStream) throws IOException {
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
                // build products
                Item out = new Item();
                simpleStringSplitter.setString(receiveString);
                Iterator<String> iterator = simpleStringSplitter.iterator();

                // String category, productDesc, Integer unitsAvailable, Double unitPrice, String unitDesc, String note
                if (iterator.hasNext()) {
                    String category = iterator.next();
                    if (! category.equals("")) {
                        out.setCategory(category);
                    }
                }
                if (iterator.hasNext()) {
                    String productDesc = iterator.next();
                    if (! productDesc.equals("")) {
                        out.setProductDesc(productDesc);
                    }
                }
                if (iterator.hasNext()) {
                    String unitsAvailable = iterator.next();
                    if (! unitsAvailable.equals("")) {
                        out.setUnitsAvailable(Integer.valueOf(unitsAvailable));
                    }
                }
                if (iterator.hasNext()) {
                    String unitPrice = iterator.next();
                    if (! unitPrice.equals("")) {
                        Object o = unitPrice.replace("$", "");
                        out.setUnitPrice(Double.parseDouble(o.toString()));
                    }
                }
                if (iterator.hasNext()) {
                    String unitDesc = iterator.next();
                    if (! unitDesc.equals("")) {
                        out.setUnitDesc(unitDesc);
                    }
                }
                if (iterator.hasNext()) {
                    String note = iterator.next();
                    if (! note.equals("")) {
                        out.setNote(note);
                    }
                }
                myItems.add(out);
            }
        }
    }

    protected void setFilename(String filename) {
        this.filename = filename;
    }

    protected String getDataUrl() {
        return dataUrl;
    }

    protected void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }


    protected void writeToFile(Context context, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(Hub.logTag, "File (" + filename + ") write failed: " + e.toString());
        }
    }

    protected void writeToFile(Context context,BufferedReader rd) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
            String line = "";
            while ((line = rd.readLine()) != null) {
                outputStreamWriter.write(line +'\n');
            }
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(Hub.logTag, "File (" + filename + ") write failed: " + e.toString());
        }
    }

    protected ArrayList<Item> readFromFile(Context context, Hub.FileType ftype) {
        ArrayList<Item> myItems = new ArrayList<Item>();
        try {
            // get the time the file was last changed here
            File myFile = new File(context.getFilesDir() +"/" + filename);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String lastRefreshTSStr = sdf.format(myFile.lastModified());
            Log.w(Hub.logTag, "Using file (" + filename + ") last modified on : " + lastRefreshTSStr);
            lastRefreshTS = sdf.getCalendar();

            // create products from the file here
            InputStream inputStream = context.openFileInput(filename);
            if ( inputStream != null ) {
                switch (ftype) {
                    case CSV:
                        parseCSV(myItems, inputStream);
                        break;
                }
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(Hub.logTag, "File  (" + filename + ") not found: " + e.toString());
        } catch (IOException e) {
            Log.e(Hub.logTag, "Can not read file  (" + filename + ") : " + e.toString());
        }
        Log.w(Hub.logTag, "Number of products loaded: " + myItems.size());
        return myItems;
    }

    public Calendar getLastRefreshTS() {
        return lastRefreshTS;
    }
}
