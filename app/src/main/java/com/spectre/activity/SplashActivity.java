package com.spectre.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.spectre.R;
import com.spectre.activity_new.HomeAct;
import com.spectre.activity_new.MasterAppCompactActivity;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.ChatService;
import com.spectre.utility.NotificationHelper;
import com.spectre.utility.SharedPrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
                //getChatList(SplashActivity.this);
                Intent intent=new Intent(getBaseContext(),ChatService.class);
                startService(intent);
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
    private void getChatList(final Context context) {
        MyDialogProgress.close(context);
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.TO_ID, SharedPrefUtils.getPreference(context, Constant.USER_ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Urls.GETCHATCOUNT, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                try {
                    if (js.getString("status").equals("success")) {
                        String datacount=js.getString("data");
                        //  Toast.makeText(getApplicationContext(), datacount+" "+"message is unread..", Toast.LENGTH_LONG).show();
                        Log.i("Chat service",datacount+" "+"message is unread..");
                        MyDialogProgress.close(context);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Chat service ERROR",e.toString());
                    MyDialogProgress.close(context);
                }
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Log.i("Chat service ERROR",msg);
                // Utility.showToast(context, msg);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Log.i("Chat service ERROR",msg);
                //Utility.showToast(context, msg);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Log.i("Chat service ERROR",msg);
                // Utility.showToast(context, msg);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {

            }
        });
    }
}
