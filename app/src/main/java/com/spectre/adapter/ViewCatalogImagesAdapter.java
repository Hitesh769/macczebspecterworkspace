package com.spectre.adapter;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;

import com.spectre.R;
import com.spectre.activity.ZoomActivity;
import com.spectre.customView.TouchImageView;

import java.util.List;

/**
 * Created by mona on 30/8/17.
 */

public class ViewCatalogImagesAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<String> listImage;
    //View itemView;
    TouchImageView imageView;
    ImageView iv_rotate;
    private int pos;
    private double mCurrAngle = 0;
    int count;
    private double mPrevAngle = 0;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();
    //scaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener());

    public ViewCatalogImagesAdapter(Context context, List<String> listImage, int position) {
        this.mContext = context;
        this.listImage = listImage;
        this.pos = position;
        Log.e("Cons", "position " + pos);
    }

    @Override
    public int getCount() {
        return listImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        count = 0;
        Log.e("", "position " + position);
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.layout_catalog_zoom_image, container, false);
        imageView = (TouchImageView) itemView.findViewById(R.id.iv_catalog);

        new AQuery(mContext).id(imageView).image(listImage.get(position), true, true, 400, R.drawable.ic_launcher_web);
        /*iv_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==0){
                    imageView.setRotation(90);
                    count++;
                }else {
                    imageView.setRotation(180);
                    count--;
                }
            }
        });*/
       /* imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        imageView.setMaxZoom(2f);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
/*

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final float xc = imageView.getWidth() / 2;
        final float yc = imageView.getHeight() / 2;

        final float x = motionEvent.getX();
        final float y = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                imageView.clearAnimation();
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                mPrevAngle = mCurrAngle;
                mCurrAngle = Math.toDegrees(Math.atan2(x - xc, yc - y));
                animate(mPrevAngle, mCurrAngle, 0);
                System.out.println(mCurrAngle);
                break;
            }
            case MotionEvent.ACTION_UP: {
                mPrevAngle = mCurrAngle = 0;
                break;
            }
        }
        return false;
    }
*/

    private void animate(double fromDegrees, double toDegrees, long durationMillis) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        imageView.startAnimation(rotate);
        //  System.out.println(mCurrAngle);
        Toast.makeText(mContext, "" + mCurrAngle, Toast.LENGTH_SHORT).show();
    }
}