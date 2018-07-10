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
import com.spectre.activity.CarDetailActivity;
import com.spectre.activity.HomeActivity;
import com.spectre.activity.ManageAdActivity;
import com.spectre.activity.ManageRentedActivity;
import com.spectre.activity.PostAdActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.activity.ZoomActivity;
import com.spectre.beans.AdPost;
import com.spectre.customView.CropSquareTransformation;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;
import com.spectre.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Sumit on 9/25/2016.
 */
public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.ViewHolder> {

    private final ArrayList<AdPost> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;
    private String perDay = "";

    public CarListAdapter(Context appContext, ArrayList<AdPost> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status; /*Buy 0, Rent 1*/
        if (status == 1) {
            perDay = appContext.getString(R.string.per_day);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_detail, parent, false);
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
            //   holder.txtCarName.setText(adPost.getCar_name());
        }

        if (!adPost.getModel().isEmpty()) {
            nameModel = nameModel + " \u2022 " + adPost.getModel().trim();
            //  holder.txtCarModel.setText(appContext.getString(R.string.model_) + " " + adPost.getModel().trim());
        }


        if (!nameModel.trim().isEmpty()) {
            holder.txtCarName.setText(nameModel);
        } else {
            holder.txtCarName.setText(appContext.getString(R.string.na));
        }

        if (status != 1) {
            if (!adPost.getYear().isEmpty()) {
                yearMileage = adPost.getYear().trim();
                //   holder.txtCarName.setText(adPost.getCar_name());
            }

            if (!adPost.getMileage().isEmpty()) {
                yearMileage = yearMileage + " \u2022 " + adPost.getMileage().trim() + " " + appContext.getString(R.string.miles);
                //   holder.txtCarName.setText(adPost.getCar_name());
            }

            if (!yearMileage.trim().isEmpty()) {
                holder.txtCarModel.setText(yearMileage);
            } else {
                holder.txtCarModel.setText(appContext.getString(R.string.na));
            }

            holder.txtCarModel.setVisibility(View.VISIBLE);
        } else {
            holder.txtCarModel.setVisibility(View.GONE);
        }


        if (!adPost.getPrice().isEmpty()) {
            holder.txtCarPrice.setText(appContext.getString(R.string.dollar) + " " + adPost.getPrice().trim() + "" + perDay);
        } else {
            holder.txtCarPrice.setText(appContext.getString(R.string.na));
        }


        if (!adPost.getCreate_date().isEmpty()) {
            holder.txtCarDate.setText(adPost.getCreate_date().trim());
        } else {
            holder.txtCarDate.setText(appContext.getString(R.string.na));
        }

        if (appContext instanceof HomeActivity) {
            if (!adPost.getFull_name().isEmpty()) {
                holder.txtOwnerName.setText(adPost.getFull_name());
                //    holder.txtOwnerName.setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(appContext, 1), null, null, null);
            } else {
                holder.txtOwnerName.setText(appContext.getString(R.string.na));
            }
        } else {
            holder.txtOwnerName.setVisibility(View.GONE);
        }

        if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
            new AQuery(appContext).id(holder.ivProduct).image(arraylist.get(position).getImage().get(0), true, true, 300, R.drawable.ic_launcher_web);
          /*  Picasso.with(appContext)
                    .load(arraylist.get(position).getImage().get(0))
                    //  .transform(new CropSquareTransformation())
                    .into(holder.ivProduct);*/
        }


        holder.btnViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appContext instanceof HomeActivity) {
                    Intent intent = new Intent(appContext, CarDetailActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    intent.putExtra(Constant.TYPE, status);
                    ((HomeActivity) appContext).startActivityForResult(intent, 404);
                }
                String s = Utility.getSharedPreferences(appContext, Constant.USER_TYPE);
               /* if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("2")) {
                    if (appContext instanceof HomeActivity) {
                        Intent intent = new Intent(appContext, CarDetailActivity.class);
                        intent.putExtra(Constant.DATA, adPost);
                        intent.putExtra(Constant.POSITION, position);
                        intent.putExtra(Constant.TYPE, status);
                        ((HomeActivity) appContext).startActivityForResult(intent, 404);
                    }
                } else {
                    ((HomeActivity) appContext).openDialogToLogin();
                   *//* if (appContext instanceof HomeActivity) {
                        Intent intent = new Intent(appContext, CarDetailActivity.class);
                        intent.putExtra(Constant.DATA, adPost);
                        intent.putExtra(Constant.POSITION, position);
                        intent.putExtra(Constant.TYPE, status);
                        ((HomeActivity) appContext).startActivityForResult(intent, 404);
                    }*//*

                }*/
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appContext instanceof HomeActivity) {
                    Intent intent = new Intent(appContext, CarDetailActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    intent.putExtra(Constant.TYPE, status);
                    ((HomeActivity) appContext).startActivityForResult(intent, 404);
                }
                String s = Utility.getSharedPreferences(appContext, Constant.USER_TYPE);
               /* if (s.equalsIgnoreCase("1") || s.equalsIgnoreCase("2")) {
                    if (appContext instanceof HomeActivity) {
                        Intent intent = new Intent(appContext, CarDetailActivity.class);
                        intent.putExtra(Constant.DATA, adPost);
                        intent.putExtra(Constant.POSITION, position);
                        intent.putExtra(Constant.TYPE, status);
                        ((HomeActivity) appContext).startActivityForResult(intent, 404);
                    }
                } else {
                    ((HomeActivity) appContext).openDialogToLogin();
                   *//* if (appContext instanceof HomeActivity) {
                        Intent intent = new Intent(appContext, CarDetailActivity.class);
                        intent.putExtra(Constant.DATA, adPost);
                        intent.putExtra(Constant.POSITION, position);
                        intent.putExtra(Constant.TYPE, status);
                        ((HomeActivity) appContext).startActivityForResult(intent, 404);
                    }*//*

                }*/
            }
        });

        holder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
                    Intent intent = new Intent(appContext, ZoomActivity.class);
                    intent.putExtra(Constant.IMAGE, arraylist.get(position).getImage().get(0));
                    appContext.startActivity(intent);
                } else {

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txtCarName, txtCarModel, txtCarPrice, txtOwnerName,txtCarDate;
        ImageView ivProduct;
        CustomRayMaterialTextView btnViewDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            btnViewDetail = (CustomRayMaterialTextView) itemView.findViewById(R.id.btn_view_detail);
            ivProduct = (ImageView) itemView.findViewById(R.id.iv_product);
            txtCarName = (CustomTextView) itemView.findViewById(R.id.txt_car_name);
            txtCarPrice = (CustomTextView) itemView.findViewById(R.id.txt_car_price);
            txtCarModel = (CustomTextView) itemView.findViewById(R.id.txt_car_model);
            txtOwnerName = (CustomTextView) itemView.findViewById(R.id.txt_owner_name);
            txtCarDate = (CustomTextView) itemView.findViewById(R.id.txt_car_date);
        }
    }
}
