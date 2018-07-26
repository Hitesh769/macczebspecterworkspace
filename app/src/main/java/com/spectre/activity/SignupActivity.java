package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.spectre.R;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private CustomEditText etMail, etName, etMobile, etPassword, etCPassword;
    private AppCompatCheckBox checkbox;
    private CustomEditText txt_country_name;
    private String name, mob, mail, pin, cpin;
    private CountryCodePicker ccp;
    private int type = 1;
    private AlertBox alertBox;
    private String a = "I agree to Spectre's Terms & Conditions.";
    private CustomTextView txtTerms;
    private String countryAlpha = "AE";
    private String countryCode = "+971";
    private boolean isValid;
    private String number;
    private AppCompatRadioButton radioCustomer, radioGarage;
    private boolean checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_signup);
        Utility.setUpToolbar_(context, "<font color='#ffffff'>Sign Up</font>", true);
        //   setContentView(R.layout.activity_signup);
        initView();
    }

    private void initView() {
        txtTerms = (CustomTextView) findViewById(R.id.txt_terms);
        etMail = (CustomEditText) findViewById(R.id.et_mail);
        etName = (CustomEditText) findViewById(R.id.et_name);
        etMobile = (CustomEditText) findViewById(R.id.et_mobile_no);
        etPassword = (CustomEditText) findViewById(R.id.et_pin);
        etCPassword = (CustomEditText) findViewById(R.id.et_cpin);
        checkbox = (AppCompatCheckBox) findViewById(R.id.checkbox);
        txt_country_name = (CustomEditText) findViewById(R.id.txt_country_name);
        txt_country_name.setOnClickListener(this);
        //  txt_country_name.setEnabled(false);
        txt_country_name.setClickable(true);
        txt_country_name.setFocusable(false);
        txt_country_name.setLongClickable(false);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ((CustomRayMaterialTextView) findViewById(R.id.btn_submit)).setOnClickListener(this);

        radioCustomer = findViewById(R.id.radio_customer);
        radioGarage = findViewById(R.id.radio_garage);

        AppCompatRadioButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioCustomer.setChecked(radioCustomer == buttonView);
                    radioGarage.setChecked(radioGarage == buttonView);
                    checked = true;
                }
            }
        };

        radioCustomer.setOnCheckedChangeListener(listener);
        radioGarage.setOnCheckedChangeListener(listener);


        alertBox = new AlertBox(context);
        if (getIntent() != null && getIntent().hasExtra(Constant.TYPE)) {
            type = getIntent().getIntExtra(Constant.TYPE, 1);
        }

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                try {
                    txt_country_name.setText(ccp.getSelectedCountryCodeWithPlus());
                    countryCode = ccp.getSelectedCountryCodeWithPlus();
                    countryAlpha = ccp.getSelectedCountryNameCode();
                    ccp.setDefaultCountryUsingNameCode(ccp.getDefaultCountryNameCode());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        setUpSpan();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                if (areFieldsValid()) {
                    callAPI();
                }

                break;
            case R.id.txt_country_name:
                ccp.launchCountrySelectionDialog();
                //   CountryCodeDialog.openCountryCodeDialog(ccp);
                break;
        }
    }


    private boolean areFieldsValid() {
        String message = "";
        name = etName.getText().toString().trim();
        mob = etMobile.getText().toString().trim();
        mail = etMail.getText().toString().trim();
        pin = etPassword.getText().toString().trim();
        cpin = etCPassword.getText().toString().trim();

        etName.setError(null);
        etMobile.setError(null);
        etMail.setError(null);
        etPassword.setError(null);
        etCPassword.setError(null);


        if (name.length() == 0 || name.length() < 1) {
            etName.setError(getString(R.string.enter_valid_name));
            etName.requestFocus();
            return false;
        }


        if (mob.trim().length() == 0) {
            etMobile.setError(getString(R.string.please_enter_mobile_no));
            return false;
        }


        if (!mob.trim().isEmpty()) {

            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(mob.trim(), countryAlpha);
                isValid = phoneUtil.isValidNumber(swissNumberProto);
                PhoneNumberUtil.PhoneNumberType nrtype = phoneUtil.getNumberType(swissNumberProto);
                // PhoneNumberUtil.PhoneNumberType nrtype = phoneUtil.getNumberType(swissNumberProto);
                PhoneNumberUtil.ValidationResult validationResult = phoneUtil.isPossibleNumberForTypeWithReason(swissNumberProto, nrtype);
                if (validationResult.name().equalsIgnoreCase("TOO_SHORT")) {
                    message = "The phone number you enterd is too short for the country: " + txt_country_name.getText().toString().trim() + ".";
                    isValid = false;
                } else if (validationResult.name().equalsIgnoreCase("TOO_LONG")) {
                    message = "The phone number you entered is too long for the country: " + txt_country_name.getText().toString().trim() + ".";
                    isValid = false;
                } else if (validationResult.name().equalsIgnoreCase("INVALID_LENGTH")) {
                    //message = "The phone number you entered is too long for the country: " + txt_country_name.getText().toString().trim() + ".";
                    isValid = false;
                }

               /* if (nrtype.name().equalsIgnoreCase("MOBILE")) {
                    isValid = true;
                } else if (nrtype.name().equalsIgnoreCase("FIXED_LINE_OR_MOBILE")) {
                    isValid = true;
                }*//* else if (nrtype.name().equalsIgnoreCase("UNKNOWN") && validationResult.name().equalsIgnoreCase("IS_POSSIBLE")) {
                    isValid = true;
                }*//* else {
                    isValid = false;
                }*/

            } catch (Exception e) {
                if (e.getMessage().equalsIgnoreCase("The string supplied did not seem to be a phone number.")) {
                    // alertBox.openMessage(getString(R.string.mobile_error), "Ok", "", false);
                    showToast(getString(R.string.mobile_error));
                    return false;
                } else if (e.getMessage().equalsIgnoreCase("The string supplied is too short to be a phone number.")) {
                    // alertBox.openMessage("The phone number you entered is too short for the country: " + txt_country_name.getText().toString().trim() + ".", "Ok", "", false);
                    showToast("The phone number you entered is too short for the country: " + txt_country_name.getText().toString().trim() + ".");
                    return false;
                }

                isValid = false;

            }

            if (!isValid) {
                if (message.isEmpty())
                    showToast(getString(R.string.mobile_error));
                else
                    showToast(message);
                //  alertBox.openMessage(getString(R.string.mobile_error), "Ok", "", false);

                // alertBox.openMessage(message, "Ok", "", false);
                // MyDialog.dialog_(getString(R.string.mobile_error), appContext);

                return false;
            }


        }

        if (!Utility.emailValidator(mail.trim())) {
            etMail.setError(getString(R.string.Enter_valid_mail));
            return false;
        }

        if (pin.length() == 0) {
            etPassword.setError(getString(R.string.enter_valid_pin));
            etPassword.requestFocus();
            return false;
        }


        if (pin.length() < 6) {
            etPassword.setError(getString(R.string.password_count));
            etPassword.requestFocus();
            return false;
        }

        if (cpin.length() == 0) {
            etCPassword.setError(getString(R.string.enter_valid_cpin));
            etCPassword.requestFocus();
            return false;
        }

        if (cpin.length() < 6) {
            etCPassword.setError(getString(R.string.con_password_count));
            etCPassword.requestFocus();
            return false;
        }

        if (!pin.equals(cpin)) {
            etCPassword.setError(getString(R.string.pin_does_not_match));
            etCPassword.requestFocus();
            return false;
        }

        if (!checkbox.isChecked()) {
            showToast("Please agree the Terms and Conditions to proceed.");
            return false;
        }

        return true;
    }


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    private void callAPI() {
        /*{
            "user_name":"sumit",
                "user_mobile":"8962493409",
                "user_password":"123456",
                "user_email":"sumit@ebabu.com",
                "user_type":"1"
        }*/
        if (!Utility.isConnectingToInternet(context)) {
            alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
            return;
        }

        MyDialogProgress.open(context);

        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put(Constant.USER_NAME, etName.getText().toString().trim());
            number = etMobile.getText().toString().trim();
            if (number.startsWith("0")) {
                number = number.replaceFirst("0", "");
            } else {

            }
            jsonObject.put(Constant.USER_MOBILE, number);
            jsonObject.put(Constant.USER_PASSWORD, etPassword.getText().toString().trim());
            jsonObject.put(Constant.USER_EMAIL, etMail.getText().toString().trim());
            jsonObject.put(Constant.USER_TYPE, radioCustomer.isChecked() ? "1" : "2");
            jsonObject.put(Constant.MOBILE_CODE, countryCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AqueryCall request = new AqueryCall(this);
        request.postWithoutToken(Urls.SIGNUP, jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                saveData(js);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {

            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                MyDialogProgress.close(context);
                new AlertBox(context).openMessage(inactive, context.getString(R.string.ok), "", false);

            }
        });
    }

    private void saveData(JSONObject js) {
        MyDialogProgress.close(context);
        Utility.setSharedPreference(context, Constant.USER_MOBILE, number);
        startActivity(new Intent(context, OTPActivity.class).putExtra(Constant.TYPE, 1));
    }


    private void setUpSpan() {
        SpannableString ss = new SpannableString(a);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                // ds.setColor(getResources().getColor(R.color.colorPrimary));
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(new MyClickSpan(1), a.indexOf("Terms & Conditions."), a.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), a.indexOf("Terms & Conditions."), a.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), a.indexOf("Terms & Conditions."), a.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtTerms.setText(ss);
        txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
        txtTerms.setHighlightColor(Color.TRANSPARENT);
    }


    public class MyClickSpan extends ClickableSpan {

        int pos;

        public MyClickSpan(int position) {
            this.pos = position;
        }

        @Override
        public void onClick(View widget) {
            //  startActivity(new Intent(context, WebViewActivity.class).putExtra(Constant.TYPE, 1));
            Utility.showToast(context, "Coming Soon");
        }

    }
}
