package dwai.textmessagebrowser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class WebsiteSelectActivity extends Activity {
    SparseFullTextMessageAdapter adapter;
    @Bind(R.id.webSiteListView) ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_select);
        ButterKnife.bind(this);
        Log.d("COSMOS", "Website selector created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("COSMOS", "On resume called");
        Log.d("COSMOS", "All messages: " + MainActivity.messages.toString());
        adapter = new SparseFullTextMessageAdapter(this, MainActivity.messages);
        view.setAdapter(adapter);
    }

    @OnItemClick(R.id.webSiteListView)
    public void onItemClick(AdapterView<?> list, View row, int index, long rowID) {
        Log.d("COSMOS", "INDEX " + index);
        String fullHTML = MainActivity.messages.get(MainActivity.messages.keyAt(index)).getDecompressedMessages();
        MainActivity.setWebView(fullHTML);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("webviewcontent", fullHTML);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
