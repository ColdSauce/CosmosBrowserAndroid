package dwai.textmessagebrowser;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Orien on 2/2/16.
 * Source: http://stackoverflow.com/questions/21677866/how-to-use-sparsearray-as-a-source-for-adapter
 */
public class SparseFullTextMessageAdapter extends SparseArrayAdapter<String> {
    private final LayoutInflater mInflater;
    public SparseFullTextMessageAdapter(Context context, SparseArray<FullTextMessage> data) {
        Log.d("COSMOS", "Adapter constructor called");
        mInflater = LayoutInflater.from(context);
        SparseArray<String> strings = new SparseArray<String>();
        for (int i = 0; i < data.size(); i++) {
            int key = data.keyAt(i);
            FullTextMessage message = data.get(key);
            Log.d("COSMOS", "Text message contents: " + message.toString());
            strings.append(key, "Website id: " + key + ", size (in text messages) " +
                    message.getSize() + " received messages out of " + message.getTargetSize() + " total");
        }
        setData(strings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView result = (TextView) convertView;
        if (result == null) {
            result = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        result.setText(getItem(position));
        return result;
    }
}