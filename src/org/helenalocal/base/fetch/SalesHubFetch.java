package org.helenalocal.base.fetch;

import android.content.Context;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.helenalocal.base.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class SalesHubFetch extends HubFetch {

    public SalesHubFetch() {
        this.setFilename("HL-SalesHubFetch.csv");
    }

    @Override
    public List<Product> getProductList(Context context) throws IOException {
        HttpClient client = new DefaultHttpClient();

        // Actual Hub page
        // String url = "http://hub.helenalocal.org/";

        // CSV file direct from spreadsheet
        String url = "https://docs.google.com/spreadsheet/pub?key=0AtzLFk-EifKHdHg5OVZmRVdoSWJ4NU92ekppNDl0dEE&single=true&gid=1&output=csv";
        HttpGet request = new HttpGet(url);
        try {
            // first try the net
            HttpResponse response = client.execute(request);
            System.out.println("response.getStatusLine() = " + response.getStatusLine());

            // make net version local
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            writeToFile(context, rd);
            System.out.println("wrote file from the net...");
        } catch (UnknownHostException e) {
            System.out.println("couldn't get the file from the net... ");
        }

        // regardless of net work with file
        return readFromFile(context);
    }
}
