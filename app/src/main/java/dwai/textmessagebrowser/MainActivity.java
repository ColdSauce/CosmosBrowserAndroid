package dwai.textmessagebrowser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.lang.reflect.Method;


public class MainActivity extends Activity {
    private static final String PHONE_NUMBER = "8443343982";
    private final String ROOT_HTML_FILE_NAME = "root.html";
    private FullTextMessage fullTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets the font for the whole layout.
        final Typeface mFont = Typeface.createFromAsset(getAssets(),
                "fonts/proxima.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        MainActivity.setAppFont(mContainer, mFont, true);

        final EditText urlEditText =  (EditText) findViewById(R.id.urlEditText);
        urlEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        textToTwilio(urlEditText.getText().toString());
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });




    }
    private void textToTwilio(String whatToSend) throws Exception{
        String phone_Num = PHONE_NUMBER;
        String send_msg = whatToSend;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone_Num, null, send_msg, null, null);

    }


    private void saveFile(String name, String content) {
        String filename = name;
        String string = content;
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Overrides the usual function of back pressed so that it doesn't close the app but rather goes back a page with the web view.
    @Override
    public void onBackPressed() {
        WebView rootWebView = ((WebView) findViewById(R.id.theWebView));
        if (rootWebView.canGoBack()) {
            rootWebView.goBack();
        }
    }

    //Recursively sets the appFont because Android is weird like that.
    public static final void setAppFont(ViewGroup mContainer, Typeface mFont, boolean reflect) {

        if (mContainer == null || mFont == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i) {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView) {
                ((TextView) mChild).setTypeface(mFont);
            } else if (mChild instanceof ViewGroup) {
                setAppFont((ViewGroup) mChild, mFont, true);
            } else if (reflect) {
                try {
                    Method mSetTypeface = mChild.getClass().getMethod("setTypeface", Typeface.class);
                    mSetTypeface.invoke(mChild, mFont);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
                            String fullHTML = fullTextMessage.addText((msgBody));
                            if(!fullHTML.equals("NOT LAST")){
                                ((WebView)findViewById(R.id.theWebView)).loadDataWithBaseURL("",fullHTML,"text/html","UTF-8","");
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


}
