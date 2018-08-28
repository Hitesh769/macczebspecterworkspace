package com.spectre.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.rey.material.widget.ProgressView;
import com.spectre.R;

public class ProgressDialog {
    /*public static Dialog createProgressDialog(Context context) {
        try {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View view = View.inflate(context, R.layout.dialog_loading, null);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            return dialog;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }*/

    public static Dialog createProgressDialog(Context context) {
        try {
            Dialog dd = new Dialog(context);
            ProgressView progressDialog;

            dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.progress_dialog);
            progressDialog = (ProgressView) dd.findViewById(R.id.progress_pv_circular_inout);
            progressDialog.start();
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);
            // dd.show();
            return dd;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
