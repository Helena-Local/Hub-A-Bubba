package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubFactory;
import org.helenalocal.base.IHub;
import org.helenalocal.base.Product;
import org.helenalocal.utils.ViewServer;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Product>> {
    private final String logTag = "MainActivity";
    public static final String HUB_TYPE_KEY = "hubType";

    private static final int LoaderIdCSA = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        Bundle b = new Bundle();
        b.putString(HUB_TYPE_KEY, Hub.Type.CSA.name());
        getSupportLoaderManager().initLoader(LoaderIdCSA, b, this);

        ViewServer.get(this).addWindow(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ViewServer.get(this).removeWindow(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewServer.get(this).setFocusedWindow(this);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Hub.Type type = Hub.Type.valueOf(bundle.getString(HUB_TYPE_KEY));
        AsyncProductLoader loader = new AsyncProductLoader(this, type);

        //TODO shane -- This is a working example... Let me know if you have questions about it... :)
        Product product = new Product("Kevin's Farm","foo@home.com", "http://my.farm.com", "Produce", "Onion","http://recipe.com/onion","N/A",20,"lbs", Calendar.getInstance(),10.01,"from android");
        IHub myHub = HubFactory.buildHubFetch(Hub.Type.GROWER);
        try {
            // succeed
            myHub.setProduct(this.getApplicationContext(),product);
            // fail with invalid category...
            product.setCategory("Foo");
            myHub.setProduct(this.getApplicationContext(),product);
        } catch (Exception e) {
            // use email which will use smtp, so no need to retry, clean up, etc.
            Log.w(logTag, "e = " + e.toString());

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{ Hub.HUB_EMAIL_TO });
            email.putExtra(Intent.EXTRA_SUBJECT, Hub.HUB_EMAIL_SUBJECT);
            email.putExtra(Intent.EXTRA_TEXT, "We found a problem submitting your data to the Helena Local Hub... Click the send button for this email and we'll send the request to Helena Local for you... Next time you have a good network " +
                    "connection synchronize your email, then check your sent folder to make sure it went out.  Call us @ 406-219-1414 if you have questions or concerns.  \n\nDetails follow: \n----------\n" + product.toEmail());

            //need this to prompts email client only
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Product>> listLoader, List<Product> products) {
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}