package com.spectre.activity_new;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.spectre.R;
import com.spectre.beans.AdPost;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.helper.Common;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookCarSummaryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    CustomTextView toolbarTitle;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbarActionbar;
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
    @BindView(R.id.booking_done)
    CustomTextView bookingDone;
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.gendertitle)
    CustomTextView gendertitle;
    @BindView(R.id.txtGender)
    CustomTextView txtGender;
    @BindView(R.id.rel_gender)
    RelativeLayout relGender;
    @BindView(R.id.buyer_locationtitle)
    CustomTextView buyerLocationtitle;
    @BindView(R.id.txtBuyer_location)
    CustomTextView txtBuyerLocation;
    @BindView(R.id.rel_locataion)
    RelativeLayout relLocataion;
    @BindView(R.id.summary_rent)
    LinearLayout linSummaryRent;
    @BindView(R.id.carseriestitle)
    CustomTextView carseriestitle;
    @BindView(R.id.txtCarSeries)
    CustomTextView txtCarSeries;
    @BindView(R.id.cartypetitle)
    CustomTextView cartypetitle;
    @BindView(R.id.txt_car_type)
    CustomTextView txtCarType;
    @BindView(R.id.colortitle)
    CustomTextView colortitle;
    @BindView(R.id.txt_color)
    CustomTextView txtColor;
    @BindView(R.id.milegtitle)
    CustomTextView milegtitle;
    @BindView(R.id.txt_mileg)
    CustomTextView txtMileg;
    @BindView(R.id.summary_buyer)
    LinearLayout linSummaryBuyer;
    @BindView(R.id.scrollView)
    ScrollView scrollView;


    private ActionBar actionBar;
    private Context context;
    private AdPost adPost;
    private int position = -1, type = -1;
    private String perDay = "";
    private Display display;
    private boolean isSuccess = false;
    RelativeLayout toolbar;
    String name="",location="",email="",phone="",gender="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_book_a_car_summary);

        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Summary</font>", true);

        toolbar = (RelativeLayout) findViewById(R.id.toolbar);

        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        setData();

    }

    @OnClick(R.id.btnConfirm)
    public void onViewClicked() {

        if (isSuccess == false) {
            callAPI("", "", "","");
           // scrollView.pageScroll(0);
           // scrollView.setVerticalScrollbarPosition(0);
        } else {
            Utility.contectDialog(adPost.getMobile_no(),BookCarSummaryActivity.this,adPost.getUser_id(),adPost.getFull_name());
           // getSuccessIntent();
        }

       /* Intent intent = new Intent(this, BookCarConfirmBooking.class);
         intent.putExtra(Constant.DATA, adPost);
         intent.putExtra(Constant.POSITION, position);
         intent.putExtra(Constant.TYPE, type);
        startActivity(intent);*/
    }

    private void setData() {
        type = getIntent().getExtras().getInt(Constant.TYPE);

        name=getIntent().getStringExtra(Constant.NAME);
        location=getIntent().getStringExtra(Constant.LOCATION);
        email=getIntent().getStringExtra(Constant.EMAIL);
        phone=getIntent().getStringExtra(Constant.PHONE);
        gender=getIntent().getStringExtra(Constant.GENDER);

        if (type != 1) {
            //    perDay = getString(R.string.per_day);
            linSummaryBuyer.setVisibility(View.VISIBLE);
            relGender.setVisibility(View.VISIBLE);
            relLocataion.setVisibility(View.VISIBLE);
            linSummaryRent.setVisibility(View.GONE);
        }
        txtPickUp.setText(Common.strDayPickUp + ", " + Common.strYearPickUp + " " + Common.strMonthPickUp + " " + Common.strDatePickUp);
        txtDropOff.setText(Common.strDayDropoff + ", " + Common.strYearDropoff + " " + Common.strMonthDropoff + " " + Common.strDateDropoff);
        txtName.setText(name);
        txtEmail.setText(email);
        txtPhone.setText(phone);
        txtBuyerLocation.setText(location);
        if (gender.equals("1")){
            txtGender.setText("male");
        }
        else {
            txtGender.setText("female");
        }


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
            /*set data for buy*/

           /* if (!adPost.getFull_name().isEmpty()) {
                txtName.setText(adPost.getFull_name().trim());
            } else {
                txtName.setText(context.getString(R.string.na));
            }
            if (!adPost.getEmail().isEmpty()) {
                txtEmail.setText(adPost.getEmail().trim());
            } else {
                txtEmail.setText(context.getString(R.string.na));
            }
            if (!adPost.getMobile_no().isEmpty()) {
                txtPhone.setText(adPost.getMobile_no().trim());
            } else {
                txtPhone.setText(context.getString(R.string.na));
            }
            if (!adPost.getLocation().isEmpty()) {
                txtLocation.setText(adPost.getLocation().trim());
            } else {
                txtLocation.setText(context.getString(R.string.na));
            }*/
            if (!adPost.getCar_name().isEmpty()) {
                txtCarName.setText(adPost.getCar_name().trim());
            } else {
                txtCarName.setText(context.getString(R.string.na));
            }
            if (!adPost.getModel().isEmpty()) {
                txtCarModel.setText(adPost.getModel().trim());
            } else {
                txtCarModel.setText(context.getString(R.string.na));
            }
            /*summary data*/
            if (!adPost.getVersion().isEmpty()) {
                txtCarSeries.setText(adPost.getVersion().trim());
            } else {
                txtLocation.setText(context.getString(R.string.na));
            }
            if (!adPost.getCar_type().isEmpty()) {
                txtCarType.setText(adPost.getCar_type().trim());
            } else {
                txtCarType.setText(context.getString(R.string.na));
            }
            if (!adPost.getColor().isEmpty()) {
                txtColor.setText(adPost.getColor().trim());
            } else {
                txtColor.setText(context.getString(R.string.na));
            }
            if (!adPost.getMileage().isEmpty()) {
                txtMileg.setText(adPost.getMileage().trim());
            } else {
                txtMileg.setText(context.getString(R.string.na));
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

    public void getSuccessIntent() {
        Intent intent = new Intent(context, HomeAct.class);
        startActivity(intent);
    }

    private void callAPI(String dialog, String trim, String from, String to) {
        MyDialogProgress.open(context);

        JSONObject js = new JSONObject();
        try {
            js.put(Constant.SECOND_USER_ID, adPost.getUser_id());
            js.put(Constant.INTERESTED_ID, adPost.getAdd_id());
            js.put(Constant.TYPE, type == 0 ? 1 : 2);
            js.put(Constant.PROBLEM, trim);
            js.put(Constant.FROM_DATE, from);
            js.put(Constant.TO_DATE, to);
            js.put(Constant.END_USER, SharedPrefUtils.getPreference(context, Constant.USER_ID, ""));
            js.put(Constant.GENDER,gender);
            js.put(Constant.NAME,name);
            js.put(Constant.LOCATION,location);
            js.put(Constant.PHONE,phone);
            js.put(Constant.EMAIL,email);

           /* {
                "second_user_id":"337",
                    "type":"3",
                    "interested_id":"0"
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AqueryCall(this).postWithJsonToken(Urls.INTEREST, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), js, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String success) {
                // showToast(success);
                MyDialogProgress.close(context);
               // isChange = true;
                //dd.dismiss();
                isSuccess = true;
                card1.setVisibility(View.VISIBLE);
                btnConfirm.setText("Finish");
                toolbar.setVisibility(View.GONE);

                adPost.setIs_interest("1");
               // getSuccessIntent();
                /*if (!adPost.getIs_interest().isEmpty() && adPost.getIs_interest().equalsIgnoreCase("1")) {
                    btn_show_interest.setText(R.string.request_sent);
                } else {
                    if (type == 1) {
                        btn_show_interest.setText(R.string.book_car);
                    } else {
                        btn_show_interest.setText(R.string.buy_car);
                    }

                }*/
                new AlertBox(context).openMessage(success, "Okay", "", false);
            }

            @Override
            public void onFailed(JSONObject js, String failed) {
                Utility.showToast(context, failed);

             if (failed.equalsIgnoreCase("already interested.")){
                 Utility.contectDialog(adPost.getMobile_no(),BookCarSummaryActivity.this,adPost.getUser_id(),adPost.getFull_name());
             }
                MyDialogProgress.close(context);


                //  showToast(failed);
            }

            @Override
            public void onAuthFailed(JSONObject js, String failed) {
                MyDialogProgress.close(context);
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String nullp) {

                if (nullp.equalsIgnoreCase("1")) {
                    //  showToast(getString(R.string.connection));
                    Utility.showToast(context, getString(R.string.connection));
                } else {
                    // showToast(getString(R.string.something_wrong));
                    Utility.showToast(context, getString(R.string.something_wrong));
                }

                MyDialogProgress.close(context);
            }

            @Override
            public void onException(JSONObject js, String exception) {
                // showToast(exception);
                Utility.showToast(context, exception);
                MyDialogProgress.close(context);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                Utility.showToast(context, inactive);
                MyDialogProgress.close(context);
            }
        });

        //  MyDialogProgress.close(context);
    }
}
