package com.spectre.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.spectre.R;
import com.spectre.activity.AddWorkActivity;
import com.spectre.activity.GarageDetailActivity;

import com.spectre.activity.HomeActivity;
import com.spectre.beans.AdPost;
import com.spectre.beans.Garage;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sumit on 9/25/2016.
 */
public class GarageHomeListAdapter extends RecyclerView.Adapter<GarageHomeListAdapter.ViewHolder> {

    private final ArrayList<Garage> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;

    public GarageHomeListAdapter(Context appContext, ArrayList<Garage> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_garage_detail_new, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Garage adPost = arraylist.get(position);

        if (adPost.getCompany_name() != null && !adPost.getCompany_name().isEmpty()) {
            holder.txt_name.setText(adPost.getCompany_name());
        } else {
            holder.txt_name.setText("");
        }

        if (!adPost.getAddress().isEmpty()) {
            holder.txt_address.setText(adPost.getAddress().trim());
        } else {
            holder.txt_address.setText("");
        }
        if (adPost.getFull_name() != null && !adPost.getExpertise().isEmpty()) {
            holder.txt_car_type.setText(adPost.getExpertise());
        } else {
            holder.txt_car_type.setText("");
        }
        if (!adPost.getMobile_no().isEmpty()) {
            String mobile = adPost.getMobile_code() + "" + adPost.getMobile_no().trim();
            holder.txt_number.setText(mobile);
        } else {
            holder.txt_number.setText("");
        }
        if (!adPost.getReviews().isEmpty()) {
            ArrayList<Integer> avg=new ArrayList<>();
            avg.clear();
            for(int i=0;i<adPost.getReviews().size();i++){
                avg.add(Integer.parseInt(adPost.getReviews().get(i).getRating()));
            }
           double dblavg=calculateAverage(avg);
           holder.txt_count_avg.setText(""+dblavg);
        } else {
            holder.txt_count_avg.setText(appContext.getString(R.string.na));
        }
        if (!adPost.getReviews().isEmpty()) {
            String reviewcount = "" +adPost.getReviews().size();
            holder.txt_review_count.setText(reviewcount);
        } else {
             holder.txt_review_count.setText("0");
        }


        if (adPost.getCompany_logo() != null && !adPost.getCompany_logo().isEmpty()) {
            new AQuery(appContext).id(holder.iv_product).image(adPost.getCompany_logo(), true, true, 300, R.drawable.ic_launcher_web);
        } else {
            if (status == 1) {
                holder.iv_product.setImageResource(R.drawable.ic_launcher_web);
            } else {
                holder.iv_product.setImageResource(R.drawable.ic_launcher_web);
            }
        }

    /*    holder.btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               *//* if (appContext instanceof GarageHomeActivity && status == 2) {
                    Intent intent = new Intent(appContext, AddWorkActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((GarageHomeActivity) appContext).startActivityForResult(intent, 404);
                }
*//*

                String s = SharedPrefUtils.getPreference(appContext, Constant.USER_TYPE, "");
                if (appContext instanceof HomeActivity && status == 1) {
                    Intent intent = new Intent(appContext, GarageDetailActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((HomeActivity) appContext).startActivityForResult(intent, 405);

                }
            }
        });*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               /* if (appContext instanceof GarageHomeActivity && status == 2) {
                    Intent intent = new Intent(appContext, AddWorkActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    ((GarageHomeActivity) appContext).startActivityForResult(intent, 404);
                }*/


                String s = SharedPrefUtils.getPreference(appContext, Constant.USER_TYPE, "");
                // if (appContext instanceof HomeActivity && status == 1) {
                if (status == 1) {
                    Intent intent = new Intent(appContext, GarageDetailActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    appContext.startActivity(intent);
                    //((HomeActivity) appContext).startActivityForResult(intent, 405);

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt_address, txt_number,txt_car_type;
        TextView txt_name, txt_review_count, txt_count_avg;
        ImageView iv_product;
        CustomRayMaterialTextView btn_save_changes;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_review_count = (TextView) itemView.findViewById(R.id.txt_review_count);
            txt_count_avg = (TextView) itemView.findViewById(R.id.txt_count_avg);
            txt_address = (CustomTextView) itemView.findViewById(R.id.txt_address);
            txt_number = (CustomTextView) itemView.findViewById(R.id.txt_number);
            txt_car_type = (CustomTextView) itemView.findViewById(R.id.txt_car_type);
            iv_product = (ImageView) itemView.findViewById(R.id.iv_product);
            //   btn_save_changes = (CustomRayMaterialTextView) itemView.findViewById(R.id.btn_save_changes);

            if (status == 1) {
                //txt_number.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.garage_call, 0, 0, 0);
                txt_number.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                txt_address.setMaxLines(2);
            } else {
                txt_number.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                txt_address.setMaxLines(1);
            }

        }
    }
    private double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}
