package com.example.ogl4jo3.smartsocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ogl4jo3 on 2016/4/13.
 */
public class NeweditActivity extends Activity implements View.OnClickListener {

	private EditText nameEditText;
	private Spinner idspinner;
	private ArrayAdapter<String> adp_id;
	private OkHttpClient client;
	private String Jsondata = "";
	private ArrayList<String> entry_id, field1;
	private Button parsebutton;
	private Button okButton, deleteButton;
	int new_edit;
	int position;
	String name;
	String device_id;
	int hour, minute;

	com.example.ogl4jo3.smartsocket.PickerView minute_pv;
	com.example.ogl4jo3.smartsocket.PickerView second_pv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_edit_layout);
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		idspinner = (Spinner) findViewById(R.id.idspinner);
		parsebutton = (Button) findViewById(R.id.parsebutton);
		parsebutton.setOnClickListener(this);
		entry_id = new ArrayList<String>();
		field1 = new ArrayList<String>();
		client = new OkHttpClient();
		getjson();

		//timepicker
		minute_pv = (com.example.ogl4jo3.smartsocket.PickerView) findViewById(R.id.minute_pv);
		second_pv = (com.example.ogl4jo3.smartsocket.PickerView) findViewById(R.id.second_pv);
		List<String> minutes = new ArrayList<String>();
		List<String> seconds = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			minutes.add(i < 10 ? "0" + i : "" + i);
		}
		for (int i = 0; i < 60; i += 5) {
			seconds.add(i < 10 ? "0" + i : "" + i);
		}
		minute_pv.setData(minutes);
		minute_pv.setOnSelectListener(new PickerView.onSelectListener() {

			@Override
			public void onSelect(String text) {
				hour = Integer.parseInt(text);
				Toast.makeText(NeweditActivity.this, "選擇了 " + text + " 小時", Toast.LENGTH_SHORT)
						.show();
			}
		});
		second_pv.setData(seconds);
		second_pv.setOnSelectListener(new PickerView.onSelectListener() {

			@Override
			public void onSelect(String text) {
				minute = Integer.parseInt(text);
				Toast.makeText(NeweditActivity.this, "選擇了 " + text + " 分鐘", Toast.LENGTH_SHORT)
						.show();
			}
		});

		idspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String str = parent.getItemAtPosition(position).toString();
				device_id = str;
				Toast.makeText(NeweditActivity.this, "選擇了 " + str, Toast.LENGTH_SHORT).show();
				//idtextview.setText(str);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		Bundle extras = getIntent().getExtras();
		name = extras.getString("name");
		//hour = extras.getInt("hour");
		//minute = extras.getInt("minute");
		hour = 1;
		minute = 5;
		new_edit = extras.getInt("new_edit");   //new=0 , edit=1 , delete=2
		position = extras.getInt("position");
		nameEditText.setText(name);

		okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(this);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(this);
		if (new_edit == 0) {
			deleteButton.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == okButton) {
			if ((nameEditText.length() == 0) || (device_id == null)) {
				Toast.makeText(NeweditActivity.this, "請輸入裝置名稱並選擇裝置id", Toast.LENGTH_LONG).show();
			} else {
				Intent intent = new Intent();
				intent.putExtra("name", nameEditText.getText().toString());
				intent.putExtra("device_id", device_id);
				intent.putExtra("hour", hour);
				intent.putExtra("minute", minute);
				intent.putExtra("new_edit", new_edit);   //new=0 , edit=1 , delete=2
				intent.putExtra("position", position);
				setResult(1, intent);
				finish();
			}
		} else if (v == deleteButton) {
			Intent intent = new Intent();
			intent.putExtra("new_edit", 2);   //new=0 , edit=1 , delete=2
			intent.putExtra("position", position);
			setResult(1, intent);
			finish();
		} else if (v == parsebutton) {
			parseJson();
			adp_id = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
					field1);
			idspinner.setAdapter(adp_id);
		}
	}

	private void getjson() {
		Request request = new Request.Builder().url(Constant.geturl).get().build();

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String body = response.body().string();
				//Log.d(TAG, body);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Jsondata = body;
						//gettextview.setText(body);
					}
				});
			}
		});
	}

	private String correctJsondata(String Jsondata) {
		String correctjsondata = "[";
		int count = 0;
		for (int i = 0; i < Jsondata.length() - 1; i++) {
			if (Jsondata.charAt(i) == '{' && count < 3) {
				count++;
			}
			if (count == 3) {
				correctjsondata += Jsondata.charAt(i);
			}
		}
		return correctjsondata;
	}

	private void parseJson() {
		//getjson();
		Log.e(Constant.TAG, "Jsondata: " + Jsondata);
		Log.e(Constant.TAG, "correctJsondata(Jsondata) " + correctJsondata(Jsondata));
		//gettextview.setText(correctJsondata(Jsondata));
		try {
			JSONArray feedsArray = new JSONArray((correctJsondata(Jsondata)));
			for (int i = 0; i < feedsArray.length(); i++) {
				JSONObject jsfeeds = feedsArray.getJSONObject(i);
				Log.e(Constant.TAG, "entry_id: " + jsfeeds.optString("entry_id"));
				Log.e(Constant.TAG, "field1: " + jsfeeds.optString("field1"));
				entry_id.add(jsfeeds.optString("entry_id"));
				field1.add(jsfeeds.optString("field1"));
				//Toast.makeText(this, jsEmployee.getString("name"), Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			Log.e(Constant.TAG, "parseJson: GGG");
			//Toast.makeText(this, Jsondata, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

}
