package org.helenalocal.base.post;

import android.content.Context;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class GrowerHub extends Hub {

    public GrowerHub() {
        this.setFilename("HL-GrowerHub.csv");
    }

    @Override
    public List<Product> getProductList(Context context) throws IOException {
        return null;
    }
}
