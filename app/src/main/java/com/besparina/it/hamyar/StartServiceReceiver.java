package com.besparina.it.hamyar;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by hashemi on 10/15/2018.
 */

public class StartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(new Intent(context, ServiceGetNewJob.class));
//            context.startForegroundService(new Intent(context, ServiceGetFactorAccept.class));
//            context.startForegroundService(new Intent(context, ServiceGetLocation.class));
//            context.startForegroundService(new Intent(context, ServiceGetSliderPic.class));
//            context.startForegroundService(new Intent(context, ServiceSyncProfile.class));
//            context.startForegroundService(new Intent(context, ServiceSyncServiceSelected.class));
//            context.startForegroundService(new Intent(context, ServiceGetJobUpdate.class));
//            context.startForegroundService(new Intent(context, ServiceDeleteJob.class));
//            context.startForegroundService(new Intent(context, ServiceGetUserServiceStartDate.class));
//        } else {
//            context.startService(new Intent(context, ServiceGetNewJob.class));
//            context.startService(new Intent(context, ServiceGetFactorAccept.class));
//            context.startService(new Intent(context, ServiceGetLocation.class));
//            context.startService(new Intent(context, ServiceGetSliderPic.class));
//            context.startService(new Intent(context, ServiceSyncProfile.class));
//            context.startService(new Intent(context, ServiceSyncServiceSelected.class));
//            context.startService(new Intent(context, ServiceGetJobUpdate.class));
//            context.startService(new Intent(context, ServiceDeleteJob.class));
//            context.startService(new Intent(context, ServiceGetUserServiceStartDate.class));
//        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
            //*****************************************SchaduleServiceGetNewJob******************************************
            ComponentName serviceComponent_SchaduleServiceGetNewJob = new ComponentName(context, SchaduleServiceGetNewJob.class);
            JobInfo.Builder builder_SchaduleServiceGetNewJob = null;
            builder_SchaduleServiceGetNewJob = new JobInfo.Builder(0, serviceComponent_SchaduleServiceGetNewJob);
            builder_SchaduleServiceGetNewJob.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetNewJob.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetNewJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceGetNewJob.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetNewJob.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetNewJob = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetNewJob = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetNewJob.schedule(builder_SchaduleServiceGetNewJob.build());
            //*****************************************SchaduleServiceGetFactorAccept******************************************
            ComponentName serviceComponent_SchaduleServiceGetFactorAccept = new ComponentName(context, SchaduleServiceGetFactorAccept.class);
            JobInfo.Builder builder_SchaduleServiceGetFactorAccept = null;
            builder_SchaduleServiceGetFactorAccept = new JobInfo.Builder(1, serviceComponent_SchaduleServiceGetFactorAccept);
            builder_SchaduleServiceGetFactorAccept.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetFactorAccept.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetFactorAccept.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceGetFactorAccept.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetFactorAccept.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetFactorAccept = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetFactorAccept = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetFactorAccept.schedule(builder_SchaduleServiceGetFactorAccept.build());

            //*****************************************SchaduleServiceGetLocation************************************************
            ComponentName serviceComponent_SchaduleServiceGetLocation = new ComponentName(context, SchaduleServiceGetLocation.class);
            JobInfo.Builder builder_SchaduleServiceGetLocation = null;
            builder_SchaduleServiceGetLocation = new JobInfo.Builder(2, serviceComponent_SchaduleServiceGetLocation);
            builder_SchaduleServiceGetLocation.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetLocation.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetLocation.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceGetLocation.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetLocation.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetLocation = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetLocation = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetLocation.schedule(builder_SchaduleServiceGetLocation.build());

            //*****************************************SchaduleServiceGetSliderPic******************************************
            ComponentName serviceComponent_SchaduleServiceGetSliderPic = new ComponentName(context, SchaduleServiceGetSliderPic.class);
            JobInfo.Builder builder_SchaduleServiceGetSliderPic = null;
            builder_SchaduleServiceGetSliderPic = new JobInfo.Builder(4, serviceComponent_SchaduleServiceGetSliderPic);
            builder_SchaduleServiceGetSliderPic.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetSliderPic.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetSliderPic.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceGetSliderPic.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetSliderPic.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetSliderPic = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetSliderPic = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetSliderPic.schedule(builder_SchaduleServiceGetSliderPic.build());

            //*****************************************SchaduleServiceSyncProfile******************************************
            ComponentName serviceComponent_SchaduleServiceSyncProfile = new ComponentName(context, SchaduleServiceSyncProfile.class);
            JobInfo.Builder builder_SchaduleServiceSyncProfile = null;
            builder_SchaduleServiceSyncProfile = new JobInfo.Builder(5, serviceComponent_SchaduleServiceSyncProfile);
            builder_SchaduleServiceSyncProfile.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceSyncProfile.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceSyncProfile.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceSyncProfile.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceSyncProfile.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceSyncProfile = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceSyncProfile = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceSyncProfile.schedule(builder_SchaduleServiceSyncProfile.build());

            //*****************************************SchaduleServiceSyncServiceSelected******************************************
            ComponentName serviceComponent_SchaduleServiceSyncServiceSelected = new ComponentName(context, SchaduleServiceSyncServiceSelected.class);
            JobInfo.Builder builder_SchaduleServiceSyncServiceSelected = null;
            builder_SchaduleServiceSyncServiceSelected = new JobInfo.Builder(6, serviceComponent_SchaduleServiceSyncServiceSelected);
            builder_SchaduleServiceSyncServiceSelected.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceSyncServiceSelected.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceSyncServiceSelected.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceSyncServiceSelected.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceSyncServiceSelected.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceSyncServiceSelected = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceSyncServiceSelected = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceSyncServiceSelected.schedule(builder_SchaduleServiceSyncServiceSelected.build());

            //*****************************************SchaduleServiceGetJobUpdate******************************************
            ComponentName serviceComponent_SchaduleServiceGetJobUpdate = new ComponentName(context, SchaduleServiceGetJobUpdate.class);
            JobInfo.Builder builder_SchaduleServiceGetJobUpdate = null;
            builder_SchaduleServiceGetJobUpdate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetJobUpdate);
            builder_SchaduleServiceGetJobUpdate.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetJobUpdate.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetJobUpdate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceGetJobUpdate.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetJobUpdate.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetJobUpdate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetJobUpdate = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetJobUpdate.schedule(builder_SchaduleServiceGetJobUpdate.build());

            //*****************************************SchaduleServiceDeleteJob******************************************
            ComponentName serviceComponent_SchaduleServiceDeleteJob = new ComponentName(context, SchaduleServiceDeleteJob.class);
            JobInfo.Builder builder_SchaduleServiceDeleteJob = null;
            builder_SchaduleServiceDeleteJob = new JobInfo.Builder(7, serviceComponent_SchaduleServiceDeleteJob);
            builder_SchaduleServiceDeleteJob.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceDeleteJob.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceDeleteJob.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceDeleteJob.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceDeleteJob.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceDeleteJob = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceDeleteJob = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceDeleteJob.schedule(builder_SchaduleServiceDeleteJob.build());

            //*****************************************SchaduleServiceGetUserServiceStartDate******************************************
            ComponentName serviceComponent_SchaduleServiceGetUserServiceStartDate = new ComponentName(context, SchaduleServiceGetUserServiceStartDate.class);
            JobInfo.Builder builder_SchaduleServiceGetUserServiceStartDate = null;
            builder_SchaduleServiceGetUserServiceStartDate = new JobInfo.Builder(7, serviceComponent_SchaduleServiceGetUserServiceStartDate);
            builder_SchaduleServiceGetUserServiceStartDate.setMinimumLatency(5 * 1000); // wait at least
            builder_SchaduleServiceGetUserServiceStartDate.setOverrideDeadline(50 * 1000); // maximum delay
            builder_SchaduleServiceGetUserServiceStartDate.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
            builder_SchaduleServiceGetUserServiceStartDate.setRequiresDeviceIdle(false); // device should be idle
            builder_SchaduleServiceGetUserServiceStartDate.setRequiresCharging(false); // we don't care if the device is charging or not
            JobScheduler jobScheduler_SchaduleServiceGetUserServiceStartDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                jobScheduler_SchaduleServiceGetUserServiceStartDate = context.getSystemService(JobScheduler.class);
            }
            jobScheduler_SchaduleServiceGetUserServiceStartDate.schedule(builder_SchaduleServiceGetUserServiceStartDate.build());

        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(new Intent(context, ServiceGetServiceSaved.class));
