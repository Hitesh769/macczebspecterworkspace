package com.spectre.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.spectre.R;
import com.spectre.activity_new.HomeAct;
import com.spectre.activity_new.MasterAppCompactActivity;
import com.spectre.other.Constant;
import com.spectre.utility.NotificationHelper;
import com.spectre.utility.SharedPrefUtils;

import java.util.Timer;
import java.util.TimerTask;

import static com.spectre.utility.Utility.setLog;

public class SplashActivity extends MasterAppCompactActivity {

    private Context context;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        if (!SharedPrefUtils.getPreference(context, Constant.is_channel_prepared, false) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            new NotificationHelper(context);

        initView();
    }

    private void initView() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startNextActivity();
            }
        }, 3000);
    }


    private void startNextActivity() {
        String token = SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, "");
        if (TextUtils.isEmpty(token))
            SharedPrefUtils.setPreference(context, Constant.USER_TOKEN, "ef73781effc5774100f87fe2f437a435");
        token = SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, "");
        setLog("Token : " + token);
        if (i == 0) {
            i++;
            String s = SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "");
            if (s != null && !s.isEmpty()) {
                startActFinish(HomeAct.getStartIntent(context, s));
            } else {
                // startActFinish(HomeFormatActivity.getStartIntent(context, "0"));
                startActFinish(HomeAct.getStartIntent(context, "0"));
            }
            finish();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startNextActivity();
        return super.onTouchEvent(event);

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
