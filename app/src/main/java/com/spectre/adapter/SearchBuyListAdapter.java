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
import com.spectre.activity.ManageAdActivity;
import com.spectre.activity.ManageRentedActivity;
import com.spectre.activity.PostAdActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.activity_new.HomeAct;
import com.spectre.beans.AdPost;
import com.spectre.other.Constant;

import java.util.ArrayList;


/**
 * Created by Sumit on 9/25/2016.
 */
public class SearchBuyListAdapter extends RecyclerView.Adapter<SearchBuyListAdapter.ViewHolder> {

    private final ArrayList<AdPost> arraylist;
    private final Context appContext;
    private final int status;
    private String TAG = "";
    private Dialog dialog;
    private String perDay = "";

    public SearchBuyListAdapter(Context appContext, ArrayList<AdPost> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
        if (status == 1) {
            perDay = appContext.getString(R.string.per_day);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buy_search_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AdPost adPost = arraylist.get(position);


        String nameModel = "", model = "";
        String yearMileage = "";
        String mileg="";
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
            holder.txtCarPrice.setText(appContext.getString(R.string.dollar) + " " + adPost.getPrice().trim());
        } else {
            holder.txtCarPrice.setText(appContext.getString(R.string.na));
        }
        if (!adPost.getCar_type().isEmpty()) {
            holder.txt_car_type.setText(adPost.getCar_type().trim());
        } else {
            holder.txt_car_type.setText(appContext.getString(R.string.na));
        }
        if (!adPost.getColor().isEmpty()) {
            holder.txt_color.setText(adPost.getColor().trim());
        } else {
            holder.txt_color.setText(appContext.getString(R.string.na));
        }
       /* if (status != 1) {*/
            if (!adPost.getYear().isEmpty()) {
                yearMileage = adPost.getYear().trim();
                //   holder.txtCarName.setText(adPost.getCar_name());
            }

            if (!adPost.getMileage().isEmpty()) {
                mileg = adPost.getMileage().trim() + " " + appContext.getString(R.string.miles);
                //   holder.txtCarName.setText(adPost.getCar_name());
            }

            if (!yearMileage.trim().isEmpty()) {
                holder.txtCarDate.setText(yearMileage);
            } else {
                holder.txtCarDate.setText(appContext.getString(R.string.na));
            }
            if (!mileg.trim().isEmpty()) {
                holder.txtCarMileage.setText(mileg);
            } else {
                holder.txtCarMileage.setText(appContext.getString(R.string.na));
            }
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (appContext instanceof HomeAct) {
            Intent intent = new Intent(appContext, CarDetailActivity.class);
            intent.putExtra(Constant.DATA, adPost);
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.TYPE, status);
            appContext.startActivity(intent);
        }
        else   if (appContext instanceof ManageAdActivity) {
            Intent intent = new Intent(appContext, PostAdActivity.class);
            intent.putExtra(Constant.DATA, adPost);
            intent.putExtra(Constant.POSITION, position);
            appContext.startActivity(intent);
          //  ((ManageAdActivity) appContext).startActivityForResult(intent, 404);

        }

    }
});

       // }

      /*  if (!adPost.getCreate_date().isEmpty()) {
            holder.txtCarDate.setText(adPost.getCreate_date().trim());
        } else {
            holder.txtCarDate.setText(appContext.getString(R.string.na));
        }*/

        /*if (appContext instanceof HomeAct) {
            if (!adPost.getFull_name().isEmpty()) {
                holder.txtOwnerName.setText(adPost.getFull_name());
                //    holder.txtOwnerName.setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(appContext, 1), null, null, null);
            } else {
                holder.txtOwnerName.setText(appContext.getString(R.string.na));
            }
        } else {
            holder.txtOwnerName.setVisibility(View.GONE);
        }*/

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

    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCarName, txtCarMileage,txtOwnerName, txtCarDate, txtModel,txtCarPrice,txt_car_type,txt_color;
        ImageView ivProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            txtCarName = itemView.findViewById(R.id.txt_car_name);
            txtModel = itemView.findViewById(R.id.txt_Model);
            txtCarPrice = itemView.findViewById(R.id.txt_car_price);
            txtCarMileage = itemView.findViewById(R.id.txt_car_mileage);
            txtOwnerName = itemView.findViewById(R.id.txt_owner_name);
            txtCarDate = itemView.findViewById(R.id.txt_year);
            txt_car_type=itemView.findViewById(R.id.txt_car_type);
            txt_color=itemView.findViewById(R.id.txt_color);
        }
    }
}
