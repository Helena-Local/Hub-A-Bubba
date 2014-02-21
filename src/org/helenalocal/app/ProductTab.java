/*
 * Copyright (c) 2014. This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License for Helena Local Inc. All rights reseved.
 */

package org.helenalocal.app;

import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.base.Hub;
import org.helenalocal.base.HubInit;
import org.helenalocal.base.Item;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductTab extends TabBase {

    private static String LogTag = "ProductTab";

    private List<Object> _itemList;
    private ProductItemAdapter _arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeReceiver(HubInit.HubType.ITEM_HUB);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.product_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textView = (TextView) getActivity().findViewById(R.id.welcomeText);
        Linkify.addLinks(textView, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(textView, Pattern.compile("HelenaLocal.org"), "http://", null, new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "www.helenalocal.org";
            }
        });

        _itemList = new ArrayList<Object>();
        _arrayAdapter = new ProductItemAdapter(getActivity(), _itemList);

        ListView listView = (ListView) getActivity().findViewById(R.id.productListView);
        listView.addHeaderView(new View(getActivity()));
        listView.addFooterView(new View(getActivity()));
        listView.setAdapter(_arrayAdapter);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        // grab all items for sale to community where qty > 0!
        // Toss them in a TreeMap keyed by category (sorted) with the list of items in the category as the value
        TreeMap<String, List<Item>> productMap = new TreeMap<String, List<Item>>();
        for (Item item : Hub.itemMap.values()) {
            if (item.getUnitsAvailable() > 0) {
                List<Item> itemList = productMap.get(item.getCategory());
                if (itemList == null) {
                    itemList = new ArrayList<Item>();
                    productMap.put(item.getCategory(), itemList);
                }

                itemList.add(item);
            }
        }

        // now that we have the data sorted by and organized by category, flatten it out into a list.
        // NOTE: This list contains both String (category) and Item (product)
        _itemList.clear();
        for (Map.Entry<String, List<Item>> entry : productMap.entrySet()) {

            // add the category
            _itemList.add(entry.getKey());

            // sort the products
            List<Item> productList = entry.getValue();
            Collections.sort(productList, new Comparator<Item>() {
                public int compare(Item i1, Item i2) {
                    return i1.getProductDesc().compareTo(i2.getProductDesc());
                }
            });


            // add the products
            for (Item item : productList) {
                Log.w(LogTag, item.getProductDesc());
                _itemList.add(item);
            }
        }

        _arrayAdapter.notifyDataSetChanged();
    }
}

//public class ProductTab extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
//
//    private static String Tag = "ProductTab";
//    private static final int LoaderId = 0;
//
//    private List<Item> _itemList;
//    private ProductItemAdapter _arrayAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.product_tab, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        _itemList = new ArrayList<Item>();
//        _arrayAdapter = new ProductItemAdapter(getActivity(), R.layout.product_view, _itemList);
//
//        ListView listView = (ListView)getActivity().findViewById(R.id.productListView);
//        listView.setAdapter(_arrayAdapter);
//
//        getActivity().getSupportLoaderManager().initLoader(LoaderId, null, this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getActivity().getSupportLoaderManager().restartLoader(LoaderId, null, this);
//    }
//
//
//    // ***
//    // LoaderManager callbacks
//    // ***
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
//        CursorLoader loader = new CursorLoader(getActivity(), ItemProvider.CONTENT_URI, null, null, null, null);
//        return loader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        int itemIdIndex = cursor.getColumnIndexOrThrow(ItemProvider.KEY_ITEM_ID);
//        int producerIdIndex = cursor.getColumnIndexOrThrow(ItemProvider.KEY_PRODUCER_ID);
//        int inCSAIndex = cursor.getColumnIndex(ItemProvider.KEY_IN_CSA);
//        int productDescIndex = cursor.getColumnIndexOrThrow(ItemProvider.KEY_PRODUCT_DESC);
//        int unitsAvailableIndex = cursor.getColumnIndex(ItemProvider.KEY_UNITS_AVAILABLE);
//        int unitPriceIndex = cursor.getColumnIndex(ItemProvider.KEY_UNIT_PRICE);
//        int unitDescIndex = cursor.getColumnIndex(ItemProvider.KEY_UNIT_DESC);
//
//        _itemList.clear();
//        while (cursor.moveToNext() == true) {
//            Item newItem = new Item(cursor.getString(itemIdIndex),
//                                    cursor.getString(producerIdIndex),
//                                    (cursor.getInt(inCSAIndex) == 1) ? true : false,
//                                    null,
//                                    cursor.getString(productDescIndex),
//                                    null,
//                                    null,
//                                    Integer.valueOf(cursor.getInt(unitsAvailableIndex)),
//                                    cursor.getString(unitDescIndex),
//                                    cursor.getDouble(unitPriceIndex),
//                                    null,
//                                    null);
//
//            _itemList.add(newItem);
//        }
//
//        _arrayAdapter.notifyDataSetChanged();
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//    }
//}