package com.project.it.hamyar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by hashemi on 02/10/2018.
 */

public class Rec_sms extends BroadcastReceiver {

    private PublicVariable PV;
    private static SmsListener mListener;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle data = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if (data != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) data.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        if(msg_from == PV.Recive_NumberPhone || msg_from == "+98"+PV.Recive_NumberPhone){
                            String messageBody = msgs[i].getMessageBody();
                            mListener.onMessageReceived(messageBody);
                        }
                    }
                }
                catch(Exception e){
                    //   Log.d("Exception caught",e.getMessage());
                }
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}