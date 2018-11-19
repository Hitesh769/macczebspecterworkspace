package com.spectre.utility;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.spectre.R;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatService extends Service {
    static String msgcount="0";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
        Log.i("Chat service","Service Created");

    }
   /* @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

    }*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
       // Toast.makeText(this, "Service StartedCommand", Toast.LENGTH_LONG).show();
        Log.i("Chat service","Service onStartCommand");

        getChatList(getApplicationContext());
        return Service.START_STICKY;
    }
    @Override
    public void onDestroy() {
      //  Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        Log.i("Chat service","Service Destroy");
    }

    private void getChatList(final Context context) {
        MyDialogProgress.close(context);
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.TO_ID, SharedPrefUtils.getPreference(context, Constant.USER_ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(context).postWithJsonToken(Urls.GETCHATCOUNT, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                try {
                    if (js.getString("status").equals("success")) {
                        String datacount=js.getString("data");

                            int intmsgcount=Integer.parseInt(msgcount);
                            int intdatacount=Integer.parseInt(datacount);
                            if (intmsgcount!=intdatacount) {
                                msgcount=datacount;
                                displayToast(context, datacount + " " + "unread messages..");
                                getChatList(context);
                            }

                      //  Toast.makeText(getApplicationContext(), datacount+" "+"message is unread..", Toast.LENGTH_LONG).show();
                        Log.i("Chat service",datacount+" "+"unread messages..");

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
                msgcount="0";
                getChatList(context);
                // Utility.showToast(context, msg);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                msgcount="0";
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Log.i("Chat service ERROR",msg);
                msgcount="0";
                getChatList(context);
                //Utility.showToast(context, msg);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                msgcount="0";
                Log.i("Chat service ERROR",msg);
                // Utility.showToast(context, msg);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                msgcount="0";
            }
        });
    }

    private void displayToast(Context context,String s) {
        LayoutInflater inflater =(LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        ImageView image = (ImageView) layout.findViewById(R.id.image);
       // image.setImageResource(R.mipmap.splashlogo);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(s);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }






}