package com.spectre.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.spectre.R;

/**
 * a simple slider view, which just show an image. If you want to make your own slider view,
 *
 * just extend BaseSliderView, and implement getView() method.
 */
public class CustomPbSliderView extends BaseSliderView {

    private static RotateAnimation anim;
    private static ImageView ashoka;

    public CustomPbSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.pd_type_default,null);
        ImageView target = (ImageView)v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
       // ImageView target = (ImageView)v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
      //  ashoka = (ImageView)v.findViewById(R.id.loading_bar);
      //  animation();
        bindEventAndShow(v, target);
        return v;
    }


  /*  public static void animation() {
        int currentRotation = 0;
        anim = new RotateAnimation(currentRotation, (360 * 4),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentRotation = (currentRotation + 45) % 360;
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(35000);
        // anim.setRepeatMode(Animation.INFINITE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setRepeatMode(Animation.RESTART);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        ashoka.startAnimation(anim);
    }*/
}
