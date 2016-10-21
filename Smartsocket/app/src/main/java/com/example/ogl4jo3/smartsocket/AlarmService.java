package com.example.ogl4jo3.smartsocket;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.ogl4jo3.smartsocket.PickerView.TAG;

/**
 * Created by ogl4jo3 on 2016/10/7.
 */

public class AlarmService extends Service {

	private OkHttpClient client;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		final String device_id = intent.getExtras().getString("device_id");

		client = new OkHttpClient();

		// 抓取報價
		new Thread() {

			@Override
			public void run() {
				//StockService stockService = new StockService();
				//Stock stock = stockService.getStock(stockNo);

				// 建立 Notification
				int nid = 1;
				NotificationManager notificationManager =
						(NotificationManager) getApplicationContext()
								.getSystemService(Context.NOTIFICATION_SERVICE);
				NotificationCompat.Builder builder =
						new NotificationCompat.Builder(getApplicationContext());
				builder.setSmallIcon(R.drawable.coca_cola) // 通知服務 icon
						.setContentTitle("Smartsocket定時結束") // 標題
						.setContentText("裝置" + device_id + "已關閉") // 內文
						//.setContentInfo("昨收：" ) // 信息
						.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);
				writedata("field" + device_id, "0");
				builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
				// 抬頭顯示儀
				builder.setPriority(
						NotificationCompat.PRIORITY_HIGH); // 亦可帶入Notification.PRIORITY_MAX參數
				notificationManager.notify(nid, builder.build()); // 發佈Notification
			}

		}.start();

		// 關閉服務
		stopSelf();
		return Service.START_REDELIVER_INTENT;
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

	@Override
	public void onDestroy() {
		Log.i("mylog", "onDestroy()");
		super.onDestroy();
	}
}
