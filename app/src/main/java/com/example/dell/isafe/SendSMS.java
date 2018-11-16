package com.example.dell.isafe;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.telephony.SmsManager.getDefault;
import static android.telephony.SmsManager.getSmsManagerForSubscriptionId;
import static android.telephony.SubscriptionManager.getDefaultSmsSubscriptionId;

public class SendSMS {
    String msg;
    String no;
    Context context;
        public SendSMS(String num, String text ){
        msg= "Help.. My Location:"+text;
        no=num;
//        Toast.makeText(this, "***"+msg, Toast.LENGTH_SHORT).show();
//        Log.d("swati",msg+"vvvvvv"+no);
//        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
//
//        no = no.trim();
//        //Get the SmsManager instance and call the sendTextMessage method to send message
////        SmsManager sms = getSmsManagerForSubscriptionId(getDefaultSmsSubscriptionId());
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(no, null, msg, null,pi);
//    Toast.makeText(getApplicationContext(), "Message Sent successfully!",
//                Toast.LENGTH_LONG).show();

    }
}

