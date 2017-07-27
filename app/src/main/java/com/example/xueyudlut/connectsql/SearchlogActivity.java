package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SearchlogActivity extends Activity {

    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    ImageView img;
    ListView listView;
    List<String> data;
    Thread refresh   = new Thread(new Runnable() {
        @Override
        public void run() {
            String sql;
            if (MainActivity.main_usermode==2)
                sql = "USE [bill_data] select [s_name],[bill_num],[bill_date],[int_boss_checkstate],[int_cout_m] from [bill_data].[dbo].[View_message] where int_uid ='"+MainActivity.main_uid+"'";
            else
                sql = "USE [bill_data] select [s_name],[bill_num],[bill_date],[int_boss_checkstate],[int_cout_m]  from [bill_data].[dbo].[View_message] where int_orgid = (select int_orgid  FROM T_User where int_uid ='"+MainActivity.main_uid+"')";
            data =  databaseHandle.SelectSQLname(sql);
            handler.sendEmptyMessage(0);
        }
    });
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
        TextView tile = findViewById(R.id.search_title);
        if (MainActivity.main_usermode==1){
            tile.setText("管理员界面—检索记录");
            findViewById(R.id.search_tip).setVisibility(View.VISIBLE);
        }

        img= findViewById(R.id.image_test);
        listView = findViewById(R.id.list_itemw);
        refresh.start();


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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (MainActivity.main_usermode==1){
                    Map<String, Object> map =list.get(i);
                    Intent it =new  Intent(SearchlogActivity.this,BossPassActivity.class);
                    Bundle bundle= new Bundle();
                    bundle.putString("msg_id",map.get("msg_id").toString());
                    it.putExtras(bundle);
                    startActivity(it);
                }
                return true;
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

        for (int i =0;i<data.size();i=i+5){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", data.get(i));
            map.put("num", data.get(i+1));
            map.put("date", data.get(i+2));
            if (data.get(i+3)==null||data.get(i+3).toString().equals("0"))
                map.put("check","审核中");
            else if(data.get(i+3).toString().equals("1")){
                map.put("check","通过");
            }else{
                map.put("check","不通过");
            }
            map.put("msg_id",data.get(i+4));
            list.add(map);
        }
        return list;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
