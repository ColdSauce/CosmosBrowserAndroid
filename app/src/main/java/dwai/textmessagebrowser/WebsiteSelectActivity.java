package dwai.textmessagebrowser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class WebsiteSelectActivity extends Activity {
    SparseFullTextMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_select);
        Log.d("COSMOS", "Website selector created");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("COSMOS", "On resume called");
        Log.d("COSMOS", "All messages: " + MainActivity.messages.toString());
        adapter = new SparseFullTextMessageAdapter(this, MainActivity.messages);
        ListView view = (ListView) findViewById(R.id.webSiteListView);
        view.setAdapter(adapter);
        view.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> list,
                                            View row,
                                            int index,
                                            long rowID) {
                        Log.d("COSMOS", "INDEX " + index);
                        String fullHTML = MainActivity.messages.get(MainActivity.messages.keyAt(index)).getDecompressedMessages();
                        if (MainActivity.webView != null) {
                            (MainActivity.webView).loadMarkdown(fullHTML, "file:///android_asset/main.css");
                        }
                    }
                }
        );
    }
}
