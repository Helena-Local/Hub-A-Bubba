package org.helenalocal.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;

public class GrowerDetailActivity extends Activity {

    private static final String Tag = "GrowerDetailActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_detail_activity);

        String growerName = getIntent().getStringExtra("growerNameKey");
        TextView tv = (TextView) findViewById(R.id.welcomeTextView);
        tv.setText(String.format("Welcome %s", growerName));
    }
}