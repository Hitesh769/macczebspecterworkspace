package com.spectre.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.daimajia.slider.library.SliderLayout;
import com.spectre.R;
import com.spectre.beans.AdPost;
import com.spectre.beans.Work;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;
import com.spectre.utility.Utility;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkDetailActivity extends AppCompatActivity {

    private Context context;
    private ActionBar actionBar;

    private SliderLayout imageSlider;
    private CustomTextView txt_car_name, txt_car_price, txt_car_model, txt_car_version,
            txt_car_type, txt_car_mileage, txt_modify, txt_problem;
    private CircleImageView iv_profile;
    private CustomRayMaterialTextView btn_show_interest;
    private Display display;
    private Work adPost;
    private int position = -1, type = -1;
    HashMap<String, String> url_from_api = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_work_detail);
        context = this;
        Utility.setContentView(context, R.layout.activity_work_detail);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Work Detail</font>", true);
        initView();
    }

    private void initView() {
        imageSlider = (SliderLayout) findViewById(R.id.slider);
        txt_car_name = (CustomTextView) findViewById(R.id.txt_car_name);
        txt_car_price = (CustomTextView) findViewById(R.id.txt_car_price);
        txt_car_model = (CustomTextView) findViewById(R.id.txt_car_model);
        txt_car_version = (CustomTextView) findViewById(R.id.txt_car_version);
        txt_car_type = (CustomTextView) findViewById(R.id.txt_car_type);
        txt_car_mileage = (CustomTextView) findViewById(R.id.txt_car_mileage);
        txt_modify = (CustomTextView) findViewById(R.id.txt_modify);
        txt_problem = (CustomTextView) findViewById(R.id.txt_problem);


        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {

            adPost = (Work) getIntent().getExtras().get(Constant.DATA);
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + adPost.getCar_name() + "</font>"));
            position = getIntent().getExtras().getInt(Constant.POSITION);
            type = getIntent().getExtras().getInt(Constant.TYPE);


            display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            android.view.ViewGroup.LayoutParams layoutParams = imageSlider.getLayoutParams();
            layoutParams.width = display.getWidth();
            layoutParams.height = Utility.dpToPx(context, 250);
            imageSlider.setLayoutParams(layoutParams);

            if (adPost.getCar_name() != null && !adPost.getCar_name().isEmpty())
                txt_car_name.setText(adPost.getCar_name());
            else
                txt_car_name.setText(getString(R.string.na));


            if (adPost.getPrice() != null && !adPost.getPrice().isEmpty()) {
                //  txt_car_price.setText(context.getString(R.string.dollar) + " " + adPost.getPrice().trim());
                txt_car_price.setText(getString(R.string.dollar)+" "+adPost.getPrice().trim());
            } else {
                txt_car_price.setText(context.getString(R.string.na));
            }

            if (adPost.getModel() != null && !adPost.getModel().isEmpty()) {
                txt_car_model.setText(adPost.getModel().trim());
            } else {
                txt_car_model.setText(context.getString(R.string.na));
            }

            if (adPost.getVersion() != null && !adPost.getVersion().isEmpty()) {
                txt_car_version.setText(adPost.getVersion().trim());
            } else {
                txt_car_version.setText(context.getString(R.string.na));
            }

            if (adPost.getCar_type() != null && !adPost.getCar_type().isEmpty()) {
                txt_car_type.setText(adPost.getCar_type().trim());
            } else {
                txt_car_type.setText(context.getString(R.string.na));
            }

            if (adPost.getMileage() != null && !adPost.getMileage().isEmpty()) {
                txt_car_mileage.setText(adPost.getMileage().trim());
            } else {
                txt_car_mileage.setText(context.getString(R.string.na));
            }

            if (adPost.getCar_modified() != null && !adPost.getCar_modified().isEmpty()) {
                txt_modify.setText(adPost.getCar_modified().trim().equalsIgnoreCase("0") ? getString(R.string.no) : getString(R.string.yes));
            } else {
                txt_modify.setText(context.getString(R.string.na));
            }

            if (adPost.getProblem() != null && !adPost.getProblem().isEmpty()) {
                txt_problem.setText(adPost.getProblem().trim().equalsIgnoreCase("0") ? getString(R.string.no) : getString(R.string.yes));
            } else {
                txt_problem.setText(context.getString(R.string.na));
            }


            if (adPost.getImage().size() == 0) {
                Utility.setUpViewPagerNew(imageSlider, context);
            } else {
                for (String s : adPost.getImage()) {
                    url_from_api.put(s, s);
                }
                Utility.setUpViewPager(imageSlider, context, url_from_api, "0");
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
