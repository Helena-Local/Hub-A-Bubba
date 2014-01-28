package org.helenalocal.base.get;

import android.content.Context;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class SalesHub extends Hub {

    public SalesHub() {
        setFilename("HL-SalesHub.csv");

        // CSV file direct from spreadsheet
        setDataUrl("https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdHg5OVZmRVdoSWJ4NU92ekppNDl0dEE&single=true&gid=1&output=csv");
    }

    @Override
    public List<Product> getProductList(Context context) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(dataUrl);
        try {
            // first try the net
            HttpResponse response = client.execute(request);
            Log.w(Hub.BACKEND, "HTTP execute Response.getStatusLine() = " + response.getStatusLine());

            // make net version local
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            writeToFile(context, rd);
            Log.w(Hub.BACKEND, "Wrote file from the net to device...");
        } catch (UnknownHostException e) {
            Log.w(Hub.BACKEND, "Couldn't get the file from the net just using file from device... ");
        }

        // regardless of net work with file
        return readFromFile(context,Hub.CSV);
    }
}
