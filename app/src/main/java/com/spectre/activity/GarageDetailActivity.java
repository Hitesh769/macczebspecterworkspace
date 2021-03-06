package com.spectre.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.daimajia.slider.library.SliderLayout;
import com.spectre.R;
import com.spectre.activity_new.BookCarSummaryActivity;
import com.spectre.beans.Garage;
import com.spectre.beans.Review;
import com.spectre.beans.Work;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GarageDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ActionBar actionBar;
    private ImageView imv_banner;
    private Garage adPost;
   /* private WorkList workList;*/
    private CustomRayMaterialTextView btn_show_interest, btn_review;
    private int position = -1, type = -1;
    private Display display;
    private SliderLayout imageSlider;
    ArrayList<Review> ReviewList = new ArrayList<>();
    ArrayList<Work> WorkList = new ArrayList<>();
    private boolean isChanged;

    RecyclerView galleryRecyclerView, reviewRecyclerView;
    private LinearLayoutManager gLayoutManager, rLayoutManager;
    private RecyclerView.Adapter gAdapter, rAdapter;
    private CustomTextView txt_gallery_load, txt_review_load, no_review_found, no_gallery_found;
    private RelativeLayout head_gallery, head_review;
    //  private int reviewIsThere = -1;
    private Dialog dd;
    CircleImageView iv_profile;
    CustomTextView txt_email_id, txt_adress, txt_contact, txt_expertise, txt_repair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_garage_detail);
        context = this;
        Utility.setContentView(context, R.layout.activity_garage_details_new);
      //  actionBar = Utility.setUpToolbarGarage(context, "<font color='#ffffff'>Garage Detail</font>", true,adPost.getCompany_logo());
        // Utility.setUpToolbar_(context, "<font color='#ffffff'>"+getString(R.string.manage_ad)+"</font>",true);
        initView();
    }

    private void initView() {

        imageSlider = (SliderLayout) findViewById(R.id.slider);
        //txt_car_name = (CustomTextView) findViewById(R.id.txt_car_name);
        txt_email_id = (CustomTextView) findViewById(R.id.txt_email_id);
        txt_adress = (CustomTextView) findViewById(R.id.txt_adress);
        txt_contact = (CustomTextView) findViewById(R.id.txt_contact);
        txt_expertise = (CustomTextView) findViewById(R.id.txt_expertise);
        no_review_found = (CustomTextView) findViewById(R.id.no_review_found);
        no_gallery_found = (CustomTextView) findViewById(R.id.no_gallery_found);
        txt_repair = (CustomTextView) findViewById(R.id.txt_repair);
        iv_profile = (CircleImageView) findViewById(R.id.iv_profile);
        head_gallery = (RelativeLayout) findViewById(R.id.head_gallery);
        head_review = (RelativeLayout) findViewById(R.id.head_review);
        imv_banner = (ImageView) findViewById(R.id.imv_banner);
        btn_show_interest = (CustomRayMaterialTextView) findViewById(R.id.btn_show_interest);
        btn_review = (CustomRayMaterialTextView) findViewById(R.id.btn_review);


        iv_profile = (CircleImageView) findViewById(R.id.iv_profile);

        btn_show_interest = (CustomRayMaterialTextView) findViewById(R.id.btn_show_interest);
        btn_show_interest.setOnClickListener(this);


        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GarageDetailActivity.this, SellerDetailsActivity.class);
                intent.putExtra(Constant.DATA, adPost);
                intent.putExtra(Constant.ISGARAGE,"YES");
                intent.putExtra(Constant.POSITION, position);
                intent.putExtra(Constant.TYPE, type);
                startActivity(intent);
            }
        });

        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {

         /*   if (getIntent().getStringExtra("myprofile")!=null){
               // adPost = (WorkList) getIntent().getExtras().get(Constant.DATA);
            }*/
            adPost = (Garage) getIntent().getExtras().get(Constant.DATA);
            actionBar = Utility.setUpToolbarGarage(context, adPost.getCompany_name(), true,adPost.getCompany_logo());

            position = getIntent().getExtras().getInt(Constant.POSITION);
            type = getIntent().getExtras().getInt(Constant.TYPE);
            display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

         /*   android.view.ViewGroup.LayoutParams layoutParams = imageSlider.getLayoutParams();
            layoutParams.width = display.getWidth();
            layoutParams.height = Utility.dpToPx(context, 250);
            imageSlider.setLayoutParams(layoutParams);*/

            android.view.ViewGroup.LayoutParams layoutParams = imv_banner.getLayoutParams();
            layoutParams.width = display.getWidth();
            layoutParams.height = Utility.dpToPx(context, 250);
            imv_banner.setLayoutParams(layoutParams);


            if (adPost.getEmail() != null && !adPost.getEmail().isEmpty()) {
                txt_email_id.setText(adPost.getEmail().trim());
            } else {
                txt_email_id.setText(context.getString(R.string.na));
            }

          /*  if (adPost.getEmail() != null && !adPost.getEmail().isEmpty()) {
                txt_email_id.setText(adPost.getEmail().trim());
            } else {
                txt_email_id.setText(context.getString(R.string.na));
            }
*/
            if (adPost.getExpertise() != null && !adPost.getExpertise().isEmpty()) {
                txt_expertise.setText(adPost.getExpertise().trim());
            } else {
                txt_expertise.setText(context.getString(R.string.na));
            }


            if (adPost.getCar_repaire() != null && !adPost.getCar_repaire().isEmpty()) {
                txt_repair.setText(adPost.getCar_repaire().trim());
            } else {
                txt_repair.setText(context.getString(R.string.na));
            }


            if (adPost.getMobile_no() != null && !adPost.getMobile_no().isEmpty()) {
                String mobile = adPost.getMobile_code() + "" + adPost.getMobile_no().trim();
                txt_contact.setText(mobile);
            } else {
                txt_contact.setText(context.getString(R.string.na));
            }

            if (adPost.getAddress() != null && !adPost.getAddress().isEmpty()) {
                txt_adress.setText(adPost.getAddress().trim());
            } else {
                txt_adress.setText(context.getString(R.string.na));
            }

            if (adPost.getCompany_logo() != null && !adPost.getCompany_logo().isEmpty()) {
                new AQuery(context).id(iv_profile).image(adPost.getCompany_logo(), true, true, 0, R.mipmap.gestuser);
            } else
                iv_profile.setImageResource(R.mipmap.gestuser);

           /* if (adPost.getImage().size() == 0) {
                Utility.setUpViewPagerNew(imageSlider, context);
            } else {
                for (String s : adPost.getImage()) {
                    url_from_api.put(s, s);
                }
                Utility.setUpViewPager(imageSlider, context, url_from_api, "0");
            }*/
            if (adPost.getGarage_image() != null && !adPost.getGarage_image().isEmpty()) {
                new AQuery(context).id(imv_banner).image(adPost.getGarage_image(), true, true, 0, R.drawable.ic_launcher_web);

                imv_banner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ZoomActivity.class);
                        intent.putExtra(Constant.IMAGE, adPost.getGarage_image());
                        startActivity(intent);
                    }
                });
            } else
                imv_banner.setImageResource(R.drawable.ic_launcher_web);

        } else {
            new AlertBox(context).openMessageWithFinish(getResources().getString(R.string.something_wrong), "Okay", "", false);
        }
    }


    private void setReviewData() {
        if (adPost.getMyreviews() != null && !adPost.getMyreviews().isEmpty()) {
            btn_review.setText(getString(R.string.edit_review));
        } else {
            btn_review.setText(getString(R.string.add_review));
        }

        if (ReviewList.size() > 0) {
            txt_review_load.setVisibility(View.VISIBLE);
            no_review_found.setVisibility(View.GONE);
                /*for (int i = 0; i <= ReviewList.size() - 1; i++) {
                    if (ReviewList.get(i).getUser_id().equalsIgnoreCase(SharedPrefUtils.getPreference(context, Constant.USER_ID))) {
                        reviewIsThere = i;
                    }
                }*/
        } else {
            txt_review_load.setVisibility(View.GONE);
            no_review_found.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish() {
        try {
            if (isChanged) {
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

    @Override
    public void onClick(View v) {
        String s = SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "");
        switch (v.getId()) {
           /* case R.id.head_gallery:
                if (WorkList.size() > 0)
                    startActivity(new Intent(context, WorkListUserActivity.class).putExtra(Constant.DATA, adPost.getUser_id()));
                break;
            case R.id.head_review:
                if (ReviewList.size() > 0)
                    startActivity(new Intent(context, ReviewListActivity.class).putExtra(Constant.DATA, adPost.getUser_id()));
                break;
            case R.id.btn_review:
                if (s.isEmpty() || s.equalsIgnoreCase("0")) {
                    Utility.openDialogToLogin(context);
                } else {
                    startActivityForResult(new Intent(context, AddReviewActivity.class).putExtra(Constant.DATA, adPost), 404);
                }

                break;
           */
            case R.id.btn_show_interest:

                if (s.isEmpty() || s.equalsIgnoreCase("0")) {
                    Utility.openDialogToLogin(context);
                } else {
                    if (!btn_show_interest.getText().toString().equalsIgnoreCase(getString(R.string.request_service_)))
                        //  showProblem();
                        callResetAPI(dd, "");
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mymenu = getMenuInflater();
        mymenu.inflate(R.menu.menu_notification, menu);
       // mymenu = (MenuInflater) menu.findItem(R.id.gallary);
        if (!adPost.getGarage_image().isEmpty()) {
         menu.getItem(0).setVisible(false);
        }
            return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                //startActivity(new Intent(context, ReviewListActivity.class));
                Intent intent = new Intent(context, ReviewListActivity.class);
                intent.putExtra(Constant.DATA, adPost);
                intent.putExtra(Constant.POSITION, position);
                context.startActivity(intent);
                break;
            case R.id.gallary:
                //startActivity(new Intent(context, ReviewListActivity.class));
                Intent intent1 = new Intent(context, WorkListUserActivity.class);
                intent1.putExtra(Constant.DATA, adPost);
                intent1.putExtra(Constant.POSITION, position);
                context.startActivity(intent1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 404) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Garage adPost1 = data.getParcelableExtra(Constant.DATA);
                    adPost.setMyreviews(adPost1.getMyreviews());
                    adPost.setMyrating(adPost1.getMyrating());

                    if (adPost1.getReviews() != null && adPost1.getReviews().size() > 0) {
                        ReviewList.clear();
                        ReviewList.addAll(adPost1.getReviews());
                        adPost.setReviews(adPost1.getReviews());
                        rAdapter.notifyDataSetChanged();
                    }
                    isChanged = true;

                    //setReviewData();
                    //  resetData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  Arraylist.clear();
                //  callMethodEventList(0);
            }
        }
    }

    private void showProblem() {

        try {
            if (dd == null) {
                dd = new Dialog(context);
                dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_send_request);
            dd.setCancelable(true);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dd.show();

            CustomTextView view = dd.findViewById(R.id.view);
            LinearLayout llDate = dd.findViewById(R.id.ll_date);


            view.setVisibility(View.VISIBLE);
            llDate.setVisibility(View.GONE);


            final CustomEditText etMobileNumber = (CustomEditText) dd.findViewById(R.id.et_problem);


            ((CustomRayMaterialTextView) dd.findViewById(R.id.btn_save_changes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etMobileNumber.setError(null);

                  /*  if (etMobileNumber.getText().toString().trim().isEmpty()) {
                        etMobileNumber.setError(getString(R.string.char_count_msg_error));
                        return;
                    }

                    if (etMobileNumber.getText().toString().trim().length() < 50) {
                        etMobileNumber.setError(getString(R.string.char_count_msg_error));
                        return;
                    }*/


                    MyDialogProgress.open(context);
                    callResetAPI(dd, etMobileNumber.getText().toString().trim());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callResetAPI(final Dialog dd, final String trim) {

        try {
            //Log.e("url.....", "" + url + " " + jsonInput);
            JSONObject jsonInput = new JSONObject();
            jsonInput.put(Constant.PROBLEM, trim);
            jsonInput.put(Constant.SECOND_USER_ID, adPost.getUser_id());
            jsonInput.put(Constant.INTERESTED_ID, adPost.getAdd_id());
            jsonInput.put(Constant.TYPE, type == 0 ? 1 : 2);

            new AQuery(context).post(Urls.FORGOT_PASSWORD, jsonInput, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //  Log.e("result.....", "" + json);

                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                //  request.onSuccess(json, Message);
                                MyDialogProgress.close(context);
                              //  dd.dismiss();
                            } else {
                                //  request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                                showToast(Message);
                                MyDialogProgress.close(context);
                            }
                        } catch (Exception e1) {
                            // request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            showToast(getString(R.string.something_wrong));
                            MyDialogProgress.close(context);

                            e1.printStackTrace();
                        }
                    } else {
                        MyDialogProgress.close(context);
                        //nullCase(status, AjaxStatus.NETWORK_ERROR);
                    }
                }
            });

            new AqueryCall(this).postWithJsonToken(Urls.INTEREST_GARAGE, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonInput, new RequestCallback() {
                @Override
                public void onSuccess(JSONObject js, String msg) {
                    MyDialogProgress.close(context);
                   // dd.dismiss();
                    setInterestData();
                    Utility.contectDialog(adPost.getMobile_no(),GarageDetailActivity.this,adPost.getUser_id(),adPost.getFull_name());
                }

                @Override
                public void onFailed(JSONObject js, String msg) {
                    showToast(msg);
                    if (msg.equalsIgnoreCase("already interested.")) {
                        Utility.contectDialog(adPost.getMobile_no(), GarageDetailActivity.this,adPost.getUser_id(),adPost.getFull_name());
                    }
                    MyDialogProgress.close(context);
                }

                @Override
                public void onAuthFailed(JSONObject js, String msg) {
                    MyDialogProgress.close(context);
                    SessionExpireDialog.openDialog(context, 0, "");
                }

                @Override
                public void onNull(JSONObject js, String msg) {
                    showToast(msg);
                    MyDialogProgress.close(context);
                }

                @Override
                public void onException(JSONObject js, String msg) {
                    showToast(msg);
                    MyDialogProgress.close(context);
                }

                @Override
                public void onInactive(JSONObject js, String inactive, String status) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showToast(context, getString(R.string.something_wrong));
            MyDialogProgress.close(context);
        }
    }

    private void setInterestData() {
        adPost.setIs_interest("1");
        isChanged = true;
        if (adPost.getIs_interest() != null && !adPost.getIs_interest().isEmpty() && adPost.getIs_interest().equalsIgnoreCase("1")) {
            btn_show_interest.setText(R.string.request_service_);
        } else {
            btn_show_interest.setText(R.string.request_service);
        }
    }

    private void showToast(String msg) {
        Utility.showToast(context, msg);
    }


}
