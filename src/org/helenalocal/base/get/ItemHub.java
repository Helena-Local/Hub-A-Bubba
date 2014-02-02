package org.helenalocal.base.get;

import android.content.Context;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.helenalocal.base.Item;
import org.helenalocal.base.Producer;
import org.helenalocal.base.Hub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class ItemHub extends Hub {

    private void loadFarms() {
        List<Producer> producerArr = new ArrayList<Producer>();
        producerArr.add(new Producer("1","Kevin's Producer","kevin@farm.com","http://my.farm.com"));

    }

    public ItemHub() {
        logTag = "ItemHub ";
        setFilename("HL-ItemHub.csv");
        setDataUrl("https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdHg5OVZmRVdoSWJ4NU92ekppNDl0dEE&single=true&gid=2&output=csv");

    }

    @Override
    public List<Item> getProduct(Context context) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(dataUrl);
        try {
            // first try the net
            HttpResponse response = client.execute(request);
            Log.w(Hub.logTag, "HTTP execute Response.getStatusLine() = " + response.getStatusLine());

            // make net version local
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            writeToFile(context, rd);
            Log.w(Hub.logTag, "Wrote file from the net to device...");
        } catch (UnknownHostException e) {
            Log.w(Hub.logTag, "Couldn't get the file from the net just using file from device... ");
        }

        //TODO kevin load farms here!
        loadFarms();

        // regardless of net work with file
        return readFromFile(context, FileType.CSV);

    }

    @Override
    public void setProduct(Context context, Item item) throws Exception {

    }

}
