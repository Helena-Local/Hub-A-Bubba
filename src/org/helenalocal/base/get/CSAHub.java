package org.helenalocal.base.get;

import android.content.Context;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class CSAHub extends Hub {

    public CSAHub() {
        this.setFilename("HL-CSAHub.csv");
    }

    @Override
    public List<Product> getProductList(Context context) throws IOException {
        return null;
    }
}
