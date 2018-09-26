package com.spectre.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.spectre.R;
import com.spectre.beans.CarName;
import com.spectre.beans.ModelName;

import java.util.ArrayList;

public class ModelNameListAdapter extends ArrayAdapter<ModelName> {

   Context context;
    public ModelNameListAdapter(Context context, ArrayList<ModelName> users) {
        super(context, 0, users);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ModelName user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialoglistitem, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        tvName.setText(user.getModel_name());

        // Return the completed view to render on screen
        return convertView;
    }
}