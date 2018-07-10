package com.spectre.activity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;
import com.androidquery.AQuery;

import com.spectre.R;
import com.spectre.other.Constant;
import com.spectre.utility.TouchImageView;
import com.spectre.utility.Utility;

public class ZoomActivity extends Activity {
    private Context appContext;
    private Bitmap bitmap;
    private TouchImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_zoom);
       // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        appContext = this;
        String value = getIntent().getExtras().getString(Constant.IMAGE);
        try {
            img = new TouchImageView(appContext);
            img.setBackgroundColor(getResources().getColor(R.color.black));
            if (Utility.isConnectingToInternet(appContext)) {
                //new LoadImage().execute(value);
                //new AQuery(appContext).id(img).image(value, true, true, 500, 0,);
                new AQuery(appContext).id(img).image(value, true, true, 500, R.drawable.ic_launcher_web);

                img.setMaxZoom(15f);
                setContentView(img);
            } else {
                Toast.makeText(appContext, getString(R.string.connection), Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void finish() {
        super.finish();
     //   overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


//    private class LoadImage extends AsyncTask<String, String, Bitmap> {
//        MyLoading loading = new MyLoading();
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loading.setUpDialog(appContext);
//        }
//
//        protected Bitmap doInBackground(String... args) {
//            try {
//                bitmap = Utils.getBitmap(args[0]);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            } catch (Error e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        protected void onPostExecute(Bitmap image) {
//            loading.hide();
//
//            if (image != null) {
//                img.setImageBitmap(image);
//                img.setMaxZoom(15f);
//                setContentView(img);
//            } else {
//                Toast.makeText(appContext, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }
}
