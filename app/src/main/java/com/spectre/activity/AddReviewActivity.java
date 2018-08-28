package com.spectre.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;

import com.google.gson.Gson;
import com.spectre.R;
import com.spectre.beans.Garage;
import com.spectre.beans.UpdateReview;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomRayMaterialEditText;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class AddReviewActivity extends AppCompatActivity {

    private Context context;
    private ActionBar actionBar;
    private RatingBar ratingBar;
    private CustomRayMaterialEditText et_review_post;
    private int shopId = 0, userRating = 1;
    private AlertBox alertBox;
    private String second_id = "0";
    private Garage adPost;
    //   private int position = -1;
    private boolean apiHit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_add_review);
        context = this;
        Utility.setContentView(context, R.layout.activity_add_review);
        actionBar = Utility.setUpToolbar_(context, getString(R.string.add_review_), true);
        initView();
    }


    /*Define View, set old data if already given rating*/

    private void initView() {
        alertBox = new AlertBox(context);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        et_review_post = (CustomRayMaterialEditText) findViewById(R.id.et_review_post);
        ratingBar.setRating((float) userRating);

        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {
            adPost = (Garage) getIntent().getExtras().get(Constant.DATA);
            // position = getIntent().getExtras().getInt(Constant.isNew, -1);
            if (adPost.getMyreviews() != null && !adPost.getMyreviews().isEmpty()) {
                et_review_post.setText(adPost.getMyreviews());
                actionBar.setTitle(getString(R.string.edit_review_));
                et_review_post.setSelection(et_review_post.getText().length());
                if (!adPost.getMyrating().isEmpty()) {
                    userRating = Integer.parseInt(adPost.getMyrating());
                    ratingBar.setRating((float) userRating);
                } else {

                }

            } else {

            }
        }
        // et_review_post.setText(userReview);
    }

    /*Check validation before calling API*/
    public void checkValidation() {


        if (et_review_post.getText().toString().trim().isEmpty()) {
            Utility.showToast(context, getString(R.string.write_review_error));
            return;
        }

        if (et_review_post.getText().toString().trim().length() < 140) {
            Utility.showToast(context, getString(R.string.char_count_review_error));
            et_review_post.setText(et_review_post.getText().toString().trim());
            return;
        }

        if (ratingBar.getRating() <= 0) {
            Utility.showToast(context, getString(R.string.write_rating_error));
            return;
        }

        if (!Utility.isConnectingToInternet(context)) {
            Utility.showToast(context, getString(R.string.connection));
            return;
        }

        sendRateReview();

    }

    /*Call API to send review & rating to server*/

    private void sendRateReview() {
        MyDialogProgress.open(context);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.SECOND_USER_ID, adPost.getUser_id());
            jsonObject.put(Constant.RATING, (int) ratingBar.getRating());
            jsonObject.put(Constant.REVIEWS, et_review_post.getText().toString().trim().replaceAll("[\n]{3,}", "\n\n"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Urls.REVIEW_N_RATING, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                apiHit = true;
                setData(js, msg);

            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {

            }
        });
    }



    private void setData(JSONObject js, String msg) {
        // JSONArray jsonArray=js.getJSONArray();
        try {
            UpdateReview updateReview = new Gson().fromJson(js.getJSONObject("data").toString(), UpdateReview.class);
            if (updateReview != null) {
                adPost.setMyrating(updateReview.getMyrating());
                adPost.setMyreviews(updateReview.getMyreviews());
                if (updateReview.getReviews() != null && updateReview.getReviews().size() > 0)
                    adPost.setReviews(updateReview.getReviews());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*if (position != -1 && adPost.getReviews().size()>0) {
            try {
                adPost.getReviews().get(position).setReviews(review);
                adPost.getReviews().get(position).setRating(rating);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        alertBox.openMessageWithFinish(msg, "Okay", "", false);
        MyDialogProgress.close(context);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.submit:
                checkValidation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        try {
            if (apiHit) {
                Intent intent = new Intent();
                intent.putExtra(Constant.DATA, adPost);
                setResult(Activity.RESULT_OK, intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
