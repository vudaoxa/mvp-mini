package org.angmarch.views;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author angelo.marchesin
 */

@SuppressWarnings("unused")
public abstract class NiceSpinnerBaseAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected int mSelectedIndex;

    public NiceSpinnerBaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View cv, ViewGroup parent) {
        TextView textView;

        if (cv == null) {
            cv = View.inflate(mContext, R.layout.spinner_list_item, null);
            textView = (TextView) cv.findViewById(R.id.tv_tinted_spinner);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selector));
            }

            cv.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder) cv.getTag()).textView;
        }

        textView.setText(getItem(position).toString());

        return cv;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void notifyItemSelected(int index) {
        mSelectedIndex = index;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public abstract int getCount();

    public abstract T getItemInDataset(int position);

    protected static class ViewHolder {

        public TextView textView;

        public ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
