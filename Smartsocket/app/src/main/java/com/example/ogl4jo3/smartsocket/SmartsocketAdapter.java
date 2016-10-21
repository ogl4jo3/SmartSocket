package com.example.ogl4jo3.smartsocket;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.ALARM_SERVICE;
import static com.example.ogl4jo3.smartsocket.PickerView.TAG;
import static com.example.ogl4jo3.smartsocket.R.id.minute;

public class SmartsocketAdapter extends BaseAdapter {

	// 定義 LayoutInflater
	private LayoutInflater myInflater;
	// 定義 Adapter 內藴藏的資料容器
	private ArrayList<Smartsocket> list;

	private OkHttpClient client;

	private Context mcon;
	private Calendar calendar = Calendar.getInstance();
	private PendingIntent sender;

	public SmartsocketAdapter(Context context, ArrayList<Smartsocket> list) {
		mcon = context;
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

		client = new OkHttpClient();

		final ViewHolder holder;

		if (convertView == null) {
			// 1:將 R.layout.row 實例化
			convertView = myInflater.inflate(R.layout.item_main, parent, false);

			// 2:建立 UI 標籤結構並存放到 holder
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.device_id = (TextView) convertView.findViewById(R.id.device_id);
			holder.hour = (TextView) convertView.findViewById(R.id.hour);
			holder.minute = (TextView) convertView.findViewById(minute);
			holder.onoffButton = (SwitchCompat) convertView.findViewById(R.id.onoffButton);
			holder.timeButton = (SwitchCompat) convertView.findViewById(R.id.timeButton);

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
		holder.device_id.setText(smartsocket.getDevice_id());
		holder.hour.setText(String.format("%02d", smartsocket.gethour())); // 記得要轉字串
		holder.minute.setText(String.format("%02d", smartsocket.getminute())); // 記得要轉字串
		holder.onoffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				holder.onoffButton.setChecked(isChecked);
				Log.e(Constant.TAG, smartsocket.getDevice_id() + " onoffButtonCheckedChanged: " +
						(isChecked ? "on" : "off"));
				if (isChecked) {
					writedata("field" + smartsocket.getDevice_id(), "1");
				} else {
					writedata("field" + smartsocket.getDevice_id(), "0");
				}
			}
		});
		holder.timeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				holder.timeButton.setChecked(isChecked);
				Log.e(Constant.TAG, smartsocket.getDevice_id() + " timeButtonCheckedChanged: " +
						(isChecked ? "on" : "off"));
				if (isChecked) {
					// 指定鬧鐘設定時間到時要執行AlarmService.class
					Intent intent = new Intent(mcon, AlarmService.class);
					intent.putExtra("device_id", smartsocket.getDevice_id());
					// 建立PendingIntent
					sender = PendingIntent
							.getService(mcon, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

					calendar.setTimeInMillis(System.currentTimeMillis());
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, smartsocket.gethour());
					calendar.set(Calendar.SECOND, smartsocket.getminute());
					calendar.set(Calendar.MILLISECOND, 0);
					Log.e(Constant.TAG, "hour: " + smartsocket.gethour() + " minute: " +
							smartsocket.getminute());
					/*
					* AlarmManager.RTC_WAKEUP設定服務在系統休眠時同樣會執行
			        * 以set()設定的PendingIntent只會執行一次
			        */
					AlarmManager alarmManager = (AlarmManager) mcon.getSystemService(ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
				} else {
					// 由AlarmManager中移除
					AlarmManager alarmManager = (AlarmManager) mcon.getSystemService(ALARM_SERVICE);
					alarmManager.cancel(sender);
					// 以Toast提示已刪除設定，並更新顯示的鬧鐘時間
					Toast.makeText(mcon, R.string.remove_alarm, Toast.LENGTH_SHORT).show();
				}

			}
		});
		//holder.onoffButton.performClick();
		return convertView;
	}

	private void writedata(String field, String onoff) {
		HttpUrl url = HttpUrl.parse("http://api.thingspeak.com/update");
		HttpUrl.Builder builder = url.newBuilder();
		builder.addQueryParameter("key", "ZSB7XOYWG6K9QWQ3");
		builder.addQueryParameter(field, onoff);

		Request request = new Request.Builder().url(builder.build()).get().build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

				final String body = response.body().string();
				Log.d(TAG, body);
				/*runOnUiThread(new Runnable() {

					@Override
					public void run() {
						//gettextview.setText(body);
					}
				});*/
			}
		});
	}

	// UI 標籤結構
	static class ViewHolder {

		TextView name;
		TextView device_id;
		TextView hour;
		TextView minute;
		SwitchCompat onoffButton;
		SwitchCompat timeButton;
	}
}