package com.spectre.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.spectre.R;
import com.spectre.customView.RangeSeekBar;
import com.spectre.utility.Utility;

public class AboutUsActivity extends AppCompatActivity {
    private Context context;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_about_us);
        context = this;
        Utility.setContentView(context, R.layout.activity_about_us);
        //  Utility.setUpToolbar_(context, "<font color='#ffffff'>About Us</font>", true);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>"+getString(R.string.about_us)+"</font>", true);
        ((WebView) findViewById(R.id.web_view)).loadData("SpectreApp is the only marketplace for Dealers, Certified PreOwned Car owners and Individuals to list their services and portfolio and get discovered. Search, Compare and Save Using the UAE's Biggest Online Car Listing for Buying & Selling and Car Rental Services.", "text/html; charset=utf-8", "UTF-8");

        /* Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.mipmap.back, null);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, Color.GRAY);
        actionBar.setNavigationIcon(R.mipmap.back);*/

   /*     RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<Integer>(this);
        // Set the range
        rangeSeekBar.setRangeValues(15, 90);
        rangeSeekBar.setSelectedMinValue(20);
        rangeSeekBar.setSelectedMaxValue(88);

        // Add to layout
        LinearLayout layout = (LinearLayout) findViewById(R.id.ll_seekbar);
        layout.addView(rangeSeekBar);*/
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
