/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.base.test;

import android.content.Context;
import org.helenalocal.base.Hub;
import org.helenalocal.base.Item;
import org.helenalocal.base.Producer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by abbie on 1/24/14.
 */
public class MockHub extends Hub {
    public List<Item> getItems(Context context) {
        ArrayList<Item> myItems = new ArrayList<Item>();
        // public Producer(String PID, String name, String contactEmail, String websiteUrl, String photoUrl, String location) {
        Producer producer = new Producer("P-2013-0", "Western Montana Growers’ Cooperative", "grower@wmgcoop.com", "http://www.wmgcoop.com/", "http://g.virbcdn.com/_f2/images/58/PageImage-524372-4680215-WMGC_WebBanner.jpg", "Arlee, MT 59821", "C-0", "This day..");
        // public Item(String IID,Producer producer,boolean inCsaThisWeek, String category, String productDesc, String productUrl, String productImageUrl, Integer unitsAvailable,
        //        String unitDesc, Double unitPrice, Calendar deliveryDate, String note) {
        myItems.add(new Item("I-2014-2-2-3","P-2013-0", true, "Produce", "Leeks","http://en.wikipedia.org/wiki/Leek‎",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/4/43/Leek.jpg/160px-Leek.jpg",20,"10 lbs",10.01,Calendar.getInstance(),"note 1"));

        myItems.add(new Item("I-2014-2-2-4","P-2013-0", true, "Produce", "Carrots","http://en.wikipedia.org/wiki/Carrots‎",
                "http://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Carrots_on_end.jpg/220px-Carrots_on_end.jpg",2,"25 lbs",23.01,Calendar.getInstance(),"note 2"));

        myItems.add(new Item("I-2014-2-2-9","P-2013-1", true, "Cheese", "Jack Cheese","",
                "",5,"24 oz",18.05,Calendar.getInstance(),""));

        myItems.add(new Item("I-2014-2-2-10","P-2013-2", false, "Milk", "Whole Milk","‎",
                "",4,"1 gal",7.01,Calendar.getInstance(),""));

        myItems.add(new Item("I-2014-2-2-11","P-2013-0", true, "Dry Goods", "Lentil","‎",
                "",36,"16 oz",5.89,Calendar.getInstance(),""));

        return myItems;
    }
}
