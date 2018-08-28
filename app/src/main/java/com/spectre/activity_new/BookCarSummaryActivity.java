package com.spectre.activity_new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.spectre.R;
import com.spectre.beans.AdPost;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomTextView;
import com.spectre.helper.Common;
import com.spectre.other.Constant;
import com.spectre.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookCarSummaryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    CustomTextView toolbarTitle;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbarActionbar;
    @BindView(R.id.driver_detail)
    CustomTextView driverDetail;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.nametitle)
    CustomTextView nametitle;
    @BindView(R.id.txtName)
    CustomTextView txtName;
    @BindView(R.id.emailtitle)
    CustomTextView emailtitle;
    @BindView(R.id.txtEmail)
    CustomTextView txtEmail;
    @BindView(R.id.phonetitle)
    CustomTextView phonetitle;
    @BindView(R.id.txtPhone)
    CustomTextView txtPhone;
    @BindView(R.id.summary)
    CustomTextView summary;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.txt_car_name)
    CustomTextView txtCarName;
    @BindView(R.id.txt_car_model)
    CustomTextView txtCarModel;
    @BindView(R.id.totalDays)
    CustomTextView totalDays;
    @BindView(R.id.txtTotalDays)
    CustomTextView txtTotalDays;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.pickupdatatitle)
    CustomTextView pickupdatatitle;
    @BindView(R.id.txt_pick_up)
    CustomTextView txtPickUp;
    @BindView(R.id.lin2)
    LinearLayout lin2;
    @BindView(R.id.dropuptitle)
    CustomTextView dropuptitle;
    @BindView(R.id.txt_drop_off)
    CustomTextView txtDropOff;
    @BindView(R.id.lin3)
    LinearLayout lin3;
    @BindView(R.id.locationtitle)
    CustomTextView locationtitle;
    @BindView(R.id.txt_location)
    CustomTextView txtLocation;
    @BindView(R.id.lin4)
    LinearLayout lin4;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.totaltitle)
    CustomTextView totaltitle;
    @BindView(R.id.txt_total)
    CustomTextView txtTotal;
    @BindView(R.id.lin5)
    LinearLayout lin5;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    private ActionBar actionBar;
    private Context context;
    private AdPost adPost;
    private int position = -1, type = -1;
    private String perDay = "";
    private Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_book_a_car_summary);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Summary</font>", true);
        ButterKnife.bind(this);
        initView();
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, BookCarSummaryActivity.class);
        return intent;
    }

    private void initView() {
       setData();
    }

    @OnClick(R.id.btnConfirm)
    public void onViewClicked() {
        Intent intent = new Intent(this, BookCarConfirmBooking.class);
         intent.putExtra(Constant.DATA, adPost);
         intent.putExtra(Constant.POSITION, position);
         intent.putExtra(Constant.TYPE, type);
        startActivity(intent);
    }

    private void setData(){
        txtPickUp.setText(Common.strDayPickUp+", "+Common.strYearPickUp+" "+Common.strMonthPickUp+" "+Common.strDatePickUp);
        txtDropOff.setText(Common.strDayDropoff+", "+Common.strYearDropoff+" "+Common.strMonthDropoff+" "+Common.strDateDropoff);

        txtName.setText(getIntent().getStringExtra(Constant.DRIVER_NAME));
        txtEmail.setText(getIntent().getStringExtra(Constant.DRIVER_EMAIL));
        txtPhone.setText(getIntent().getStringExtra(Constant.DRIVER_PHONE));

        txtLocation.setText(Common.location);

        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {

            adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
            //    actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + adPost.getCar_name() + "</font>"));
            position = getIntent().getExtras().getInt(Constant.POSITION);
            type = getIntent().getExtras().getInt(Constant.TYPE);

            if (type == 1) {
                perDay = getString(R.string.per_day);
            }
            if (!adPost.getPrice().isEmpty()) {
                txtTotal.setText(context.getString(R.string.dollar) + " " + adPost.getPrice().trim());
            } else {
                txtTotal.setText(context.getString(R.string.na));
            }
         /* if (adPost.getYear_from() != null && !adPost.getYear_from().isEmpty()) {
              txtFrom.setText(adPost.getYear_from().trim());

          } else {
              txtFrom.setText(context.getString(R.string.na));
          }
          if (adPost.getYear_to() != null && !adPost.getYear_to().isEmpty()) {
              txtTo.setText(adPost.getYear_to().trim());

          } else {
              txtTo.setText(context.getString(R.string.na));
          }*/

            display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);


        } else {
            new AlertBox(context).openMessageWithFinish(getResources().getString(R.string.something_wrong), "Okay", "", false);
        }


    }
}
