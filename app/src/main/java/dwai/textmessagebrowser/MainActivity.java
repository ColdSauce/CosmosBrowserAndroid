package dwai.textmessagebrowser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class MainActivity extends Activity {
    private final String PHONE_NUMBER = "<YOUR-TWILIO-PHONENUMBER>";
    private final String ROOT_HTML_FILE_NAME = "root.html";
    public static FullTextMessage fullTextMessage;
    public static WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView)findViewById(R.id.theWebView);
        fullTextMessage = new FullTextMessage();

        //Sets the font for the whole layout.
        final Typeface mFont = Typeface.createFromAsset(getAssets(),
                "fonts/proxima.ttf");
        final ViewGroup mContainer = (ViewGroup) findViewById(
                android.R.id.content).getRootView();
        MainActivity.setAppFont(mContainer, mFont, true);


        //The following is for testing the getDecompressedMessages method..
//        (MainActivity.webView).loadDataWithBaseURL("",fullTextMessage.getDecompressedMessages(),"text/html","UTF-8","");

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
        fullTextMessage.texts = new ArrayList<String>();
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

}
