package com.spectre.activity_new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.bumptech.glide.util.Util;
import com.daimajia.slider.library.SliderLayout;
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


public class BookCarInfoActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    CustomTextView toolbarTitle;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbarActionbar;
    @BindView(R.id.vendor_detail)
    CustomTextView vendorDetail;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.txt_from)
    CustomTextView txtFrom;
    @BindView(R.id.txt_to)
    CustomTextView txtTo;
    @BindView(R.id.changedate)
    LinearLayout changedate;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.location)
    CustomTextView location;
    @BindView(R.id.changelocation)
    LinearLayout changelocation;
    @BindView(R.id.driver_detail)
    CustomTextView driverDetail;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.input_first_name)
    EditText inputFirstName;
    @BindView(R.id.input_layout_first_name)
    TextInputLayout inputLayoutFirstName;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.input_layout_phone)
    TextInputLayout inputLayoutPhone;
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
        Utility.setContentView(context, R.layout.activity_book_a_car_information);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Information</font>", true);
        ButterKnife.bind(this);
        initView();
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, BookCarInfoActivity.class);
        return intent;
    }

    private void initView() {
             setData();
    }

    @OnClick(R.id.btnConfirm)
    public void onViewClicked() {
        nextActivity();
    }

    private void nextActivity() {
        if (inputFirstName.getText().toString().isEmpty()){
            new AlertBox(context).openMessageWithFinish(getResources().getString(R.string.nameempty), "Okay", "", false);
        }
        else if (inputEmail.getText().toString().isEmpty()){
            new AlertBox(context).openMessageWithFinish(getResources().getString(R.string.emailempty), "Okay", "", false);
        }
        else if (inputPhone.getText().toString().isEmpty()){
            new AlertBox(context).openMessageWithFinish(getResources().getString(R.string.phoneempty), "Okay", "", false);
        }
        else{
            Intent intent = new Intent(this, BookCarSummaryActivity.class);
            intent.putExtra(Constant.DATA, adPost);
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.TYPE, type);
            intent.putExtra(Constant.DRIVER_EMAIL,inputEmail.getText().toString());
            intent.putExtra(Constant.DRIVER_NAME,inputFirstName.getText().toString());
            intent.putExtra(Constant.DRIVER_PHONE,inputPhone.getText().toString());
            startActivity(intent);
        }
    }

    private void setData(){
      txtFrom.setText(Common.strDayPickUp+", "+Common.strYearPickUp+" "+Common.strMonthPickUp+" "+Common.strDatePickUp);
      txtTo.setText(Common.strDayDropoff+", "+Common.strYearDropoff+" "+Common.strMonthDropoff+" "+Common.strDateDropoff);
      //location.setText(Common.location);

      if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {

          adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
      //    actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + adPost.getCar_name() + "</font>"));
          position = getIntent().getExtras().getInt(Constant.POSITION);
          type = getIntent().getExtras().getInt(Constant.TYPE);

          if (type == 1) {
              perDay = getString(R.string.per_day);
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
