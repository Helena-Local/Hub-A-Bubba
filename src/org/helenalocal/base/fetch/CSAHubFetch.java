package org.helenalocal.base.fetch;

import android.content.Context;
import org.helenalocal.base.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class CSAHubFetch extends HubFetch {

    public CSAHubFetch() {
        this.setFilename("HL-CSAHubFetch.csv");
    }

    @Override
    public List<Product> getProductList(Context context) throws IOException {
        return null;
    }
}
