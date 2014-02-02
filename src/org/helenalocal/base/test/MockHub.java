package org.helenalocal.base.test;

import android.content.Context;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;
import org.helenalocal.base.IHub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class MockHub extends Hub implements IHub {
    @Override
    public List<Item> getProduct(Context context) {
        // CSV from spreadsheet
        // Item Description,Units Available,Unit Price,Units Desc,Notes

        ArrayList<Item> myItems = new ArrayList<Item>();
        // String farmName, String farmEmailAddress, String category,
        // String productDesc, String growerAgreementId, Integer unitsAvailable,
        // String unitDesc, Calendar deliveryDate, Double unitPrice, String note

        myItems.add(new Item("Kevin's Producer","foo@home.com", "http://my.farm.com", "Produce", "Onion","http://recipe.com/onion","N/A",20,"lbs", Calendar.getInstance(),10.01,"note 1"));
        myItems.add(new Item("Kevin's Producer","foo@home.com", "http://my.farm.com", "Produce", "Beet","http://recipe.com/beet","2014-13",29,"lbs", Calendar.getInstance(),10.02,"note 2"));
        myItems.add(new Item("Kevin's Producer","foo@home.com", "http://my.farm.com", "Produce", "Potato","http://recipe.com/potato","N/A",28,"lbs", Calendar.getInstance(),10.03,"note 3"));
        myItems.add(new Item("Kevin's Producer","foo@home.com", "http://my.farm.com", "Produce", "Lettuce","http://recipe.com/lettuce","N/A",27,"lbs", Calendar.getInstance(),10.04,"note 4"));
        myItems.add(new Item("Kevin's Producer","foo@home.com", "http://my.farm.com", "Produce", "Chard","http://recipe.com/chard","N/A",26,"lbs", Calendar.getInstance(),10.05,"note 5"));

        return myItems;
    }

    @Override
    public void setProduct(Context context, Item item) throws IOException {

    }
}
