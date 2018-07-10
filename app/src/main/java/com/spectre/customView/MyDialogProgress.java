package com.spectre.customView;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.RelativeLayout;

import com.rey.material.widget.ProgressView;
import com.spectre.R;


/**
 * Created by ebabu on 3/12/15.
 */
public class MyDialogProgress {
    static Dialog dd = null;
    static ProgressView progressDialog;

    public static Dialog open(Context context) {
        dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.progress_dialog);
            progressDialog = (ProgressView) dd.findViewById(R.id.progress_pv_circular_inout);
            progressDialog.start();
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.show();
            dd.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd;
    }

    public static Dialog openHome(Context context) {
        dd = new Dialog(context);
        try {
            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.progress_dialog);
            ((CustomTextView)dd.findViewById(R.id.txt_msg)).setText("Setting up your account...");
            progressDialog = (ProgressView) dd.findViewById(R.id.progress_pv_circular_inout);
            progressDialog.start();
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.show();
            dd.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dd;
    }

    public static void close(Context context) {
        try {
            if (dd != null && dd.isShowing() ) {
                if (progressDialog != null)
                    progressDialog.stop();
                dd.dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static boolean isOpen(Context context) {
        if (dd != null&&dd.isShowing()) {
            return true;
        }
        return false;
    }
}
