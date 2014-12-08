package dwai.cosmosbrowser;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 10/24/2014.
 */
public class SettingsListView extends ListView{
   
    public SettingsListView(Context context) {
        super(context);
        addAllItems();
    }

    public SettingsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addAllItems();
    }

    public SettingsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addAllItems();
    }
    private void addAllItems(){
        List<String> listElements = new ArrayList<String>();
        listElements.add("New tab");
        listElements.add("New incognito tab");
        listElements.add("Bookmarks");
        listElements.add("History");
        listElements.add("Find in page");
        listElements.add("Feedback");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                listElements);
        this.setAdapter(arrayAdapter);

    }
}
