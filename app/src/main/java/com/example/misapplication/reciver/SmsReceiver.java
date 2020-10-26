package com.example.misapplication.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SmsReceiver extends BroadcastReceiver {

    String abcd;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();

            Log.i(TAG, "senderNum: " + sender + " message: " + messageBody);
            if(sender.contains("QP-MANTHN")){
                abcd = messageBody.replaceAll("[^0-9]", "");   // here abcd contains otp which is in number format

            }
        }
    }
}

