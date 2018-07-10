package com.spectre.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.spectre.R;
import com.spectre.activity.CarDetailActivity;
import com.spectre.activity.HomeActivity;
import com.spectre.activity.NotificationDetailActivity;
import com.spectre.activity.WorkDetailActivity;
import com.spectre.activity.ZoomActivity;
import com.spectre.beans.AdPost;
import com.spectre.beans.NotificationListUser;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;
import com.spectre.utility.Utility;

import java.util.ArrayList;


/**
 * Created by Sumit on 9/25/2016.
 */
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    private final ArrayList<NotificationListUser> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;

    public NotificationListAdapter(Context appContext, ArrayList<NotificationListUser> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NotificationListUser adPost = arraylist.get(position);

        if (!adPost.getMsg().isEmpty()) {
            holder.txt_car_name.setText(adPost.getMsg());
        } else {
            holder.txt_car_name.setText(appContext.getString(R.string.na));
        }
//1,2,3
//Buy,Rent,Garage


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appContext, NotificationDetailActivity.class);
                intent.putExtra(Constant.DATA, adPost);
                appContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt_car_name;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_car_name = (CustomTextView) itemView.findViewById(R.id.txt_car_name);

        }
    }
}
