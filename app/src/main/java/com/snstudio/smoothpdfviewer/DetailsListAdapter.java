package com.snstudio.smoothpdfviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsListAdapter extends BaseAdapter {
    private final String[] titles = {"Titles:", "Author:", "Subject:",
            "Keywords:", "Creator:", "Producer:", "Creation Date:", "Mod Date:"};
    public ArrayList<String> detailsList;
    public Context context;

    DetailsListAdapter(ArrayList<String> detailsList, Context context) {
        this.detailsList = detailsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return detailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.details_list_item, parent, false);

        TextView title = convertView.findViewById(R.id.tv_titles);
        TextView content = convertView.findViewById(R.id.tv_content);

        title.setText(titles[position]);
        content.setText(metaIsNull(detailsList.get(position)));

        return convertView;
    }

    private String metaIsNull(String meta) {
        if (meta == null || meta.isEmpty())
            return "No Data";
        else
            return meta;
    }
}
