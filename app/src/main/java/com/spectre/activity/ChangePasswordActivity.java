package com.spectre.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.spectre.R;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private CustomEditText etOldPin, etNewPin, etCnewPin;
    private String oldPin, newPin, cNewPin;
    private CustomRayMaterialTextView btn_change_pass;
    private AlertBox alertBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_change_password);
        context = this;
        Utility.setContentView(context, R.layout.activity_change_password);
        Utility.setUpToolbar_(context, "<font color='#ffffff'>Change Password</font>", true);
        initView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initView() {
        context = this;

        etOldPin = (CustomEditText) findViewById(R.id.et_old_pin);
        etNewPin = (CustomEditText) findViewById(R.id.et_new_pin);
        etCnewPin = (CustomEditText) findViewById(R.id.et_cnew_pin);
        btn_change_pass = (CustomRayMaterialTextView) findViewById(R.id.btn_change_pass);
        btn_change_pass.setOnClickListener(this);
        alertBox = new AlertBox(context);
    }

    private boolean areFieldsValid() {
        oldPin = etOldPin.getText().toString().trim();
        newPin = etNewPin.getText().toString().trim();
        cNewPin = etCnewPin.getText().toString().trim();

        if (oldPin.length() == 0) {
            etOldPin.setError(getString(R.string.enter_valid_pin));
            etOldPin.requestFocus();
            return false;
        }

        if (newPin.length() == 0) {
            etNewPin.setError(getString(R.string.enter_valid_pin));
            etNewPin.requestFocus();
            return false;
        }

        if (newPin.length() < 6) {
            etNewPin.setError(getString(R.string.password_count));
            etNewPin.requestFocus();
            return false;
        }

        if (cNewPin.length() == 0) {
            etCnewPin.setError(getString(R.string.enter_valid_cpin));
            etCnewPin.requestFocus();
            return false;
        }

        if (cNewPin.length() < 6) {
            etCnewPin.setError(getString(R.string.con_password_count));
            etCnewPin.requestFocus();
            return false;
        }

        if (!newPin.equalsIgnoreCase(cNewPin)) {
            Toast.makeText(context, getString(R.string.pin_does_not_match), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (areFieldsValid()) {
            changePin();
            //  showToast("Password changed successfully");
            //   Utility.showToast(context,"Password changed successfully");
        }
    }

    private void changePin() {
        /*"{
        ""user_mobile"":""9754743271"",
                ""user_password"":""1235"",
                ""device_type"":""android"",
                ""user_device_token"":""d46s5f464s6f4s6f4sf65s4f6s4f6s5f4sd6f4sd6""
    }"*/

        MyDialogProgress.open(context);

        JSONObject js = new JSONObject();
        try {
            js.put(Constant.OLD_PASSWORD, oldPin);
            js.put(Constant.NEW_PASSWORD, newPin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AqueryCall(this).postWithJsonToken(Urls.CHANGE_PASSWORD, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), js, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String success) {
                // showToast(success);
              //  Utility.showToast(context, success);
                alertBox.openMessageWithFinish(success, "Okay", "", false);
                MyDialogProgress.close(context);

            }

            @Override
            public void onFailed(JSONObject js, String failed) {
                Utility.showToast(context, failed);
                MyDialogProgress.close(context);
                //  showToast(failed);
            }

            @Override
            public void onAuthFailed(JSONObject js, String failed) {

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
