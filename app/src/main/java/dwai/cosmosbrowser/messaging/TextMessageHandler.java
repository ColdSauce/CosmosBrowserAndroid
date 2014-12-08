package dwai.cosmosbrowser.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dwai.cosmosbrowser.MainBrowserScreen;

/**
 * Created by Stefan on 10/19/2014.
 */
public class TextMessageHandler {

    private final String TAG = "TextMessageHandler";
    public static final String PHONE_NUMBER = "8443341241";




    /**
     *
     * @param body Body of the message a person is trying to send.
     * @param to Who the person is sending the text message to. Must be 10 digits.
     */
    public void sendTextMessage(String body, String to){
        //TODO: Make it so that this works when it's not 10 digits.

        if(body == null || to == null){
            Log.e(TAG, "***** ERROR EITHER BODY OR TO IS NULL!");
            return;
        }

        SmsManager smsManager = SmsManager.getDefault();
        if(body.length() > 160) {
            //Because the body of the message can be larger than tha 140 bit limit presented, the message must be split up.
            ArrayList<String> parts = smsManager.divideMessage(body);
            smsManager.sendMultipartTextMessage(to, null, parts, null, null);
        }
        else{
            smsManager.sendTextMessage(to,null,body,null,null);
        }

    }

    public static class SMSReceiver extends BroadcastReceiver {

        private final String TAG = "SMSReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle extras = intent.getExtras();
                Object[] pdus = (Object[]) extras.get("pdus");
                for (Object pdu : pdus) {
                    SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdu);
                    String origin = msg.getOriginatingAddress();
                    String body = msg.getMessageBody();


                    MainBrowserScreen.webView.loadDataWithBaseURL(null,origin + " , " + body,"text/html","utf-8",null);
                }
            }
        }
    }
}
