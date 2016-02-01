package dwai.textmessagebrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

import us.feras.mdv.MarkdownView;


public class MainActivity extends Activity {
    private final String PHONE_NUMBER = "+16123560899";
    private final String ROOT_HTML_FILE_NAME = "root.html";
    public static FullTextMessage fullTextMessage;
    public static MarkdownView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("COSMOS", "CREATED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (MarkdownView)findViewById(R.id.theWebView);
        fullTextMessage = new FullTextMessage();
        WebViewClient webViewClient= new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String  url){
                Log.d("COSMOS", "Override URL Loading, url " + url);
                try {
                    fullTextMessage.clear();
                    //sendStringToTwilio(url);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        } ;
        webView.setWebViewClient(webViewClient);
        webView.loadMarkdown("### New Window\n" +
                " \n" +
                "######Enter a valid url into the bar above. \n" +
                " \n" +
                "######Please note we're still in the early, beta, stages.\n" +
                " \n" +
                "######There will be bugs, that's the entire reason for us releasing so early.\n" +
                " \n" +
                "######Please report all bugs to bugs@cosmosbrowser.org\n" +
                " \n" +
                "######We can't handle huge websites just yet. We're working on better compression.\n" +
                " \n" +
                "######Hang tight!", "file:///android_asset/main.css");


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
                        fullTextMessage.clear();
                        String urlText = urlEditText.getText().toString();
                        if(!Patterns.WEB_URL.matcher(urlText).matches()){
                            generateAlertDialog("URL is invalid! Please try again with the correct url.");
                            return true;

                        }

                        if(urlText.length() > 3 && !urlText.contains(" ")) {
                            if (!(urlText.substring(0, 7).equals("http://") || urlText.substring(0, 8).equals("https://")))
                                urlText = "http://" + urlText;
                            int hash = urlText.hashCode(); // TODO: convert to binary
                            hash %= 1000;
                            urlText = "GET " + urlText;
                            FullTextMessage request = new FullTextMessage(urlText, hash);
                            request.to = PHONE_NUMBER;
                            //Log.d("COSMOS", "About to send");
                            request.send();
                        } else {
                            Toast.makeText(getBaseContext(), "Please enter a valid URL!", Toast.LENGTH_SHORT).show();
                        }
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
        Log.d("Text", "Texting " + whatToSend);
        sms.sendTextMessage(phone_Num, null, send_msg, null, null);
    }
    private void sendStringToTwilio(String whatToSend){
         String send_msg = whatToSend;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(PHONE_NUMBER, null, send_msg, null, null);
    }
    private void generateAlertDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
