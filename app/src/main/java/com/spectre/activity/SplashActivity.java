package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.spectre.R;
import com.spectre.other.Constant;
import com.spectre.utility.NotificationHelper;
import com.spectre.utility.Utility;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private Context context;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        if (!Utility.getSharedPreferencesBoolean(context, Constant.is_channel_prepared) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
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
        Log.d("Token", Utility.getSharedPreferences(context.getApplicationContext(), Constant.USER_TOKEN) + "");
        if (i == 0) {
            i++;
            String s = Utility.getSharedPreferences(context, Constant.USER_TYPE);
            if (s != null && !s.isEmpty()) {
                if (s.equalsIgnoreCase("1")) {
                    startActivity(new Intent(context, HomeActivity.class).putExtra(Constant.TYPE, "1"));
                } else {
                    //startActivity(new Intent(context, GarageHomeActivity.class));
                    Intent intent = new Intent(context, HomeActivity.class).putExtra(Constant.TYPE, "2");
                    startActivity(intent);
                }
                //   startActivity(new Intent(context, HomeActivity.class).putExtra(Constant.TYPE, "1"));
            } else {
                startActivity(new Intent(context, HomeActivity.class).putExtra(Constant.TYPE, "0"));
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
