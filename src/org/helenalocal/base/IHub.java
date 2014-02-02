package org.helenalocal.base;

import android.content.Context;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public interface IHub {
    public List<Item> getProduct(Context context) throws IOException;
    public void setProduct(Context context, Item item) throws Exception;
    public Calendar getLastRefreshTS();
}
