/*
* Copyright 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.spectre.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

import com.spectre.R;
import com.spectre.other.Constant;


/**
 * Helper class to manage notification channels, and create notifications.
 */
public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String PRIMARY_CHANNEL = "mtjf_match";
    public static final String SECONDARY_CHANNEL = "mtjf_fan";
    public static final String TERTIARY_CHANNEL = "mtjf_msg";
    public static final String QUATERNARY_CHANNEL = "mtjf_activity";

    /**
     * Registers notification channels, which can be used later by individual notifications.
     *
     * @param ctx The application context
     */
    public NotificationHelper(Context ctx) {
        super(ctx);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
                    getString(R.string.PRIMARY_CHANNEL), NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(chan1);

        NotificationChannel chan2 = new NotificationChannel(SECONDARY_CHANNEL,
                getString(R.string.SECONDARY_CHANNEL), NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(chan2);


            NotificationChannel chan3 = new NotificationChannel(TERTIARY_CHANNEL,
                getString(R.string.TERTIARY_CHANNEL), NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(chan3);

           /* NotificationChannel chan4 = new NotificationChannel(QUATERNARY_CHANNEL,
                getString(R.string.QUATERNARY_CHANNEL), NotificationManager.IMPORTANCE_HIGH);
            getManager().createNotificationChannel(chan4);*/
            SharedPrefUtils.setPreference(ctx, Constant.is_channel_prepared,true);
        }
    }


    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
}
