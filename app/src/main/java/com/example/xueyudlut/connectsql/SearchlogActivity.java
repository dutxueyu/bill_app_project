package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class SearchlogActivity extends Activity {

    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    ImageView img;
    ListView listView;
    List<String> data;
    List<Map<String, Object>> list;
    Bitmap bt = null;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listView.setAdapter(new SimpleAdapter(getApplicationContext(),getData(),R.layout.vlist, new String[]{"name","num","date","check"},new int[]{R.id.name,R.id.num,R.id.date,R.id.txt_chenck}));
          //  listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,getData()));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchlog);
        img= findViewById(R.id.image_test);
        listView = findViewById(R.id.list_itemw);
        new Thread(new Runnable() {
            @Override
            public void run() {
                 String sql = "USE [bill_data] select [s_name],[bill_num],[bill_date],[is_checked]  from [bill_data].[dbo].[View_message] where int_uid ='"+MainActivity.main_uid+"'";
                 data =  databaseHandle.SelectSQLname(sql);
                 handler.sendEmptyMessage(0);
            }
        }).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map =list.get(i);
                System.out.println("out"+i+"\\"+l+"\\"+  map.get("num"));
                Intent it = new Intent(SearchlogActivity.this,ReadmsgActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("billnum", map.get("num").toString());
                it.putExtras(bundle);
                startActivity(it);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
               // bt= databaseHandle.ReadDBImage();
               // handler.sendEmptyMessage(0);
            }
        }).start();

    //    img.setImageBitmap(bt);

    }
    private  List<Map<String, Object>> getData(){

       list = new ArrayList<Map<String, Object>>();

        for (int i =0;i<data.size();i=i+4){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", data.get(i));
            map.put("num", data.get(i+1));
            map.put("date", data.get(i+2));
            if (data.get(i+3).toString().equals("0"))
                map.put("check","审核中");
            else{
                map.put("check","通过");
            }
            list.add(map);
        }




        return list;
    }


}
