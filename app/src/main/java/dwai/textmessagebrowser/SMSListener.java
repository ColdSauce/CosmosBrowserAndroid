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

    private void loadWebpage() {
        String fullHTML = MainActivity.fullTextMessage.getDecompressedMessages();
        Log.d("COSMOS", "Full HTML:\t" + fullHTML);
        if (MainActivity.webView != null) {
            (MainActivity.webView).loadMarkdown(fullHTML, "file:///android_asset/main.css");
        }
    }

    private void sendWebpage(String request) throws IOException {
        httpRequestProcessor processor = new httpRequestProcessor();
        processor.execute(request);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Log.d("COSMOS", "Texts ArrayList:\t"+MainActivity.fullTextMessage.texts.toString());
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
                        Log.d("COSMOS", "StreamSize " + streamSize);
//                        Log.d("COSMOS", msgBody.substring(0,msgBody.indexOf("%")));
                        MainActivity.fullTextMessage.addText(msgBody);
                        //This, to my knowledge, gets rid of this text message.
                        abortBroadcast();
                    }

                    Log.d("COSMOS", "Texts-Size :\t"+MainActivity.fullTextMessage.texts.size());
                    Log.d("COSMOS", "Texts ArrayList:\t"+MainActivity.fullTextMessage.texts.toString());
                    if(MainActivity.fullTextMessage.texts.size() == streamSize) {
                        MainActivity.fullTextMessage.sortMessages();
                        Log.d("COSMOS", "Message stub " + MainActivity.fullTextMessage.getAllMessages().substring(0, 8));
                        if (MainActivity.fullTextMessage.getAllMessages().substring(0, 8).equals("GET http")) {
                            String request = MainActivity.fullTextMessage.getAllMessages().substring(4);
                            MainActivity.fullTextMessage.texts.clear();
                            sendWebpage(request);
                        } else {
                            loadWebpage();
                        }
                    }

                }catch(Exception e){
                    abortBroadcast();
                    e.printStackTrace();
                }
            }
        }
    }

    private class httpRequestProcessor extends AsyncTask<String, Object, Object> {

        private void send(FullTextMessage message) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("6123560899", null, "0%H4sICNBaqFYAA2EudHh0ANPyyc9RSMlMT88sqdTiAgAtdaCXDgAAAA==*11", null, null);
            return;
        }

        private String encode(String output) {
            return "";
        }

        @Override
        protected Object doInBackground(String[] strings) {
            String request = strings[0];
            Log.d("COSMOS", "Webpage request:" + request);
            Document doc;
            try {
                doc = Jsoup.connect(request).get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Elements styles = doc.select("style");
            for (Element style: styles) {
                style.remove();
            }
            styles = doc.select("script");
            for (Element style: styles) {
                style.remove();
            }
            Element header = doc.select("head").first();
            header.remove();
            //style.remove();
            String strippedText = doc.toString().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", "\n");
            Log.d("COSMOS", "Site contents " + strippedText);
            FullTextMessage message = new FullTextMessage();
            send(message);
            return null;
        }
    }
}