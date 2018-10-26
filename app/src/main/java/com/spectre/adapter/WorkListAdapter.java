package com.spectre.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.spectre.R;
import com.spectre.activity.ManageAdActivity;
import com.spectre.activity.ManageRentedActivity;
import com.spectre.activity.PostAdActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.activity.WorkDetailActivity;
import com.spectre.beans.AdPost;
import com.spectre.beans.Work;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;

import java.util.ArrayList;


/**
 * Created by Sumit on 9/25/2016.
 */
public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.ViewHolder> {

    private ArrayList<Work> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;

    public WorkListAdapter(Context appContext, ArrayList<Work> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (status == 1) {
            //v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail, parent, false);
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seller_profile, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work_detail_, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (status == 2) { //if gallary
            setGallaryData(holder,position);
        }
        else{ //if seller profile
            setSellerData(holder,position);
        }

    }

    private void setSellerData(ViewHolder holder, final int position) {
        final Work adPost = arraylist.get(position);
        String nameModel = "";
        String yearMileage = "";
        if (!adPost.getCar_name().isEmpty()) {
            nameModel = adPost.getCar_name().trim();
        }

        if (!adPost.getModel().isEmpty()) {
            nameModel = nameModel + " " + adPost.getModel().trim();
        }
        if (!nameModel.isEmpty()) {
            holder.txtCarName.setText(nameModel);
        } else {
            holder.txtCarName.setText(appContext.getString(R.string.na));
        }
        if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
            new AQuery(appContext).id(holder.ivProduct).image(arraylist.get(position).getImage().get(0), true, true, 300,R.color.grayimgback);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(appContext, WorkDetailActivity.class);
                intent.putExtra(Constant.DATA, adPost);
                intent.putExtra(Constant.POSITION, position);
                appContext.startActivity(intent);

            }
        });
    }

    private void setGallaryData(final ViewHolder holder, final int position) {
        final Work adPost = arraylist.get(position);
        String nameModel = "";
        String yearMileage = "";

        if (!adPost.getCar_name().isEmpty()) {
            nameModel = adPost.getCar_name().trim();
        }

        if (!adPost.getModel().isEmpty()) {
            nameModel = nameModel + " \u2022 " + adPost.getModel().trim();
        }
        if (!nameModel.isEmpty()) {
            holder.txtCarName.setText(nameModel);
        } else {
            holder.txtCarName.setText(appContext.getString(R.string.na));
        }
        if (!adPost.getPrice().isEmpty()) {
            holder.txtCarPrice.setText(appContext.getString(R.string.dollar) + " " + adPost.getPrice().trim());
        } else {
            holder.txtCarPrice.setText(appContext.getString(R.string.na));
        }

        if (!adPost.getYear().isEmpty()) {
            yearMileage = adPost.getYear().trim();
        }

        if (!adPost.getMileage().isEmpty()) {
            yearMileage = yearMileage + " \u2022 " + adPost.getMileage().trim() + " " + appContext.getString(R.string.miles);
        }

        if (!yearMileage.isEmpty()) {
            holder.txtCarModel.setText(yearMileage);
        } else {
            holder.txtCarModel.setText(appContext.getString(R.string.na));
        }
        if (!adPost.getColor().trim().isEmpty())
            holder.txtColor.setText(adPost.getColor());
        else
            holder.txtColor.setText("");

        if (!adPost.getMileage().trim().isEmpty())
            holder.txtMiles.setText(adPost.getMileage() + " " + appContext.getString(R.string.miles));
        else
            holder.txtMiles.setText("");

        if (!adPost.getYear().trim().isEmpty())
            holder.txtYear.setText(adPost.getYear());
        else
            holder.txtYear.setText("");

        if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
            new AQuery(appContext).id(holder.ivProduct).image(arraylist.get(position).getImage().get(0), true, true, 300, R.drawable.ic_launcher_web);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appContext, WorkDetailActivity.class);
                intent.putExtra(Constant.DATA, adPost);
                intent.putExtra(Constant.POSITION, position);
                appContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCarName, txtCarPrice, txtCarModel, txtColor, txtMiles, txtYear;
        ImageView ivProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            if (status==1) {
                ivProduct = (ImageView) itemView.findViewById(R.id.iv_product);
                txtCarName = (TextView) itemView.findViewById(R.id.txt_car_name);
                txtCarPrice = (TextView) itemView.findViewById(R.id.txt_car_price);
                txtCarModel = (TextView) itemView.findViewById(R.id.txt_car_model);
                txtColor = (TextView) itemView.findViewById(R.id.txtColor);
                txtMiles = (TextView) itemView.findViewById(R.id.txtMiles);
                txtYear = (TextView) itemView.findViewById(R.id.txtYear);
            }
            else{
                txtCarName = (TextView) itemView.findViewById(R.id.txt_car_name);
                ivProduct = (ImageView) itemView.findViewById(R.id.iv_product);
            }
        }
    }
}
