package org.helenalocal.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Producer;

public class GrowerDetailActivity extends Activity {

    public static final String EXTRA_PRODUCER_ID = "org.helenalocal.extra.producer_id";

    private static final String Tag = "GrowerDetailActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grower_detail_activity);

        String producerId = getIntent().getStringExtra(EXTRA_PRODUCER_ID);
        Producer producer = Hub.producerMap.get(producerId);

        TextView tv = (TextView) findViewById(R.id.welcomeTextView);
        tv.setText(String.format("Welcome %s", producer.getName()));
    }
}