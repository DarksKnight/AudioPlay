package com.audiodemo.adapter;

import com.audiodemo.MainActivity;
import com.audiodemo.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 消息列表
 * Created by apple on 5/30/15.
 */
public class RecordAdapter extends ArrayAdapter<MainActivity.Recorder> {

    private List<MainActivity.Recorder> mDatas;
    private Context mContext;
    //用于计算时间宽度
    private int minItemWidth;
    private int maxItemWidth;
    private LayoutInflater mInflater;

    public RecordAdapter(Context context, List<MainActivity.Recorder> datas) {
        super(context, -1, datas);

        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        maxItemWidth = (int)(dm.widthPixels * 0.7f);
        minItemWidth = (int)(dm.widthPixels * 0.15f);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_record, parent, false);
            v = new ViewHolder();
            v.seconds = (TextView)convertView.findViewById(R.id.tvTime);
            v.length = convertView.findViewById(R.id.lfRecordLength);
            convertView.setTag(v);
        }else{
            v = (ViewHolder)convertView.getTag();
        }

        v.seconds.setText(Math.round(getItem(position).getTime()) + "\"");
        ViewGroup.LayoutParams lp = v.length.getLayoutParams();
        lp.width = (int)(minItemWidth + (maxItemWidth / 60 * getItem(position).getTime()));
        return convertView;
    }

    private class ViewHolder{
        TextView seconds;
        View length;
    }
}
