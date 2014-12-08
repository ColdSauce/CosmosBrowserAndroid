package dwai.cosmosbrowser.webkit;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Stefan on 10/19/2014.
 */
public class CosmosWebViewClient extends WebViewClient {
    private static final String TAG = "CosmosWebViewClient";
    public CosmosWebViewClient(){

    }

    /**
     *
     * @param view The WebView in question.
     * @param url What the URL of the link clicked was.
     * @return Whether the method felt an override was necessary.
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        if(view == null || url == null){
            Log.e(TAG, "***************** VIEW OR URL IS NULL ERROR *****************");
            return false;
        }
        if(!(view instanceof CosmosWebView)){
            Log.e(TAG, "***************** NOT AN INSTANCE OF COSMOS WEB VIEW! ERROR *****************");
            return false;
        }





        return true;
    }

}