//                context.startForegroundService(new Intent(context, ServiceGetLocation.class));
//                context.startForegroundService(new Intent(context, ServiceGetSliderPic.class));
//                context.startForegroundService(new Intent(context, ServiceSyncMessage.class));
//                context.startForegroundService(new Intent(context, ServiceGetServicesAndServiceDetails.class));
//                context.startForegroundService(new Intent(context, ServiceGetPerFactor.class));
//                context.startForegroundService(new Intent(context, ServiceGetServiceVisit.class));
//                context.startForegroundService(new Intent(context, ServiceGetStateAndCity.class));
//                context.startForegroundService(new Intent(context, ServiceGetUserServiceStartDate.class));
//            } 
        else {
            context.startService(new Intent(context, ServiceGetNewJob.class));//*
            context.startService(new Intent(context, ServiceGetFactorAccept.class));//*
            context.startService(new Intent(context, ServiceGetLocation.class));//*
            context.startService(new Intent(context, ServiceGetSliderPic.class));//*
            context.startService(new Intent(context, ServiceSyncProfile.class));//*
            context.startService(new Intent(context, ServiceSyncServiceSelected.class));//*
            context.startService(new Intent(context, ServiceGetJobUpdate.class));//*
            context.startService(new Intent(context, ServiceDeleteJob.class));//*
            context.startService(new Intent(context, ServiceGetUserServiceStartDate.class));//*
        }
    }
}
