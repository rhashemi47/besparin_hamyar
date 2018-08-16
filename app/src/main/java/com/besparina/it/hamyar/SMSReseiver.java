package com.besparina.it.hamyar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by hashemi on 02/10/2018.
 */

public class SMSReseiver extends BroadcastReceiver {

    private PublicVariable PV;
    private static SmsListener mListener;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED)) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle data = intent.getExtras();
                SmsMessage[] msgs = null;
                String[] StrAccept={};
                String msg_from;
                if (data != null) {
                    //---retrieve the SMS message received---
                    try {
                        String messageBody = "";
                        Object[] pdus = (Object[]) data.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < msgs.length; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            if (msg_from.compareTo(PV.Recive_NumberPhone)==0 ) {
                                messageBody = msgs[i].getMessageBody();
                                StrAccept =messageBody.split(":");
                                StrAccept[1]=StrAccept[1].trim();
                                mListener.onMessageReceived(StrAccept[1]);
                            }
                        }
                        if (messageBody.compareTo("") != 0) {
                            Intent broadcastintent = new Intent();
                            broadcastintent.setAction("SMS_RECEIVED_ACTION");
                            broadcastintent.putExtra("sms", StrAccept[1]);
                            context.sendBroadcast(broadcastintent);
                        }
                    } catch (Exception e) {
                        //   Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}