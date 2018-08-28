package com.spectre.customView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;

import com.spectre.R;
import com.spectre.activity.AddReviewActivity;
import com.spectre.activity.AddWorkActivity;
import com.spectre.activity.AddWorkSectionActivity;
import com.spectre.activity.CarDetailActivity;
import com.spectre.activity.ChangePasswordActivity;
import com.spectre.activity.EditProfileActivity;
import com.spectre.activity.GarageDetailActivity;
import com.spectre.activity.LoginActivity;
import com.spectre.activity.NotificationDetailActivity;
import com.spectre.activity.PostAdActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.utility.Utility;


/**
 * Created by pc on 07-09-2017.
 */
public class AlertBox {
    private Context context;
    android.support.v7.app.AlertDialog alertDialog;
    android.support.v7.app.AlertDialog.Builder builder;
    Dialog dialogLogout = null;


    public AlertBox(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
    }


    public void openDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {

        } else {
            builder.setTitle("Logout");
            builder.setMessage("Do you want to logout?");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utility.resetPreferences(context);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
    }

    public void openMessage(String msg, String positive, String negative, boolean isNegVisible) {
        if (alertDialog != null && alertDialog.isShowing()) {

        } else {
            //  builder.setTitle("Logout");
            builder.setMessage(Html.fromHtml(msg));
            builder.setCancelable(true);
            builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //  Utility.resetPreferences(context);
                }
            });

            if (isNegVisible) {
                builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }

            alertDialog = builder.create();
            alertDialog.show();

            if (!isNegVisible) {
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
            }

            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
    }


    public void openMessageWithFinish(String msg, String positive, String negative, boolean isNegVisible) {
        if (alertDialog != null && alertDialog.isShowing()) {

        } else {
            //  builder.setTitle("Logout");
            builder.setMessage(msg);
            builder.setCancelable(false);
            builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //  Utility.resetPreferences(context);
                    /*if (context instanceof MessageDetailActivity) {
                        ((MessageDetailActivity) context).finish();
                    }else if(context instanceof HomeFormatActivity){
                        ((HomeFormatActivity) context).finish();
                    }else*/
                    if (context instanceof LoginActivity) {
                        ((LoginActivity) context).finish();
                    } else if (context instanceof PostAdActivity) {
                        ((PostAdActivity) context).finish();
                    } else if (context instanceof RentCarActivity) {
                        ((RentCarActivity) context).finish();
                    } else if (context instanceof EditProfileActivity) {
                        ((EditProfileActivity) context).finish();
                    }else if (context instanceof AddWorkActivity) {
                        ((AddWorkActivity) context).finish();
                    }else if (context instanceof CarDetailActivity) {
                        ((CarDetailActivity) context).finish();
                    }else if (context instanceof GarageDetailActivity) {
                        ((GarageDetailActivity) context).finish();
                    }else if (context instanceof AddReviewActivity) {
                        ((AddReviewActivity) context).finish();
                    }else if (context instanceof NotificationDetailActivity) {
                        ((NotificationDetailActivity) context).finish();
                    }else if (context instanceof AddWorkSectionActivity) {
                        ((AddWorkSectionActivity) context).finish();
                    }else if (context instanceof ChangePasswordActivity) {
                        ((ChangePasswordActivity) context).finish();
                    }
                }
            });
            if (isNegVisible) {
                builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }

            alertDialog = builder.create();
            alertDialog.show();

            if (!isNegVisible) {
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
            }
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));

        }
    }


    public void openDialogImage() {
        if (alertDialog != null && alertDialog.isShowing()) {

        } else {
            builder.setTitle("");
            //   builder.setTitle("Logout");
            builder.setMessage("Take picture using");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (context instanceof EditProfileActivity) {
                        ((EditProfileActivity) context).pickFromCamera();
                    }
                }
            });

            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (context instanceof EditProfileActivity) {
                        ((EditProfileActivity) context).pickFromGallery();
                    }/* else if (context instanceof HomeFormatActivity) {
                        ((HomeFormatActivity) context).pickFromGallery();
                    }*/
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
    }
}
