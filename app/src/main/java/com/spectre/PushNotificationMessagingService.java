package com.spectre;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.spectre.activity.NotificationActivity;
import com.spectre.activity.SplashActivity;
import com.spectre.other.Constant;
import com.spectre.utility.NotificationHelper;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;


public class PushNotificationMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    //public MyPrefrence prefrence;
    boolean status;
    private String type = "0";
    private String subType = "0";
    private int count = 0;
    String name, likeId, userId, contactNo;
    String channelName = "";// The user-visible name of the channel.
    String channelId = "";// The user-visible name of the channel.


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
      /*  try {
            Log.d("PUSH_NOTIFICATION", String.valueOf(remoteMessage));
            JSONObject obj = new JSONObject(remoteMessage.getData().get("message"));
            Log.d("PUSH_NOTIFICATION", obj.toString());
            sendNotification(obj.getString("title"), obj.getString("message"));

            if (Utility.getSharedPreferencesBoolean(getApplicationContext(), Constant.LOGIN_PREF)) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        //   {id=94, msg=Navin like your testing post, type=1, image=http://callmateaap.com/callmate_app/post_images/3a3daa38e71c45236803a3d5436ad57d.jpg}
        //   prefrence = new MyPrefrence(getApplicationContext());
        Log.d("PUSH_NOTIFICATION", String.valueOf(remoteMessage));
        try {

            // if (!Utility.getSharedPreferencesBoolean_(getApplicationContext(), Constant.APP_NOTIFICATION)) {
            if (!SharedPrefUtils.getPreference(getApplicationContext(), Constant.APP_NOTIFICATION, true)) {
                return;
            }

            if (remoteMessage.getData().containsKey("type")) {
                type = remoteMessage.getData().get("type");

                if (type.equalsIgnoreCase("1")) {
                    channelId = NotificationHelper.PRIMARY_CHANNEL;
                } else if (type.equalsIgnoreCase("2")) {
                    channelId = NotificationHelper.SECONDARY_CHANNEL;
                } else if (type.equalsIgnoreCase("3")) {
                    channelId = NotificationHelper.TERTIARY_CHANNEL;
                } else {
                    channelId = "4";
                }
            }

            if(remoteMessage.getData().containsKey("type")){
                type = remoteMessage.getData().get("type");
            }
            //  if(type.equalsIgnoreCase("2")&&)
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("image"), remoteMessage.getData().get("msg"));

        } catch (Exception e) {
            e.printStackTrace();
        }


       /* Log.d("PUSH_NOTIFICATION", String.valueOf(remoteMessage));
        type = remoteMessage.getData().get("type");
        if (type.equalsIgnoreCase("2") && BuildConfig.VERSION_CODE < Integer.parseInt(remoteMessage.getData().get("message_id"))) {
            sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("image"), remoteMessage.getData().get("message"), remoteMessage.getData().get("message_id"), remoteMessage.getData().get("type"), remoteMessage.getData().get("user_id"));
        } else {
            if (Utility.getSharedPreferencesBoolean(getApplicationContext(), Constant.ISLOGIN)) {
                try {
                    sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("image"), remoteMessage.getData().get("message"), remoteMessage.getData().get("message_id"), remoteMessage.getData().get("type"), remoteMessage.getData().get("user_id"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/


      /*  // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        sendNotification("Great Lakes", remoteMessage.getNotification().getBody());*/

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts 
    private void sendNotification(String title, String image, String messageBody) {
        long when = System.currentTimeMillis();
        Bitmap bitmap = null;
        Intent intent = null;
        /*NotificationDetail notificationDetail=null;
        int count;
        try {
             notificationDetail = new NotificationDetail(section_id, UserID, type);
        } catch (Exception e) {

        }*/
       /* if (type.equalsIgnoreCase("2")) {
            try {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName()));
            } catch (android.content.ActivityNotFoundException anfe) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()));
                //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            } catch (Exception e) {
                return;
            }
        } else {

        }*/
/*        if (type != null && !type.equalsIgnoreCase("1")) {
            intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.TYPE, type);
            //  intent.putExtra(Constant.TYPE, type);
        } else if (type != null && !type.equalsIgnoreCase("2")) {
            intent = new Intent(this, HintMessagingActivity.class);

            intent.putExtra(Constant.LIKE_ID, likeId);
            intent.putExtra(Constant.CONTACT_NO, contactNo);
            intent.putExtra(Constant.USER_ID, userId);
            intent.putExtra(Constant.CONTACT_NAME, name);
            intent.putExtra(Constant.YES, "");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.TYPE, type);
            //  intent.putExtra(Constant.TYPE, type);
        } else {
            intent = new Intent(this, SplashMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }*/

        intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  intent.putExtra(Constant.NOTIFICATION, notificationDetail);


        try {
            if (image != null || !image.isEmpty())
                bitmap = Utility.getBitmapFromUrl(getApplication(), image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(type), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //  intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);



       /* RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_item);
        remoteViews.setTextViewText(R.id.txt_emoji,Html.fromHtml(messageBody) + "");*/
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(Html.fromHtml(messageBody) + "")
                // .setContent(remoteViews)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(channelId);
        }

        if (bitmap != null) {
            notificationBuilder.setLargeIcon(bitmap);
        }

        notificationBuilder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_white).setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        }


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       /* count = Utility.getIngerSharedPreferences(getApplicationContext(), Constant.NOTI_COUNT);
        Utility.setIntegerSharedPreference(getApplicationContext(), Constant.NOTI_COUNT, ++count);*/
        // SharedPreferencesMethod.setInt(getApplicationContext(), SharedPreferencesMethod.NO_OF_NOTIFICATION, SharedPreferencesMethod.getInt(getApplicationContext(), SharedPreferencesMethod.NO_OF_NOTIFICATION) + 1);
        String s = SharedPrefUtils.getPreference(getApplicationContext(), Constant.USER_TYPE, "");
        if (!s.equalsIgnoreCase("") || !s.equalsIgnoreCase("0")) {
            notificationBuilder.setDefaults(android.app.Notification.DEFAULT_SOUND);
            notificationBuilder.setDefaults(android.app.Notification.DEFAULT_LIGHTS);
            notificationBuilder.setDefaults(android.app.Notification.DEFAULT_VIBRATE);
            notificationBuilder.setDefaults(android.app.Notification.FLAG_ONLY_ALERT_ONCE);
            notificationBuilder.setDefaults(android.app.Notification.PRIORITY_MAX);
            int types = 0;
            try {
                types = (Integer.parseInt(subType));
            } catch (Exception e) {

            }
            //  notificationManager.notify((int)when, notificationBuilder.build());
            notificationManager.notify(types, notificationBuilder.build());
        }
    }

   /* @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
       Log.d("Yo yo yo yo yo","Yo yo yo yo yo");
    }*/
}