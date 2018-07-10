package com.spectre.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.spectre.R;
import com.spectre.adapter.ViewCatalogImagesAdapter;
import com.spectre.other.Constant;
import com.spectre.utility.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class ViewCatalogImagesActivity extends AppCompatActivity {
    private Context context;

    public List<String> listImage = new ArrayList();
    private ViewPager vpCatalog;
    private String list;
    ViewCatalogImagesAdapter adapter;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_view_catalog_images);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        init();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        context = this;
        pos = getIntent().getIntExtra(Constant.POSITION, 1);
        listImage = getIntent().getStringArrayListExtra(Constant.IMAGE);
        vpCatalog = (ViewPager) findViewById(R.id.vp_catalog);
        adapter = new ViewCatalogImagesAdapter(context, listImage, pos);
        vpCatalog.setAdapter(adapter);
        vpCatalog.setPageTransformer(true, new DepthPageTransformer());
        vpCatalog.setCurrentItem(pos);
        vpCatalog.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
