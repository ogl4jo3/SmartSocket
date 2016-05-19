package com.example.ogl4jo3.smartsocket;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;


public class SmartsocketAdapter extends BaseAdapter {

    // 定義 LayoutInflater
    private LayoutInflater myInflater;
    // 定義 Adapter 內藴藏的資料容器
    private ArrayList<Smartsocket> list;

    public SmartsocketAdapter(Context context, ArrayList<Smartsocket> list) {
        //預先取得 LayoutInflater 物件實體
        myInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() { // 公定寫法(取得List資料筆數)
        return list.size();
    }

    @Override
    public Object getItem(int position) { // 公定寫法(取得該筆資料)
        return list.get(position);
    }

    @Override
    public long getItemId(int position) { // 公定寫法(取得該筆資料的position)
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            // 1:將 R.layout.row 實例化
            convertView = myInflater.inflate(R.layout.item_main, parent, false);

            // 2:建立 UI 標籤結構並存放到 holder
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.starttime_hour = (TextView) convertView.findViewById(R.id.starttime_hour);
            holder.starttime_minute = (TextView) convertView.findViewById(R.id.starttime_minute);
            holder.endtime_hour = (TextView) convertView.findViewById(R.id.endtime_hour);
            holder.endtime_minute = (TextView) convertView.findViewById(R.id.endtime_minute);
            //holder.image = (ImageView) convertView.findViewById(R.id.image);
            //holder.onoffButton = (ToggleButton) convertView.findViewById(R.id.onoffButton);
            holder.onoffButton=(SwitchCompat)convertView.findViewById(R.id.onoffButton);

            // 3:注入 UI 標籤結構 --> convertView
            convertView.setTag(holder);

        } else {
            // 取得  UI 標籤結構
            holder = (ViewHolder) convertView.getTag();
        }

        // 4:取得Fastfood物件資料
        final Smartsocket smartsocket = list.get(position);

        // 5:設定顯示資料
        holder.name.setText(smartsocket.getName());
        holder.starttime_hour.setText(String.format("%02d", smartsocket.getstarttime_hour())); // 記得要轉字串
        holder.starttime_minute.setText(String.format("%02d", smartsocket.getstarttime_minute())); // 記得要轉字串
        holder.endtime_hour.setText(String.format("%02d", smartsocket.getendtime_hour())); // 記得要轉字串
        holder.endtime_minute.setText(String.format("%02d", smartsocket.getendtime_minute())); // 記得要轉字串
        //holder.image.setImageResource(smartsocket.getImageId());
//        holder.onoffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                holder.onoffButton.setChecked(isChecked);
//                //holder.price.setText(smartsocket.getName() + " " + (isChecked ? "on" : "off"));
//            }
//        });
        holder.onoffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.onoffButton.setChecked(isChecked);
                holder.name.setText(smartsocket.getName() + " " + (isChecked ? "on" : "off"));
            }
        });
        return convertView;
    }


    // UI 標籤結構
    static class ViewHolder {
        TextView name;
        TextView starttime_hour;
        TextView starttime_minute;
        TextView endtime_hour;
        TextView endtime_minute;
        //ImageView image;
        SwitchCompat onoffButton;
        //ToggleButton onoffButton;
    }
}