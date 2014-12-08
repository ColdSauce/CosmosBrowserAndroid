package dwai.cosmosbrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dwai.cosmosbrowser.webkit.CosmosWebView;

public class MainBrowserScreen extends Activity {

    private static final String TAG = "MainBrowserScreen";
    public static CosmosWebView webView;
    @InjectView(R.id.settingsListView)SettingsListView settingsListView;
    @InjectView(R.id.rootWebView)CosmosWebView cosmosWebView;
    @InjectView(R.id.searchBar)LinearLayout searchBar;
    @InjectView(R.id.urlEditText)EditText urlEditText;
    @InjectView(R.id.tabsListView)ListView tabsListView;
    @InjectView(R.id.tabsButton)ImageView tabsButton;
    @InjectView(R.id.moreSettingsButton)ImageView moreSettingsButton;
    @InjectView(R.id.moreOptionsView)RelativeLayout moreOptionsView;
    @InjectView(R.id.topSettingsBar)LinearLayout topSettingsBar;
    List<View> expandableViews = new ArrayList<View>();


    @SuppressWarnings("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_browser_screen);
        webView = (CosmosWebView)findViewById(R.id.rootWebView);
        ButterKnife.inject(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            searchBar.setElevation(10);
            moreOptionsView.setElevation(13);
        }
        expandableViews.add(moreOptionsView);
        expandableViews.add(tabsListView);
        webView.loadUrl("file:///android_asset/testfile.html");




        cosmosWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideAllExpandableViews();
                    hideKeyboard();
                    view.requestFocus();
                }
                return false;
            }
        });
        urlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocused) {
                if(isFocused){
                    urlEditText.setSelection(urlEditText.getText().length());
                    searchBar.getChildAt(1).setVisibility(View.GONE);
                    searchBar.getChildAt(2).setVisibility(View.GONE);
                    hideAllExpandableViews();
                }
                else{
                    //This sets the cursor of the edit text back to the front so that the url is visible when focus changes.
                    urlEditText.setSelection(0);
                    searchBar.getChildAt(1).setVisibility(View.VISIBLE);
                    searchBar.getChildAt(2).setVisibility(View.VISIBLE);
                }
            }
        });


    }




    public void onResume(){
        findViewById(R.id.rootWebView).requestFocus();
        super.onResume();
    }

    public void clickedTabs(View v){
        moreOptionsView.setVisibility(View.INVISIBLE);
        int visibility = tabsListView.getVisibility();
        if(visibility == View.VISIBLE){
            tabsListView.setVisibility(View.INVISIBLE);
        }
        else{
            tabsListView.setVisibility(View.VISIBLE);
        }
    }

    public void clickedSettings(View v){

        tabsListView.setVisibility(View.INVISIBLE);
        int visibility = moreOptionsView.getVisibility();
        if(visibility == View.VISIBLE){
            moreOptionsView.setVisibility(View.INVISIBLE);
        }
        else{
            moreOptionsView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed(){
        ListView settingsListView = (ListView)findViewById(R.id.settingsListView);

        if(searchBar.getChildAt(1).getVisibility() == View.GONE){
            searchBar.getChildAt(1).setVisibility(View.VISIBLE);
            searchBar.getChildAt(2).setVisibility(View.VISIBLE);
            cosmosWebView.requestFocus();
            hideKeyboard();
        }
        else if(settingsListView.getVisibility() == View.VISIBLE){
            hideAllExpandableViews();
        }
        else if(tabsListView.getVisibility() == View.VISIBLE){
            hideAllExpandableViews();
        }
        else{
            super.onBackPressed();
        }
    }

    private void hideAllExpandableViews(){
        for(View v : expandableViews){
            v.setVisibility(View.GONE);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
