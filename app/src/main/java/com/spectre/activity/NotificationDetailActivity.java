package com.spectre.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.daimajia.slider.library.SliderLayout;
import com.google.gson.Gson;
import com.spectre.R;
import com.spectre.beans.AdPost;
import com.spectre.beans.NotificationListUser;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationDetailActivity extends AppCompatActivity {

    private Context context;
    private ActionBar actionBar;
    private SliderLayout imageSlider;
    private CircleImageView iv_profile;
    //   private ActionBar txt_problem;
    private CustomTextView txt_car_name, txt_car_price, txt_car_model, txt_car_version,
            txt_car_type, txt_car_mileage, txt_email_id, txt_vendor_name, txt_adress,
            txt_contact, txt_from, txt_to, txt_problem, txt_message, txt_pick_up, txt_drop_off;
    private CardView card_problem, card_duration;
    private String type;
    private NotificationListUser notificationListUser;
    private AdPost adPost;
    private Display display;
    HashMap<String, String> url_from_api = new HashMap<String, String>();
    private AlertBox alertBox;
    private String perDay = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_notification_detail);
        context = this;
        Utility.setContentView(context, R.layout.activity_notification_detail);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Notification Detail</font>", true);
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
        txt_email_id = (CustomTextView) findViewById(R.id.txt_email_id);
        txt_pick_up = (CustomTextView) findViewById(R.id.txt_pick_up);
        txt_drop_off = (CustomTextView) findViewById(R.id.txt_drop_off);
        txt_message = (CustomTextView) findViewById(R.id.txt_message);
        txt_vendor_name = (CustomTextView) findViewById(R.id.txt_vendor_name);
        txt_adress = (CustomTextView) findViewById(R.id.txt_adress);
        txt_contact = (CustomTextView) findViewById(R.id.txt_contact);
        txt_to = (CustomTextView) findViewById(R.id.txt_to);
        txt_from = (CustomTextView) findViewById(R.id.txt_from);
        txt_problem = (CustomTextView) findViewById(R.id.txt_problem);
        iv_profile = (CircleImageView) findViewById(R.id.iv_profile);
        card_problem = (CardView) findViewById(R.id.card_problem);
        card_duration = (CardView) findViewById(R.id.card_duration);

        alertBox = new AlertBox(context);
        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {
            notificationListUser = getIntent().getParcelableExtra(Constant.DATA);
            type = notificationListUser.getType();
            if (type.equalsIgnoreCase("2")) {
                perDay = getString(R.string.per_day);
            }
        }
        callApi();
        // setData();

    }

    private void callApi() {
        MyDialogProgress.open(context);
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.NOTIFICATION_ID, notificationListUser.getNotification_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Urls.NOTIFICATION_DETAIL_BY_ID, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                //  setData(js, msg);
                try {
                    adPost = new Gson().fromJson(js.getJSONObject("data").toString(), AdPost.class);
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                // Utility.showToast(context, msg);
                alertBox.openMessageWithFinish(msg, "Ok", "", false);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                alertBox.openMessageWithFinish(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                alertBox.openMessageWithFinish(msg, "Ok", "", false);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {

            }
        });
    }

    private void setData() {
        //  adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);

        try {
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(Integer.parseInt(notificationListUser.getNotification_id()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyDialogProgress.close(context);
        if (!adPost.getCar_name().isEmpty())
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + adPost.getCar_name() + "</font>"));


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
            txt_car_price.setText(adPost.getPrice().trim() + " " + getString(R.string.dollar) + "" + perDay);
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


     /*   if (adPost.getEmail() != null && !adPost.getEmail().isEmpty()) {
            txt_email_id.setText(adPost.getEmail().trim());
        } else {
            txt_email_id.setText(context.getString(R.string.na));
        }


        if (adPost.getFull_name() != null && !adPost.getFull_name().isEmpty()) {
            txt_vendor_name.setText(adPost.getFull_name().trim());
        } else {
            txt_vendor_name.setText(context.getString(R.string.na));
        }


        if (adPost.getMobile_no() != null && !adPost.getMobile_no().isEmpty()) {
            txt_contact.setText(adPost.getMobile_no().trim());
        } else {
            txt_contact.setText(context.getString(R.string.na));
        }

        if (adPost.getAddress() != null && !adPost.getAddress().isEmpty()) {
            txt_adress.setText(adPost.getAddress().trim());
        } else {
            txt_adress.setText(context.getString(R.string.na));
        }*/

        if (adPost.getEmail() != null && !adPost.getEmail().isEmpty()) {
            txt_email_id.setText(adPost.getEmail().trim());
            ((CustomTextView) findViewById(R.id.txt_email_)).setText(adPost.getEmail().trim());
            ((CustomTextView) findViewById(R.id.txt_email_)).setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(context, 4), null, null, null);

        } else {
            txt_email_id.setText(context.getString(R.string.na));
        }


        if (adPost.getFull_name() != null && !adPost.getFull_name().isEmpty()) {
            txt_vendor_name.setText(adPost.getFull_name().trim());
            ((CustomTextView) findViewById(R.id.txt_name_)).setText(adPost.getFull_name().trim());
            ((CustomTextView) findViewById(R.id.txt_name_)).setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(context, 1), null, null, null);

        } else {
            txt_vendor_name.setText(context.getString(R.string.na));
        }

        if (adPost.getMobile_no() != null && !adPost.getMobile_no().isEmpty()) {
            String mobile = adPost.getMobile_code() + "" + adPost.getMobile_no().trim();
            txt_contact.setText(mobile);
            ((CustomTextView) findViewById(R.id.txt_number_)).setText(mobile);
            ((CustomTextView) findViewById(R.id.txt_number_)).setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(context, 3), null, null, null);
        } else {
            txt_contact.setText(context.getString(R.string.na));
        }


       /* if (adPost.getMobile_no() != null && !adPost.getMobile_no().isEmpty()) {
            txt_contact.setText(adPost.getMobile_no().trim());
            ((CustomTextView) findViewById(R.id.txt_number_)).setText(adPost.getMobile_no().trim());
            ((CustomTextView) findViewById(R.id.txt_number_)).setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(context, 3), null, null, null);

        } else {
            txt_contact.setText(context.getString(R.string.na));
        }*/

        if (adPost.getAddress() != null && !adPost.getAddress().isEmpty()) {
            txt_adress.setText(adPost.getAddress().trim());
            ((CustomTextView) findViewById(R.id.txt_address_)).setText(adPost.getAddress().trim());
            ((CustomTextView) findViewById(R.id.txt_address_)).setCompoundDrawablesWithIntrinsicBounds(Utility.getDrawable(context, 2), null, null, null);

        } else {
            txt_adress.setText(context.getString(R.string.na));
        }


        if (adPost.getUser_image() != null && !adPost.getUser_image().isEmpty())
            new AQuery(context).id(iv_profile).image(adPost.getUser_image(), true, true, 0, R.mipmap.gestuser);
        else
            iv_profile.setImageResource(R.mipmap.gestuser);


        if (adPost.getImage().size() == 0) {
            if (type.equalsIgnoreCase("3")) {
                if (adPost.getGarage_image() != null && !adPost.getGarage_image().isEmpty()) {
                    url_from_api.put(adPost.getGarage_image(), adPost.getGarage_image());
                } else {
                    Utility.setUpViewPagerNew(imageSlider, context);
                }
            } else {
                Utility.setUpViewPagerNew(imageSlider, context);
            }


        } else {
            for (String s : adPost.getImage()) {
                url_from_api.put(s, s);
            }
            Utility.setUpViewPager(imageSlider, context, url_from_api, "0");
        }

        if (type.equalsIgnoreCase("2")) {
            ((RelativeLayout) findViewById(R.id.rl_rent)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.ll_car_type)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.ll_car_milleage)).setVisibility(View.GONE);
            card_duration.setVisibility(View.VISIBLE);

            if (adPost.getYear_to() != null && !adPost.getYear_to().isEmpty())
                txt_to.setText(adPost.getYear_to().trim());
            else
                txt_to.setText(context.getString(R.string.na));


            if (adPost.getTo_date() != null && !adPost.getTo_date().isEmpty()) {
                txt_pick_up.setText(adPost.getTo_date().trim());
            } else {
                card_duration.setVisibility(View.GONE);
            }

            if (adPost.getFrom_date() != null && !adPost.getFrom_date().isEmpty()) {
                txt_drop_off.setText(adPost.getFrom_date().trim());
            } else {
                card_duration.setVisibility(View.GONE);
            }

            if (adPost.getYear_from() != null && !adPost.getYear_from().isEmpty())
                txt_from.setText(adPost.getYear_from().trim());
            else
                txt_from.setText(context.getString(R.string.na));
        } else {
            ((RelativeLayout) findViewById(R.id.rl_rent)).setVisibility(View.GONE);
            ((LinearLayout) findViewById(R.id.ll_car_type)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.ll_car_milleage)).setVisibility(View.VISIBLE);
        }


        if (type.equalsIgnoreCase("3")) {
            ((CardView) findViewById(R.id.card_car_detail)).setVisibility(View.GONE);

            imageSlider.setVisibility(View.GONE);

            /*if (adPost.getProblem() != null && !adPost.getProblem().isEmpty())
                txt_problem.setText(adPost.getProblem().trim());
            else
                txt_problem.setText(context.getString(R.string.na));*/
        }

        if (adPost.getProblem() != null && !adPost.getProblem().isEmpty()) {
            card_problem.setVisibility(View.VISIBLE);

            if (type.equalsIgnoreCase("3")) {
                txt_message.setText(getString(R.string.car_problem));
            } else {
                txt_message.setText(getString(R.string.message));
            }

            txt_problem.setText(adPost.getProblem().trim());
        } else {
            card_problem.setVisibility(View.GONE);

        }

        ((LinearLayout) findViewById(R.id.ll_main)).setVisibility(View.VISIBLE);


    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
