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
import com.spectre.activity.CarDetailActivity;
import com.spectre.beans.AdPost;
import com.spectre.other.Constant;

import java.util.ArrayList;


/**
 * Created by Sumit on 9/25/2016.
 */
public class SearchRentListAdapter extends RecyclerView.Adapter<SearchRentListAdapter.ViewHolder> {

    private final ArrayList<AdPost> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;
    private String perDay = "";

    public SearchRentListAdapter(Context appContext, ArrayList<AdPost> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
        perDay = appContext.getString(R.string.per_day);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rent_search_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AdPost adPost = arraylist.get(position);


        String nameModel = "", model = "";

        String owenarName="";
        if (!adPost.getFull_name().isEmpty()){
            owenarName=adPost.getFull_name().toString().trim();
            holder.txtOwnerName.setText(owenarName);
        }
        if (!adPost.getCar_name().isEmpty()) {
            nameModel = adPost.getCar_name().trim();
            //holder.txtCarName.setText(adPost.getCar_name());
        }

        if (!adPost.getModel().isEmpty()) {
            // nameModel = nameModel + " \u2022 " + adPost.getModel().trim();
            model = adPost.getModel().trim();
            //  holder.txtCarModel.setText(appContext.getString(R.string.model_) + " " + adPost.getModel().trim());
        }

        if (!nameModel.trim().isEmpty()) {
            holder.txtCarName.setText(nameModel);
        } else {
            holder.txtCarName.setText(appContext.getString(R.string.na));
        }

        if (!model.trim().isEmpty()) {
            holder.txtModel.setText(model);
        } else {
            holder.txtModel.setText(appContext.getString(R.string.na));
        }
        if (!adPost.getPrice().isEmpty()) {
            holder.txtCarPrice.setText(appContext.getString(R.string.dollar) + " " + adPost.getPrice().trim()+ " " + perDay);
        } else {
            holder.txtCarPrice.setText(appContext.getString(R.string.na));
        }
        if (!adPost.getVersion().isEmpty()) {
            holder.txt_car_type.setText(adPost.getVersion().trim());
        } else {
            holder.txt_car_type.setText(appContext.getString(R.string.na));
        }
        if (adPost.getYear_from()!=null&&!adPost.getYear_from().isEmpty()) {
            holder.txt_from_date.setText(adPost.getYear_from().trim());
        } else {
            holder.txt_from_date.setText(appContext.getString(R.string.na));
        }
        if (adPost.getYear_to()!=null&&!adPost.getYear_to().isEmpty()) {
            holder.txt_to_date.setText(adPost.getYear_to().trim());
        } else {
            holder.txt_to_date.setText(appContext.getString(R.string.na));
        }



        if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
            new AQuery(appContext).id(holder.ivProduct).image(arraylist.get(position).getImage().get(0), true, true, 300, R.drawable.ic_launcher_web);
          /*  Picasso.with(appContext)
                    .load(arraylist.get(position).getImage().get(0))
                    //  .transform(new CropSquareTransformation())
                    .into(holder.ivProduct);*/
        }


    /*    holder.ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arraylist.get(position).getImage() != null && arraylist.get(position).getImage().size() > 0) {
                    Intent intent = new Intent(appContext, ZoomActivity.class);
                    intent.putExtra(Constant.IMAGE, arraylist.get(position).getImage().get(0));
                    appContext.startActivity(intent);
                } else {

                }
            }
        });*/
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(appContext, CarDetailActivity.class);
            intent.putExtra(Constant.DATA, adPost);
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.TYPE, status);
            appContext.startActivity(intent);
        }
    });

    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_to_date,txtCarName, txtCarMileage,txtOwnerName, txt_from_date, txtModel,txtCarPrice,txt_car_type,txt_color;
        ImageView ivProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            txtCarName = itemView.findViewById(R.id.txt_car_name);
            txtModel = itemView.findViewById(R.id.txt_Model);
            txtCarPrice = itemView.findViewById(R.id.txt_car_price);
            txtCarMileage = itemView.findViewById(R.id.txt_car_mileage);
            txtOwnerName = itemView.findViewById(R.id.txt_owner_name);
            txt_from_date = itemView.findViewById(R.id.txt_from_date);
            txt_car_type=itemView.findViewById(R.id.txt_car_type);
            txt_to_date=itemView.findViewById(R.id.txt_to_date);
        }
    }
}
