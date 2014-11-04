package dwai.textmessagebrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

import us.feras.mdv.MarkdownView;


public class MainActivity extends Activity {
    private final String PHONE_NUMBER = "0018443343982";
    private final String ROOT_HTML_FILE_NAME = "root.html";
    public static FullTextMessage fullTextMessage;
    public static MarkdownView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (MarkdownView)findViewById(R.id.theWebView);
        fullTextMessage = new FullTextMessage();
        WebViewClient webViewClient= new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView  view, String  url){
                try {
                    fullTextMessage.texts.clear();
                    Log.d("COSMOS", "the url is " + url);
                    sendStringToTwilio(url);
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

        //The following is for testing the getDecompressedMessages method..
//        (MainActivity.webView).loadDataWithBaseURL("",fullTextMessage.getDecompressedMessages(),"text/html","UTF-8","");

        final EditText urlEditText =  (EditText) findViewById(R.id.urlEditText);
        urlEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        fullTextMessage.texts.clear();
                        String urlText = urlEditText.getText().toString();
                        if(!Patterns.WEB_URL.matcher(urlText).matches()){
                            generateAlertDialog("URL is invalid! Please try again with the correct url.");
                            return true;

                        }
                        if(urlText.length() > 3 && !urlText.contains(" ")) {
                            if (urlText.substring(0, 7).equals("http://") || urlText.substring(0, 8).equals("https://"))
                                textToTwilio(urlText);
                            else
                                textToTwilio("http://" + urlText);
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

    //Overrides the usual function of back pressed so that it doesn't close the app but rather goes back a page with the web view.
//    @Override
//    public void onBackPressed() {
//        WebView rootWebView = ((WebView) findViewById(R.id.theWebView));
//        if (rootWebView.canGoBack()) {
//            rootWebView.goBack();
//        }
//    }



}
