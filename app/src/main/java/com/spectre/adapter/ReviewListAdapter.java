package com.spectre.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.spectre.R;
import com.spectre.beans.Review;
import com.spectre.beans.Work;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Sumit on 9/25/2016.
 */
public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private final ArrayList<Review> arraylist;
    private final Context appContext;
    private final int status;

    public ReviewListAdapter(Context appContext, ArrayList<Review> arraylist, int status) {
        this.arraylist = arraylist;
        this.appContext = appContext;
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        if (status == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_detail_, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_detail, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Review adPost = arraylist.get(position);

        if (!adPost.getUser_name().isEmpty()) {
            holder.txt_user_name.setText(adPost.getUser_name());
        } else {
            holder.txt_user_name.setText(appContext.getString(R.string.na));
        }

        if (!adPost.getReviews().isEmpty()) {
            holder.txt_review.setText(adPost.getReviews().trim());

            if (adPost.getReviews().trim().length() > 250 && !adPost.isOpen()) {
                addReadMoreComment(Html.fromHtml(adPost.getReviews()).toString().trim(), holder.txt_review, position, adPost, appContext, 250);
            }
        } else {
            holder.txt_review.setText(appContext.getString(R.string.na));
        }


        if (!adPost.getDate().isEmpty()) {
            holder.txt_date.setText(adPost.getDate().trim());
        } else {
            holder.txt_date.setText(appContext.getString(R.string.na));
        }

        if (!adPost.getRating().isEmpty()) {
            int i = Integer.parseInt(adPost.getRating());
            holder.rating.setRating(i);
        } else {
            holder.rating.setRating(0);
        }

        if (arraylist.get(position).getUser_image() != null && !arraylist.get(position).getUser_image().isEmpty()) {
            new AQuery(appContext).id(holder.iv_profile).image(arraylist.get(position).getUser_image(), true, true, 300, R.mipmap.gestuser);
          /*  Picasso.with(appContext)
                    .load(arraylist.get(position).getImage().get(0))
                    //  .transform(new CropSquareTransformation())
                    .into(holder.ivProduct);*/
        } else {
            holder.iv_profile.setImageResource(R.mipmap.gestuser);
        }

       /* holder.btnViewDetail.setOnClickListener(new View.OnClickListener() {
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
        });*/
    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView txt_user_name, txt_date, txt_review;
        RatingBar rating;
        CircleImageView iv_profile;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_user_name = (CustomTextView) itemView.findViewById(R.id.txt_user_name);
            txt_date = (CustomTextView) itemView.findViewById(R.id.txt_date);
            txt_review = (CustomTextView) itemView.findViewById(R.id.txt_review);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            iv_profile = (CircleImageView) itemView.findViewById(R.id.iv_profile);
        }
    }

    public void addReadMoreComment(final String text, final TextView textView, final int position, final Review reviewsFeedback, final Context appContext, final int counter) {
        SpannableString ss = new SpannableString(text.substring(0, counter) + "... Continue Reading");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                textView.setText(text);
                try {
                    reviewsFeedback.setOpen(true);
                    arraylist.set(position, reviewsFeedback);
                    notifyItemChanged(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(appContext.getResources().getColor(R.color.colorPrimary, appContext.getTheme()));
                } else {
                    ds.setColor(appContext.getResources().getColor(R.color.colorPrimary));
                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 17, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
