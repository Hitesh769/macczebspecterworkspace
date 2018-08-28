package com.spectre.activity_new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.spectre.R;

public class MasterAppCompactActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        setActivityFinished();
        super.onBackPressed();
    }

    /**
     * Activity finish with animation (left to right)
     */
    public void setActivityFinished() {
        hideKeyBoard();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        supportFinishAfterTransition();
    }

    /**
     * hide keyboard
     */
    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * start new activity without finish current activity
     *
     * @param intent
     */
    public void startAct(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Start activity for result with request code
     *
     * @param intent
     * @param requestCode
     */
    public void startActForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * start new activity with finish current activity
     *
     * @param intent
     */
    public void startActFinish(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        supportFinishAfterTransition();
    }

    /**
     * start new activity with finish current activity
     *
     * @param intent
     */
    public void startActClearTop(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        supportFinishAfterTransition();
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
            startAct(intent);
        }
    }

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
            startActivity(intent, options.toBundle());
        } else {
            startActFinish(intent);
        }
    }
}
