package com.spectre.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.spectre.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginDialog extends Dialog {


    private Context context;
    public OnDialogItemClickListener listener;
    private String message;

    public LoginDialog(Context context, String message, OnDialogItemClickListener listener) {
        super(context, R.style.DialogSlideAnim);
        this.context = context;
        this.listener = listener;
        this.message = message;
    }

    public interface OnDialogItemClickListener {
        void onItemClick(boolean isLogout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login);

        getWindow().setGravity(Gravity.CENTER);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnLogin, R.id.txtSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                listener.onItemClick(false);
                break;
            case R.id.txtSignUp:
                listener.onItemClick(true);
                break;
        }
    }
}
