package com.artico.delivery.pedidos.ui.dialogs.rate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.artico.delivery.pedidos.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gustavo on 10/9/17.
 */

public class CustomItemAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private LayoutInflater inflater=null;
    Context ctx;

    public CustomItemAdapter(Context context, ArrayList<HashMap<String, String>> d)
    {
        ctx = context;
        data=d;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(convertView==null) vi = inflater.inflate(R.layout.listview_rating, null);

        TextView title = vi.findViewById(R.id.more_item);
//        TextView price = (TextView) vi.findViewById(R.id.itemPrice);

        HashMap<String, String> category = new HashMap<String, String>();
        category = data.get(position);

        title.setText(category.get("itemName"));
//        price.setText(category.get("itemPrice"));
        String itemId = category.get("itemId");

        return vi;
    }
}
