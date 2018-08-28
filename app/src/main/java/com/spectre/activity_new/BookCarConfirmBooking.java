package com.spectre.activity_new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
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

public class BookCarConfirmBooking extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    CustomTextView toolbarTitle;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbarActionbar;
    @BindView(R.id.booking_done)
    CustomTextView bookingDone;
    @BindView(R.id.txtCarBrand)
    CustomTextView txtCarBrand;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.txt_car_model)
    CustomTextView txtCarModel;
    @BindView(R.id.lin2)
    LinearLayout lin2;
    @BindView(R.id.txt_car_price)
    CustomTextView txtCarPrice;
    @BindView(R.id.lin3)
    LinearLayout lin3;
    @BindView(R.id.txt_car_version)
    CustomTextView txtCarVersion;
    @BindView(R.id.lin4)
    LinearLayout lin4;
    @BindView(R.id.txt_car_series)
    CustomTextView txtCarSeries;
    @BindView(R.id.lin5)
    LinearLayout lin5;
    @BindView(R.id.txt_posted_on)
    CustomTextView txtPostedOn;
    @BindView(R.id.lin6)
    LinearLayout lin6;
    @BindView(R.id.pickupdatatitle)
    CustomTextView pickupdatatitle;
    @BindView(R.id.txt_pick_up)
    CustomTextView txtPickUp;
    @BindView(R.id.lin7)
    LinearLayout lin7;
    @BindView(R.id.dropuptitle)
    CustomTextView dropuptitle;
    @BindView(R.id.txt_drop_off)
    CustomTextView txtDropOff;
    @BindView(R.id.lin8)
    LinearLayout lin8;
    @BindView(R.id.btnFinish)
    Button btnFinish;
    private Context context;
    private ActionBar actionBar;
    private AdPost adPost;
    private int position = -1, type = -1;
    private String perDay = "";
    private Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_confirm_booking);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Booking Complete</font>", true);
        initView();
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, BookCarConfirmBooking.class);
        return intent;
    }

    private void initView() {
      //  setData();
    }

    private void setData() {
       /* txtPickUp.setText(Common.strDayPickUp + ", " + Common.strYearPickUp + " " + Common.strMonthPickUp + " " + Common.strDatePickUp);
        txtDropOff.setText(Common.strDayDropoff + ", " + Common.strYearDropoff + " " + Common.strMonthDropoff + " " + Common.strDateDropoff);
*/
        //location.setText(Common.location);

        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {

            adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
            //    actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + adPost.getCar_name() + "</font>"));
            position = getIntent().getExtras().getInt(Constant.POSITION);
            type = getIntent().getExtras().getInt(Constant.TYPE);


            if (adPost.getPrice() != null && !adPost.getPrice().isEmpty()) {
                txtCarPrice.setText(getString(R.string.dollar)+" "+adPost.getPrice().trim());

            } else {
                txtCarPrice.setText(context.getString(R.string.na));
            }
            if (adPost.getVersion() != null && !adPost.getVersion().isEmpty()) {
                txtCarVersion.setText(adPost.getVersion());

            } else {
                txtCarVersion.setText(context.getString(R.string.na));
            }

            if (type == 1) {
                perDay = getString(R.string.per_day);
            }

            display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);


        } else {
            new AlertBox(context).openMessageWithFinish(getResources().getString(R.string.something_wrong), "Okay", "", false);
        }


    }

}
