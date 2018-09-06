package com.spectre.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.spectre.R;
import com.spectre.activity.ManageAdActivity;
import com.spectre.activity.ManageRentedActivity;
import com.spectre.activity.ManageWorkActivity;
import com.spectre.activity.PostAdActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.beans.AdPost;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Sumit on 9/25/2016.
 */
public class ManageAdsListAdapter extends RecyclerView.Adapter<ManageAdsListAdapter.ViewHolder> {

    private final ArrayList<AdPost> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;

    public ManageAdsListAdapter(Context appContext, ArrayList<AdPost> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_detail_2, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AdPost adPost = arraylist.get(position);

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
        if (!adPost.getCar_type().trim().isEmpty())
            holder.txtType.setText(adPost.getCar_type());
        else
            holder.txtType.setText("");


//        if(!adPost.getLocation().isEmpty()){
//                   holder.txtRentLocation.setText(adPost.getLocation());
//        }

        if (appContext instanceof ManageAdActivity) {
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

            holder.txtCarModel.setVisibility(View.VISIBLE);
        } else {
            yearMileage = "";
            holder.txtCarModel.setVisibility(View.GONE);
        }

        if (!adPost.getPrice().isEmpty()) {
            holder.txtCarPrice.setText(appContext.getString(R.string.dollar) + " " + adPost.getPrice().trim());
        } else {
            holder.txtCarPrice.setText(appContext.getString(R.string.na));
        }

        if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
            new AQuery(appContext).id(holder.ivProduct).image(arraylist.get(position).getImage().get(0));
        }

       // holder.txtOwnerName.setVisibility(View.GONE);
        if (!adPost.getFull_name().isEmpty()) {
            holder.txtOwnerName.setText(adPost.getFull_name());
            //    holder.txtOwnerName.setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(appContext, 1), null, null, null);
        } else {
            holder.txtOwnerName.setText(appContext.getString(R.string.na));
        }

        holder.btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appContext instanceof ManageAdActivity) {
                    Intent intent = new Intent(appContext, PostAdActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((ManageAdActivity) appContext).startActivityForResult(intent, 404);

                }

                if (appContext instanceof ManageRentedActivity) {
                    Intent intent = new Intent(appContext, RentCarActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((ManageRentedActivity) appContext).startActivityForResult(intent, 404);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (appContext instanceof ManageAdActivity) {
                    Intent intent = new Intent(appContext, PostAdActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((ManageAdActivity) appContext).startActivityForResult(intent, 404);

                }

                if (appContext instanceof ManageRentedActivity) {
                    Intent intent = new Intent(appContext, RentCarActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((ManageRentedActivity) appContext).startActivityForResult(intent, 404);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCarName, txtCarPrice, txtCarModel, txtOwnerName, txtCarDate, txtRentLocation,txtType;
        CustomTextView txt_car_date;
        ImageView ivProduct;
        CustomRayMaterialTextView btnViewDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            btnViewDetail = (CustomRayMaterialTextView) itemView.findViewById(R.id.btn_view_detail);
            ivProduct = (ImageView) itemView.findViewById(R.id.iv_product);
            txtType = (TextView) itemView.findViewById(R.id.txtType);
            txtCarName = (TextView) itemView.findViewById(R.id.txt_car_name);
            txtCarPrice = (TextView) itemView.findViewById(R.id.txt_car_price);
            txtCarModel = (TextView) itemView.findViewById(R.id.txt_car_model);
            txtOwnerName = (TextView) itemView.findViewById(R.id.txt_owner_name);
            txtCarDate = (CustomTextView) itemView.findViewById(R.id.txt_car_date);
            txtCarDate.setVisibility(View.GONE);
            //txtRentLocation = (CustomTextView) itemView.findViewById(R.id.tv_post_ad_location);
        }
    }
}
