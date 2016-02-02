package dwai.textmessagebrowser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
    }
}
