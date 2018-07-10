package com.spectre.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.beans.CarName;
import com.spectre.beans.ImageData;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
import com.spectre.customView.AlertBox;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.fragment.WorkBasicFragment;
import com.spectre.fragment.WorkDescriptionFragment;
import com.spectre.fragment.WorkImagesFragment;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.DepthPageTransformer;
import com.spectre.utility.NonSwipeableViewPager;
import com.spectre.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddWorkSectionActivity extends AppCompatActivity {

    private Context context;
    private CustomViewPagerAdapter customViewPagerAdapter;
    private NonSwipeableViewPager viewPager;
    private ActionBar actionBar;
    private RelativeLayout rlProgressBar;
    private Fragment workBasic, workDescription, workImages;
    Handler mHandler = new Handler();
    private ProgressView progressPvLinearColors;
    private AlertBox alertBox;
    public JSONObject jsonObject = new JSONObject();
    public boolean apiCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_add_work_section);
        context = this;
        Utility.setContentView(context, R.layout.activity_add_work_section);
        actionBar = setUpToolbar_(context, getString(R.string.add_work), true);
        initView();
    }

    private void initView() {
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        progressPvLinearColors = ((ProgressView) findViewById(R.id.progress_pv_linear_colors));
        alertBox = new AlertBox(context);
        //   rlProgressBar = (RelativeLayout) findViewById(R.id.rl_progress_bar);
        //   rlProgressBar.getBackground().setLevel(0);
        // viewPager.setPageTransformer(true, new DepthPageTransformer());
        customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(customViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (workDescription != null) {
                    ((WorkDescriptionFragment) workDescription).clearFocus();
                }

                try {
                    final InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void submitForm() {

        if (workBasic != null) {
            ((WorkBasicFragment) workBasic).getData();
            /*if (jsonObject == null) {
                setPosition(0, false);
            }*/
        } else {

        }


        if (workDescription != null) {
            ((WorkDescriptionFragment) workDescription).getData();
            /*if (jsonObject == null) {
                setPosition(1, false);
            }*/
        } else {

        }


        if (workImages != null) {
            ((WorkImagesFragment) workImages).getData();
            //  setPosition(1, false);
        } else {

        }

        if (jsonObject.length() == 16) {
            callApi(jsonObject);
        }

    }


    private class CustomViewPagerAdapter extends FragmentStatePagerAdapter {

        public CustomViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (workBasic == null)
                    workBasic = new WorkBasicFragment();
                return workBasic;
            } else if (position == 1) {
                if (workDescription == null)
                    workDescription = new WorkDescriptionFragment();
                return workDescription;
            } else if (position == 2) {
                if (workImages == null)
                    workImages = new WorkImagesFragment();
                return workImages;
            }

            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_UNCHANGED;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            if (position == 0) {
                workBasic = (WorkBasicFragment) createdFragment;
                return workBasic;
            } else if (position == 1) {
                workDescription = (WorkDescriptionFragment) createdFragment;
                return workDescription;
            } else if (position == 2) {
                workImages = (WorkImagesFragment) createdFragment;
                return workImages;
            }
            return null;
        }
    }

    public void setPosition(int position, boolean animate) {

        viewPager.setCurrentItem(position, animate);

        if (position == 0) {
            //   ((ProgressView) findViewById(R.id.progress_pv_linear_colors)).setProgress(0);
            setLevel(0, animate);
        } else if (position == 1) {
            setLevel(.35f, animate);
            //    ((ProgressView) findViewById(R.id.progress_pv_linear_colors)).setProgress(.3f);
            // setLevel(3300,animate);
        } else if (position == 2) {
            // ((ProgressView) findViewById(R.id.progress_pv_linear_colors)).setProgress(.6f);
            setLevel(.70f, animate);
        }
    }

    private void setLevel(float level, boolean reverse) {
        if (!reverse) {

         /*   for (int i = level; i > 0; i++) {
                final int finalI = i;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlProgressBar.getBackground().setLevel(finalI);
                    }
                }, 100);
            }*/
        } else {
            /*for (int i = 0; i <= level; i++) {
                final int finalI = i;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlProgressBar.getBackground().setLevel(finalI);
                    }
                }, 100);
            }*/
        }
        progressPvLinearColors.setProgress(level);
    }


    public ActionBar setUpToolbar_(Context appContext, String title, boolean home) {
        final AppCompatActivity activity = (AppCompatActivity) appContext;
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(home);
        actionBar.setHomeButtonEnabled(false);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        // ((CustomTextView) activity.findViewById(R.id.headet_text)).setText(title);
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setHomeAsUpIndicator(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition();
            }
        });
        return actionBar;
    }

    private void selectPosition() {
        if (viewPager.getCurrentItem() > 0) {
            setPosition(viewPager.getCurrentItem() - 1, false);
        } else {
            finish();
        }
    }


    @Override
    public void finish() {
        if (apiCalled) {
            super.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            if (viewPager.getCurrentItem() > 0) {
                setPosition(viewPager.getCurrentItem() - 1, false);
            } else {
                super.finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }
    }

    private void closeKeyboard() {
        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callApi(JSONObject jsonObject) {

        MyDialogProgress.open(context);

        new AqueryCall(this).postWithJsonToken(Urls.GARAGE_WORK, Utility.getSharedPreferences(context, Constant.USER_TOKEN), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                setNewData(js, msg);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                //alertBox.openMessage(msg, "Ok", "", false);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
               /* Intent intent = new Intent(context, AuthDialogActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);*/

                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                //alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                // alertBox.openMessage(msg, "Ok", "", false);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                // MyDialogProgress.close(context);
            }
        });
    }

    private void setNewData(JSONObject js, String msg) {
        apiCalled = true;
        alertBox.openMessageWithFinish(msg, "Okay", "", false);
        MyDialogProgress.close(context);
    }
}
