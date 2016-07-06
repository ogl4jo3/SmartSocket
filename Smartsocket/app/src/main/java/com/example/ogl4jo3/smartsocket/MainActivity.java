package com.example.ogl4jo3.smartsocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;

    private ArrayList<Smartsocket> listItem;
    private SmartsocketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        // Step 1 :定義商品資 料 for JavaBean
        /*Smartsocket hamburger = new Smartsocket("Hamburger", 7, 30, 8, 30);
        index++;
        Smartsocket french = new Smartsocket("French fries", 11, 40, 15, 50);
        index++;
        Smartsocket coca = new Smartsocket("Coca Cola", 21, 25, 23, 0);*/
        listItem = new Gson().fromJson(Memory.getString(this, "ListItem", null),
                new TypeToken<ArrayList<Smartsocket>>() {
                }.getType());
        if (listItem == null)
            listItem = new ArrayList<>();

        // Step 2 :將商品放入到List集合容器中
        /*listItem.add(hamburger);
        listItem.add(french);
        listItem.add(coca);*/

        // Step 3 :建立 FastfoodAdapter 適配器
        adapter = new SmartsocketAdapter(this, listItem);

        // Step 4 :設定適配器
        listView.setAdapter(adapter);

        // Step 5 :註冊 OnItemClickListener
        listView.setOnItemClickListener(this);
    }

    // 列表項目點選之事件處理
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 取得被點選之商品資料
        Smartsocket tmp = (Smartsocket) parent.getItemAtPosition(position);
        // 取出商品名稱, 價格
        String msg = "您選的是:" + tmp.getName() + ",定時 " + tmp.gethour()
                + tmp.getminute() ;
        // Toast 顯示
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Bundle extras = new Bundle();
        extras.putInt("new_edit", 1);   //new=0 , edit=1 , delete=2
        extras.putInt("position", position);
        extras.putString("name", tmp.getName());
        extras.putInt("hour", tmp.gethour());
        extras.putInt("minute", tmp.getminute());
        Intent intent = new Intent(this, NeweditActivity.class);
        intent.putExtras(extras);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_plus) {
            try {
                Bundle extras = new Bundle();
                extras.putInt("new_edit", 0);   //new=0 , edit=1 , delete=2
                Intent intent = new Intent(this, NeweditActivity.class);
                intent.putExtras(extras);
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                Toast.makeText(this, "intent error",
                        Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == resultCode) {
            String name = data.getStringExtra("name");
            int new_edit = data.getIntExtra("new_edit", 0);
            int position = data.getIntExtra("position", 0);
            int hour = data.getIntExtra("hour", 0);
            int minute = data.getIntExtra("minute", 0);
            Toast.makeText(this, name + "intent success" + new_edit, Toast.LENGTH_SHORT).show();

            //new=0 , edit=1 , delete=2
            if (new_edit == 0) {
                Smartsocket tmp = new Smartsocket(name, 1, 00);
                listItem.add(tmp);
            } else if (new_edit == 1) {
                Smartsocket edittmp = new Smartsocket(name, hour, minute);
                listItem.set(position, edittmp);
            } else if (new_edit == 2) {
                listItem.remove(position);
            }
            Memory.setObject(this, "ListItem", listItem);
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
