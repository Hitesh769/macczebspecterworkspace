package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.spectre.R;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private SwitchCompat sw_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_settings);
        context = this;
        Utility.setContentView(context, R.layout.activity_settings);
        Utility.setUpToolbar_(context, "<font color='#ffffff'>Settings</font>", true);
        initView();
    }

    private void initView() {
        sw_notification = (SwitchCompat) findViewById(R.id.notification);
        Typeface type = Typeface.createFromAsset(getAssets(), "Poppins-Regular.ttf");
        sw_notification.setTypeface(type);
       // sw_notification.setChecked(true);
        UtilityGetValue(sw_notification, Constant.APP_NOTIFICATION);
        sw_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UtilitySetValue(Constant.APP_NOTIFICATION, isChecked);
            }
        });
        ((CustomTextView) findViewById(R.id.tv_edt_profile)).setOnClickListener(this);
        ((CustomTextView) findViewById(R.id.tv_change_pass)).setOnClickListener(this);
        ((CardView) findViewById(R.id.cv_logout)).setOnClickListener(this);
        ((CardView) findViewById(R.id.cv_change_language)).setOnClickListener(this);
        ((CardView) findViewById(R.id.cv_privacy_policy)).setOnClickListener(this);
        ((CardView) findViewById(R.id.cv_about_us)).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.cv_privacy_policy:
                break;
            case R.id.cv_change_language:
                break;
                case R.id.cv_about_us:
               //    startActivity(new Intent(context, AboutUsActivity.class));
                break;
            case R.id.cv_logout:
                Utility.openDialogToLogout(context);
                break;
            case R.id.tv_edt_profile:
                startActivity(new Intent(context, EditProfileActivity.class));
                break;
            case R.id.tv_change_pass:
                startActivity(new Intent(context, ChangePasswordActivity.class));
                break;
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void UtilityGetValue(SwitchCompat switchType, String name) {
        // switchType.setChecked(Utility.getSharedPreferencesBoolean_(context, name));
        switchType.setChecked(SharedPrefUtils.getPreference(context, name, true));
    }

    private void UtilitySetValue(String name, boolean value) {
        SharedPrefUtils.setPreference(context, name, value);
    }

}
