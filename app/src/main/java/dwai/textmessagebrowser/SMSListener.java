package dwai.textmessagebrowser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import dwai.textmessagebrowser.R;

public class SMSListener extends BroadcastReceiver {


    private int streamSize;

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
                    for(int i=0; i<msgs.length; i++)
                    {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);

                        msg_from = msgs[i].getOriginatingAddress();
                        Log.d("stuff" , msg_from);
                        if(!msg_from.equals("+18443343982")){
                            return;
                        }

                        Toast.makeText(context,"Loading! Please wait!",
                                Toast.LENGTH_SHORT).show();
                        Log.d("stuff", msg_from);
                        String msgBody = msgs[i].getMessageBody();
                        Log.d("COSMOS", msgBody);
                        streamSize = Integer.parseInt(msgBody.substring(msgBody.indexOf("*")+1,msgBody.length()-1));
//                        Log.d("COSMOS", msgBody.substring(msgBody.indexOf("*")+1,msgBody.length()-1));
//                        Log.d("COSMOS", msgBody.substring(0,msgBody.indexOf("%")));
                        MainActivity.fullTextMessage.addText(msgBody);
                        //This, to my knowledge, gets rid of this text message.
                    }
                    abortBroadcast();

                    Log.d("COSMOS", "Texts-Size :\t"+MainActivity.fullTextMessage.texts.size());

                    if(MainActivity.fullTextMessage.texts.size() == streamSize) {
                        MainActivity.fullTextMessage.sortMessages();
                    Log.d("COSMOS", "Texts ArrayList:\t"+MainActivity.fullTextMessage.texts.toString());

                    String fullHTML = MainActivity.fullTextMessage.getDecompressedMessages();
                    Log.d("COSMOS", "Full HTML:\t"+fullHTML);
                        if (MainActivity.webView != null) {
                            (MainActivity.webView).loadMarkdown(fullHTML);
                        }
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}