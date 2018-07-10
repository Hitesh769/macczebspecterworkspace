package com.spectre.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.spectre.R;
import com.spectre.customView.RangeSeekBar;
import com.spectre.utility.Utility;

public class AboutUsActivity extends AppCompatActivity {
    private Context context;
    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_about_us);
        context = this;
        Utility.setContentView(context, R.layout.activity_about_us);
        //  Utility.setUpToolbar_(context, "<font color='#ffffff'>About Us</font>", true);
        actionBar = Utility.setUpToolbarWithColor(context, "<font color='#8700C9'>"+getString(R.string.about_us)+"</font>", getResources().getColor(R.color.colorWhite));
        ((WebView) findViewById(R.id.web_view)).loadData("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.", "text/html; charset=utf-8", "UTF-8");

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
