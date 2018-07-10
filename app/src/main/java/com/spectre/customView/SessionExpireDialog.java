package com.spectre.customView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.spectre.R;
import com.spectre.utility.Utility;


/**
 * Created by pc on 07-09-2017.
 */
public class SessionExpireDialog {
    static AlertDialog alertDialog;
    static AlertDialog.Builder builder;


    public static void openDialog(final Context context, int type, String msg) {
        String title = "";
        String msgs = "";
        if (type == 0) {
            title = context.getString(R.string.login_expire);
            msgs = context.getString(R.string.login_msg);
        } else {
            title = "";
            msgs = msg;
        }


        try {
            builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setCancelable(false);
            builder.setMessage(msgs);
            builder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Utility.resetPreferences(context);
                        }
                    }, 1000);
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
