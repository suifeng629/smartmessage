package com.ctid.intelsmsapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.ctid.intelsmsapp.R;
import com.ctid.intelsmsapp.entity.Menu;

public class ChildMenuListAdapter extends BaseAdapter {

    private Context context;
    private List<Menu> typeInfoList;

    public ChildMenuListAdapter(Context context, List<Menu> typeInfoList) {
        this.context = context;
        this.typeInfoList = typeInfoList;
    }

    @Override
    public int getCount() {
        return typeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return typeInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.child_menu_item, null);
        }
        convertView.setLayoutParams(new ListView.LayoutParams(
                ListView.LayoutParams.WRAP_CONTENT,
                ListView.LayoutParams.WRAP_CONTENT));
        TextView text = (TextView) convertView.findViewById(R.id.title_name);
        text.setText(typeInfoList.get(position).getMenuName());

        return convertView;
    }
}
