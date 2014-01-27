package org.helenalocal.base;

import android.content.Context;
import org.helenalocal.base.Product;

import java.io.IOException;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public interface IHub {
   public List<Product> getProductList(Context context) throws IOException;
}
