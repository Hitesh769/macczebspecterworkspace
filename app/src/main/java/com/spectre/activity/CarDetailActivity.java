package com.spectre.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.daimajia.slider.library.SliderLayout;
import com.spectre.R;
import com.spectre.activity_new.BookCarInfoActivity;
import com.spectre.activity_new.HomeAct;
import com.spectre.beans.AdPost;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
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

public class CarDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private SliderLayout imageSlider;
    private CustomTextView txt_car_name, txt_car_price, txt_car_model, txt_car_version,
            txt_car_type, txt_car_mileage, txt_email_id, txt_vendor_name, txt_adress,
            txt_contact, txt_from, txt_to, txt_car_posted_date;
    private CircleImageView iv_profile;

    private CustomRayMaterialTextView btn_show_interest;
    private Display display;
    private ActionBar actionBar;
    private AdPost adPost;
    private int position = -1, type = -1;
    HashMap<String, String> url_from_api = new HashMap<String, String>();
    private boolean isChange;
    private String perDay = "";
    private Dialog dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_car_detail);
        context = this;
        Utility.setContentView(context, R.layout.activity_car_detail_new);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Car's Detail</font>", true);
        initView();
    }

    private void initView() {

        imageSlider = (SliderLayout) findViewById(R.id.slider);
        //txt_car_name = (CustomTextView) findViewById(R.id.txt_car_name);
        txt_car_price = (CustomTextView) findViewById(R.id.txt_car_price);
        txt_car_model = (CustomTextView) findViewById(R.id.txt_car_model);
        txt_car_version = (CustomTextView) findViewById(R.id.txt_car_version);
        txt_car_type = (CustomTextView) findViewById(R.id.txt_car_type);
        txt_car_mileage = (CustomTextView) findViewById(R.id.txt_car_mileage);
        txt_car_posted_date = (CustomTextView) findViewById(R.id.txt_car_posted_date);
        txt_email_id = (CustomTextView) findViewById(R.id.txt_email_id);
        txt_vendor_name = (CustomTextView) findViewById(R.id.txt_vendor_name);
        txt_adress = (CustomTextView) findViewById(R.id.txt_adress);
        txt_contact = (CustomTextView) findViewById(R.id.txt_contact);
        txt_to = (CustomTextView) findViewById(R.id.txt_to);
        txt_from = (CustomTextView) findViewById(R.id.txt_from);
        iv_profile = (CircleImageView) findViewById(R.id.iv_profile);
        btn_show_interest = (CustomRayMaterialTextView) findViewById(R.id.btn_show_interest);
        btn_show_interest.setOnClickListener(this);


        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {

            adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + adPost.getCar_name() + "</font>"));
            position = getIntent().getExtras().getInt(Constant.POSITION);
            type = getIntent().getExtras().getInt(Constant.TYPE);

            if (type == 1) {
                perDay = getString(R.string.per_day);
            }


            display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            android.view.ViewGroup.LayoutParams layoutParams = imageSlider.getLayoutParams();
            layoutParams.width = display.getWidth();
            layoutParams.height = Utility.dpToPx(context, 250);
            imageSlider.setLayoutParams(layoutParams);

           /* if (adPost.getCar_name() != null && !adPost.getCar_name().isEmpty())
                txt_car_name.setText(adPost.getCar_name());
            else
                txt_car_name.setText(getString(R.string.na));*/


            if (adPost.getPrice() != null && !adPost.getPrice().isEmpty()) {
                //  txt_car_price.setText(context.getString(R.string.dollar) + " " + adPost.getPrice().trim());
                txt_car_price.setText(getString(R.string.dollar) + " " + adPost.getPrice().trim() + "" + perDay);
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


            if (adPost.getCreate_date() != null && !adPost.getCreate_date().isEmpty()) {
                txt_car_posted_date.setText(adPost.getCreate_date().trim());
            } else {
                txt_car_posted_date.setText(context.getString(R.string.na));
            }


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
                Utility.setUpViewPagerNew(imageSlider, context);
            } else {
                for (String s : adPost.getImage()) {
                    url_from_api.put(s, s);
                }
                Utility.setUpViewPager(imageSlider, context, url_from_api, "0");
            }

            if (type == 1) {
                //((RelativeLayout) findViewById(R.id.rl_rent)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_from)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_to)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_car_type)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_car_milleage)).setVisibility(View.GONE);

                if (adPost.getYear_to() != null && !adPost.getYear_to().isEmpty())
                    txt_to.setText(adPost.getYear_to().trim());
                else
                    txt_to.setText(context.getString(R.string.na));

                if (adPost.getYear_from() != null && !adPost.getYear_from().isEmpty())
                    txt_from.setText(adPost.getYear_from().trim());
                else
                    txt_from.setText(context.getString(R.string.na));
            } else {
                //((RelativeLayout) findViewById(R.id.rl_rent)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_from)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_to)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_car_type)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_car_milleage)).setVisibility(View.VISIBLE);
            }

            if (!adPost.getIs_interest().isEmpty() && adPost.getIs_interest().equalsIgnoreCase("1")) {
                btn_show_interest.setText(R.string.request_sent);
            } else {
                if (type == 1) {
                    btn_show_interest.setText(R.string.book_car);
                } else {
                    btn_show_interest.setText(R.string.buy_car);
                }

            }


        } else {
            new AlertBox(context).openMessageWithFinish(getResources().getString(R.string.something_wrong), "Okay", "", false);
        }
    }

    @Override
    public void finish() {
        if (isChange && position != -1) {
            Intent intent = new Intent();
            intent.putExtra(Constant.DATA, adPost);
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.TYPE, type);
            setResult(Activity.RESULT_OK, intent);
        }
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        String s = SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "");
        switch (v.getId()) {
            case R.id.btn_show_interest:
               /* if (s.isEmpty() || s.equalsIgnoreCase("0")) {
                    Utility.openDialogToLogin(context);
                } else {
                    if (btn_show_interest.getText().toString().equalsIgnoreCase(getResources().getString(R.string.book_car)))
                        showProblem(1);

                    if (btn_show_interest.getText().toString().equalsIgnoreCase(getResources().getString(R.string.buy_car)))
                        showProblem(2);
                }*/
                //display information activity
                if (btn_show_interest.getText().toString().equalsIgnoreCase(getResources().getString(R.string.book_car))) {
                // startActivity(new Intent(this, BookCarInfoActivity.class));
                    Intent intent = new Intent(this, BookCarInfoActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    intent.putExtra(Constant.TYPE, getIntent().getStringExtra(Constant.TYPE));
                    startActivity(intent);
                }
                break;
        }
    }

    private void callAPI(final Dialog dd, String trim, String from, String to) {
        MyDialogProgress.open(context);

        JSONObject js = new JSONObject();
        try {
            js.put(Constant.SECOND_USER_ID, adPost.getUser_id());
            js.put(Constant.INTERESTED_ID, adPost.getAdd_id());
            js.put(Constant.TYPE, type == 0 ? 1 : 2);
            js.put(Constant.PROBLEM, trim);
            js.put(Constant.FROM_DATE, from);
            js.put(Constant.TO_DATE, to);

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
                isChange = true;
                dd.dismiss();
                adPost.setIs_interest("1");

                if (!adPost.getIs_interest().isEmpty() && adPost.getIs_interest().equalsIgnoreCase("1")) {
                    btn_show_interest.setText(R.string.request_sent);
                } else {
                    if (type == 1) {
                        btn_show_interest.setText(R.string.book_car);
                    } else {
                        btn_show_interest.setText(R.string.buy_car);
                    }

                }


                new AlertBox(context).openMessage(success, "Okay", "", false);
            }

            @Override
            public void onFailed(JSONObject js, String failed) {
                Utility.showToast(context, failed);
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


    private void showProblem(final int isRent) {

        try {
            if (dd == null) {
                dd = new Dialog(context);
                dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dd.setContentView(R.layout.dialog_send_request);
                dd.setCancelable(true);
                dd.getWindow().setLayout(-1, -2);
                dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                dd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            }
            dd.show();

            CustomTextView view = dd.findViewById(R.id.view);
            LinearLayout llDate = dd.findViewById(R.id.ll_date);

            if (isRent == 1) {
                view.setVisibility(View.GONE);
                llDate.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.VISIBLE);
                llDate.setVisibility(View.GONE);
            }


            final CustomEditText etMobileNumber = (CustomEditText) dd.findViewById(R.id.et_problem);
            final CustomTextView etFrom = (CustomTextView) dd.findViewById(R.id.et_car_from);
            final CustomTextView etTo = (CustomTextView) dd.findViewById(R.id.et_car_to);
            etFrom.setOnClickListener(this);
            etTo.setOnClickListener(this);

            CustomRayMaterialTextView btn = ((CustomRayMaterialTextView) dd.findViewById(R.id.btn_save_changes));
            // btn.setText(getString(R.string.show_interest));
            if (type == 1) {
                btn.setText(R.string.book_car);
            } else {
                btn.setText(R.string.buy_car);
            }

            etMobileNumber.setHint(getString(R.string.write_message_error));
            etFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.openCalendarDialog(context, etFrom);
                }
            });
            etTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.openCalendarDialog(context, etTo);
                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etMobileNumber.setError(null);

                   /* if (etMobileNumber.getText().toString().trim().isEmpty()) {
                        etMobileNumber.setError(getString(R.string.char_count_msg_error));
                        return;
                    }

                    if (etMobileNumber.getText().toString().trim().length() < 50) {
                        etMobileNumber.setError(getString(R.string.char_count_msg_error));
                        return;
                    }
*/
                    if (isRent == 1) {
                        if (etFrom.getText().toString().trim().isEmpty()) {
                            Utility.showToast(context, getString(R.string.select_pick_up_date));
                            //  etFrom.setError(getString(R.string.select_from));
                            return;
                        }

                        if (etTo.getText().toString().trim().isEmpty()) {
                            // etTo.setError(getString(R.string.select_from));
                            Utility.showToast(context, getString(R.string.select_drop_off_date));
                            return;
                        }

                        if (!Utility.checkDate(etFrom.getText().toString().trim(), etTo.getText().toString().trim())) {
                            Utility.showToast(context, getString(R.string.rent_validation));
                            return;
                        }

                        if (!Utility.checkDate(etTo.getText().toString().trim(), adPost.getYear_to())) {
                            Utility.showToast(context, getString(R.string.invalid_date));
                            return;
                        }

                        if (!Utility.checkDate(etFrom.getText().toString().trim(), adPost.getYear_to())) {
                            Utility.showToast(context, getString(R.string.invalid_date));
                            return;
                        }
                    }


                    callAPI(dd, etMobileNumber.getText().toString().trim(), etFrom.getText().toString().trim(), etTo.getText().toString().trim());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
