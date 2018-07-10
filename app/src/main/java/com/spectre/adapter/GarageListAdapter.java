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

import com.androidquery.AQuery;
import com.spectre.R;
import com.spectre.activity.AddWorkActivity;
import com.spectre.activity.GarageDetailActivity;

import com.spectre.activity.HomeActivity;
import com.spectre.activity.ManageRentedActivity;
import com.spectre.activity.ManageWorkActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.beans.AdPost;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;
import com.spectre.utility.Utility;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Sumit on 9/25/2016.
 */
public class GarageListAdapter extends RecyclerView.Adapter<GarageListAdapter.ViewHolder> {

    private final ArrayList<AdPost> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;

    public GarageListAdapter(Context appContext, ArrayList<AdPost> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_garage_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AdPost adPost = arraylist.get(position);
        if (status == 2) {
            if (!adPost.getCar_name().isEmpty()) {
                holder.txt_name.setText(adPost.getCar_name());
            } else {
                holder.txt_name.setText(appContext.getString(R.string.na));
            }

            if (!adPost.getModel().isEmpty()) {
                holder.txt_address.setText(appContext.getString(R.string.model_) + " " + adPost.getModel().trim());
            } else {
                holder.txt_address.setText(appContext.getString(R.string.na));
            }

            if (!adPost.getProblem().isEmpty()) {
                holder.txt_number.setText(adPost.getProblem().trim());
            } else {
                holder.txt_number.setText(appContext.getString(R.string.na));
            }
        } else {

            if (adPost.getFull_name() != null && !adPost.getFull_name().isEmpty()) {
                holder.txt_name.setText(adPost.getFull_name());
            } else {
                holder.txt_name.setText(appContext.getString(R.string.na));
            }

            if (!adPost.getAddress().isEmpty()) {
                holder.txt_address.setText(adPost.getAddress().trim());
            } else {
                holder.txt_address.setText(appContext.getString(R.string.na));
            }

            if (!adPost.getMobile_no().isEmpty()) {
                holder.txt_number.setText(adPost.getMobile_no().trim());
            } else {
                holder.txt_number.setText(appContext.getString(R.string.na));
            }
        }

        if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
            new AQuery(appContext).id(holder.iv_product).image(arraylist.get(position).getImage().get(0));
        } else {
            /*if(status == 1){
                holder.iv_product.setImageResource(R.drawable.garage_default);
            }else{
                holder.iv_product.setImageResource(R.drawable.default_image);
            }*/
            holder.iv_product.setImageResource(R.drawable.ic_launcher_web);
        }

        holder.btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (appContext instanceof GarageHomeActivity && status == 2) {
                if (appContext instanceof ManageWorkActivity && status == 2) {
                    Intent intent = new Intent(appContext, AddWorkActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                   // ((GarageHomeActivity) appContext).startActivityForResult(intent, 404);
                    ((ManageWorkActivity) appContext).startActivityForResult(intent, 404);
                }


                if (appContext instanceof HomeActivity && status == 1) {
                    Intent intent = new Intent(appContext, GarageDetailActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((HomeActivity) appContext).startActivityForResult(intent, 404);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (appContext instanceof GarageHomeActivity && status == 2) {
                if (appContext instanceof ManageWorkActivity && status == 2) {
                    Intent intent = new Intent(appContext, AddWorkActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                   // ((GarageHomeActivity) appContext).startActivityForResult(intent, 404);
                    ((ManageWorkActivity) appContext).startActivityForResult(intent, 404);
                }


                if (appContext instanceof HomeActivity && status == 1) {
                    Intent intent = new Intent(appContext, GarageDetailActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((HomeActivity) appContext).startActivityForResult(intent, 404);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt_name, txt_address, txt_number;
        ImageView iv_product;
        CustomRayMaterialTextView btn_save_changes;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (CustomTextView) itemView.findViewById(R.id.txt_name);
            txt_address = (CustomTextView) itemView.findViewById(R.id.txt_address);
            txt_number = (CustomTextView) itemView.findViewById(R.id.txt_number);
            iv_product = (ImageView) itemView.findViewById(R.id.iv_product);
            btn_save_changes = (CustomRayMaterialTextView) itemView.findViewById(R.id.btn_save_changes);

            if (status == 1) {
                txt_number.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.garage_call, 0, 0, 0);
                txt_address.setMaxLines(2);
            } else {
                txt_number.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                txt_address.setMaxLines(1);
            }

        }
    }
}
