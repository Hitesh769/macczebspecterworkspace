package com.spectre.fragment_new;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.spectre.R;

public class MasterFragment extends Fragment {

    /**
     * Call activity with shared element and finish activity
     *
     * @param intent
     * @param currentActivity
     * @param sharedElement
     */
    public void startActWithSharedElementFinish(Intent intent, Activity currentActivity, View sharedElement) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(currentActivity,
                            sharedElement,
                            ViewCompat.getTransitionName(sharedElement));
            currentActivity.startActivity(intent, options.toBundle());
        } else {
            startActFinish(currentActivity, intent);
        }
    }

    /**
     * Call activity with shared element
     *
     * @param intent
     * @param currentActivity
     * @param sharedElement
     */
    public void startActWithSharedElement(Intent intent, Activity currentActivity, View sharedElement) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(currentActivity,
                            sharedElement,
                            ViewCompat.getTransitionName(sharedElement));
            startActivity(intent, options.toBundle());
        } else {
            startAct(currentActivity, intent);
        }
    }

    /**
     * start new activity with finish current activity
     *
     * @param intent
     */
    public void startActFinish(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        activity.finish();
    }

    /**
     * start new activity without finish current activity
     *
     * @param intent
     */
    public void startAct(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
