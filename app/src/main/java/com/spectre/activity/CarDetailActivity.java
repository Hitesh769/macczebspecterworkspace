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
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.daimajia.slider.library.SliderLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.spectre.R;
import com.spectre.activity_new.BookCarInfoActivity;
import com.spectre.activity_new.BookCarSummaryActivity;
import com.spectre.activity_new.HomeAct;
import com.spectre.beans.AdPost;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.helper.Common;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.TouchImageView;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.spectre.utility.Utility.setLog;

public class CarDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private SliderLayout imageSlider;
    private CustomTextView txt_car_name, txt_car_price, txt_car_model, txt_car_version,
            txt_car_type, txt_car_mileage, txt_email_id, txt_vendor_name, txt_adress,
            txt_contact, txt_from, txt_to, txt_car_posted_date, vendor_detail;
    /*car specification*/

    private CustomTextView txt_color, txt_distance, txt_quality, txt_year;

    private CircleImageView iv_profile;
    private CardView card1, card2, card3;
    private CustomRayMaterialTextView btn_show_interest;
    private Display display;
    private ActionBar actionBar;
    private AdPost adPost;
    private int position = -1, type = -1;
    HashMap<String, String> url_from_api = new HashMap<String, String>();
    private boolean isChange;
    private String perDay = "";
    private Dialog dd;
    AppCompatRadioButton male;
    AppCompatRadioButton female;
    EditText edtFirstName, edtLocation, edtEmail, edtContect;
    private static final int PLACE_PICKER_REQUEST = 999;
    private String latitude = "";
    private String longitude = "";
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

        edtFirstName = (EditText) findViewById(R.id.input_first_name);
        edtLocation = (EditText) findViewById(R.id.input_location);
        edtContect = (EditText) findViewById(R.id.input_contect);
        edtEmail = (EditText) findViewById(R.id.input_email);

        txt_color = (CustomTextView) findViewById(R.id.txt_color);
        txt_distance = (CustomTextView) findViewById(R.id.txt_distance);
        txt_quality = (CustomTextView) findViewById(R.id.txt_quality);
        txt_year = (CustomTextView) findViewById(R.id.txt_year);
        male = (AppCompatRadioButton) findViewById(R.id.radio_male);
        female = (AppCompatRadioButton) findViewById(R.id.radio_female);


        vendor_detail = (CustomTextView) findViewById(R.id.vendor_detail);

        card1 = (CardView) findViewById(R.id.card1);
        card2 = (CardView) findViewById(R.id.card2);
        card3 = (CardView) findViewById(R.id.card3);

        iv_profile = (CircleImageView) findViewById(R.id.iv_profile);

        edtLocation.setOnClickListener(this);
        btn_show_interest = (CustomRayMaterialTextView) findViewById(R.id.btn_show_interest);
        btn_show_interest.setOnClickListener(this);

        AppCompatRadioButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    male.setChecked(male == buttonView);
                    female.setChecked(female == buttonView);
                }
            }
        };
        male.setOnCheckedChangeListener(listener);
        female.setOnCheckedChangeListener(listener);

        //save data in buyer details
        edtFirstName.setText(SharedPrefUtils.getPreference(context, Constant.USER_NAME, ""));
        edtLocation.setText(SharedPrefUtils.getPreference(context, Constant.USER_ADDRESS_, ""));
        edtContect.setText(SharedPrefUtils.getPreference(context, Constant.USER_MOBILE, ""));
        edtEmail.setText(SharedPrefUtils.getPreference(context, Constant.USER_EMAIL, ""));

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarDetailActivity.this, SellerDetailsActivity.class);
                intent.putExtra(Constant.DATA, adPost);
                intent.putExtra(Constant.POSITION, position);
                intent.putExtra(Constant.TYPE, type);
                startActivity(intent);
            }
        });

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

            if (adPost.getColor() != null && !adPost.getColor().isEmpty()) {
                txt_color.setText(adPost.getColor().trim());
            } else {
                txt_color.setText(context.getString(R.string.na));
            }

            if (adPost.getYear() != null && !adPost.getYear().isEmpty()) {
                txt_year.setText(adPost.getYear().trim());
            } else {
                txt_year.setText(context.getString(R.string.na));
            }

            if (adPost.getCar_condition() != null && !adPost.getCar_condition().isEmpty()) {
                txt_quality.setText(adPost.getCar_condition().trim());
            } else {
                txt_quality.setText(context.getString(R.string.na));
            }

            if (adPost.getMileage() != null && !adPost.getMileage().isEmpty()) {
                txt_distance.setText(adPost.getMileage().trim());
            } else {
                txt_distance.setText(context.getString(R.string.na));
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
                ((LinearLayout) findViewById(R.id.ll_from)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_to)).setVisibility(View.VISIBLE);
                ((LinearLayout) findViewById(R.id.ll_car_type)).setVisibility(View.GONE);
                ((LinearLayout) findViewById(R.id.ll_car_milleage)).setVisibility(View.GONE);
                card3.setVisibility(View.GONE);
                card1.setVisibility(View.GONE);
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

         /*   if (!adPost.getIs_interest().isEmpty() && adPost.getIs_interest().equalsIgnoreCase("1")) {
                btn_show_interest.setText(R.string.request_sent);
            } else {*/
                if (type == 1) {
                    btn_show_interest.setText(R.string.book_car);
                    vendor_detail.setText(R.string.vendor_detail);

                } else {
                    btn_show_interest.setText(R.string.buy_car);
                }

          //  }


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
                //display information activity
                if (btn_show_interest.getText().toString().equalsIgnoreCase(getResources().getString(R.string.book_car))) {
                    Intent intent = new Intent(this, BookCarInfoActivity.class);
                    intent.putExtra(Constant.DATA, adPost);
                    intent.putExtra(Constant.POSITION, position);
                    intent.putExtra(Constant.TYPE, type);
                    startActivity(intent);
                } else if (btn_show_interest.getText().toString().equalsIgnoreCase(getResources().getString(R.string.buy_car))) {
                    String gender = "1";
                    if (male.isChecked()) {
                        gender = "1";
                    } else {
                        gender = "2";
                    }
                    if (!edtFirstName.getText().toString().trim().isEmpty()&&!edtLocation.getText().toString().trim().isEmpty()&&!edtEmail.getText().toString().trim().isEmpty()&&!edtContect.getText().toString().trim().isEmpty()) {
                        if (emailValidator(edtEmail.getText().toString()) == true) {
                            Intent intent = new Intent(this, BookCarSummaryActivity.class);
                            intent.putExtra(Constant.DATA, adPost);
                            intent.putExtra(Constant.POSITION, position);
                            intent.putExtra(Constant.TYPE, type);
                            intent.putExtra(Constant.NAME, edtFirstName.getText().toString());
                            intent.putExtra(Constant.LOCATION, edtLocation.getText().toString());
                            intent.putExtra(Constant.EMAIL, edtEmail.getText().toString());
                            intent.putExtra(Constant.PHONE, edtContect.getText().toString());
                            intent.putExtra(Constant.GENDER, gender);
                            startActivity(intent);
                        } else {
                            Toast.makeText(context,"Please enter valid EmailID",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(context,"Field can't empty",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.input_location:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(CarDetailActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setLog("location getting");
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, CarDetailActivity.this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                Utility.setLog("LAT 2 : " + latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(", ");
                stBuilder.append(address);
                setLog("address : " + address);
                edtLocation.setText(stBuilder.toString());
                // Arraylist.clear();
                // callMethodEventList(0);
            }
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
            js.put(Constant.END_USER, adPost.getCar_name_id());

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

              /*  if (!adPost.getIs_interest().isEmpty() && adPost.getIs_interest().equalsIgnoreCase("1")) {
                    btn_show_interest.setText(R.string.request_sent);
                } else {*/
                    if (type == 1) {
                        btn_show_interest.setText(R.string.book_car);
                    } else {
                        btn_show_interest.setText(R.string.buy_car);
                    }

              //  }


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

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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
