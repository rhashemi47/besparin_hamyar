package com.besparina.it.hamyar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hashemi on 10/15/2018.
 */

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceGetNewJob.class));
        context.startService(new Intent(context, ServiceGetFactorAccept.class));
        context.startService(new Intent(context, ServiceGetLocation.class));
        context.startService(new Intent(context, ServiceGetSliderPic.class));
        context.startService(new Intent(context, ServiceSyncProfile.class));
        context.startService(new Intent(context, ServiceSyncServiceSelected.class));
        context.startService(new Intent(context, ServiceGetJobUpdate.class));
        context.startService(new Intent(context, ServiceDeleteJob.class));
        context.startService(new Intent(context, ServiceGetUserServiceStartDate.class));
    }
}
