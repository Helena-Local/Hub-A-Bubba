package org.helenalocal.app.producer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.helenalocal.Helena_Local_Hub.R;
import org.helenalocal.app.DismissableInfoHeaderItem;
import org.helenalocal.app.ListItem;
import org.helenalocal.app.member.ProductItem;
import org.helenalocal.app.restaurant.BuyerItem;
import org.helenalocal.base.Producer;

import java.util.List;

public class ProducerListAdapter extends BaseAdapter {

    private static String logTag = "ProducerListAdapter";
    private static final int ViewTypeUndefined = -1;
    private static final int ViewTypeInfoHeader = 0;
    private static final int ViewTypeProducer = 1;
    private static final int ViewTypeCount = 2;

    private List<ListItem> _itemList;
    private Context _context;

    public ProducerListAdapter(Context context, List<ListItem> items) {
        _context = context;
        _itemList = items;
    }

    @Override
    public int getCount() {
        return _itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return _itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return ViewTypeCount;
    }

    @Override
    public int getItemViewType(int position) {

        int type = ViewTypeUndefined;

        Object item = _itemList.get(position);
        if (item instanceof DismissableInfoHeaderItem) {
            type = ViewTypeInfoHeader;
        }
        else if (item instanceof ProducerItem) {
            type = ViewTypeProducer;
        }

        return type;
    }

    @Override
    public boolean isEnabled(int position) {

        return (getItemViewType(position) == ViewTypeProducer) ? true : false;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem item = _itemList.get(position);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(_context).inflate(item.getViewId(), parent, false);
        }

        item.loadView(view);

        return view;
    }

}