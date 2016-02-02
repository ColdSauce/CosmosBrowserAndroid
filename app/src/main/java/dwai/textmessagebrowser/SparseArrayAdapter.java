package dwai.textmessagebrowser;

import android.util.SparseArray;
import android.widget.BaseAdapter;

/**
 * Created by Orien on 2/2/16.
 *
 * Source: http://stackoverflow.com/questions/21677866/how-to-use-sparsearray-as-a-source-for-adapter
 */
public abstract class SparseArrayAdapter<E> extends BaseAdapter {

    private SparseArray<E> mData;
    public void setData(SparseArray<E> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public E getItem(int position) {
        return mData.valueAt(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.keyAt(position);
    }
}
