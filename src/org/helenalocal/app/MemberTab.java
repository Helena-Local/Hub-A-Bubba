package org.helenalocal.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.helenalocal.Helena_Local_Hub.R;

public class MemberTab extends Fragment {

    private static final String Tag = "MemberTab";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.member_tab, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button b = (Button) getActivity().findViewById(R.id.growerButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - pass the name for now. Evenutally will pass grower id or something.
                Intent i = new Intent(getActivity(), GrowerDetailActivity.class);
                i.putExtra("growerNameKey", "Acme Farms");
                startActivity(i);
            }
        });
    }
}