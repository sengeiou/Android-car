package com.tgf.kcwc.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.tgf.kcwc.R;

/**
 * @anthor Administrator
 * @time 2017/8/11
 * @describle
 */
public class HistoryCursorAdapter extends SimpleCursorAdapter {
    private Cursor mCursor;
    private Context mContext;
    private LayoutInflater mInflater;
    OnClearClickListener onClearClickListener;
    OnItemClickListener onItemClickListener;

    public HistoryCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mCursor = c;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public interface OnClearClickListener{
        void onClearClick(String position);
    }
    public interface OnItemClickListener{
        void onItemClick(String str);
    }
    public void setOnClearClickListener(OnClearClickListener onClearClickListener) {
        this.onClearClickListener = onClearClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
//        if (mCursor == null) {
//            convertView = mInflater.inflate(R.layout.seek_history_item, null);
//        } else {
//            convertView = view;
//        }
        ImageView clearHistory = (ImageView) view.findViewById(R.id.history_item_clear);
        final TextView textView = (TextView) view.findViewById(R.id.history_item_name);
        textView.setText(mCursor.getString(mCursor.getColumnIndex("name")));
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onClearClickListener){
                    onClearClickListener.onClearClick(textView.getText().toString().trim());

                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClickListener){
                    onItemClickListener.onItemClick(textView.getText().toString());
                }
            }
        });
        return view;
    }
}
