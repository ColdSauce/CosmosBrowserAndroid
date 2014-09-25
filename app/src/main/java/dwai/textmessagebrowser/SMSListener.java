package dwai.textmessagebrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.webkit.WebView;

import dwai.textmessagebrowser.R;

public class SMSListener extends BroadcastReceiver {




    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){

            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){

                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        //Log.d("COSMOS",msgBody);
                        String fullHTML = MainActivity.fullTextMessage.addText(msgBody);
                        if(!fullHTML.equals("NOT LAST")){
                            Log.d("COSMOS", fullHTML);
                            if(MainActivity.webView != null){
                                (MainActivity.webView).loadDataWithBaseURL("",fullHTML,"text/html","UTF-8","");
                            }

                        }
                        //This, to my knowledge, gets rid of this text message.
                        abortBroadcast();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}