package com.spectre.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.spectre.R;
import com.spectre.beans.CarName;

import java.util.ArrayList;
import java.util.List;

public class CarNameListAdapter extends ArrayAdapter<CarName> {

   Context context;
    ArrayList<CarName> users;
    public CarNameListAdapter(Context context, List<CarName> users) {
        super(context, 0, users);
        this.context=context;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CarName user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialoglistitem, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        tvName.setText(user.getCar_name());

        return convertView;
    }
}