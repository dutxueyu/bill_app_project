package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserManageActivity extends Activity {

    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    private ListView listView ;
    List<String> list;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,getData()));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        listView = findViewById(R.id.list_users);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                list = getData();
                String sql ="USE [bill_data] select [s_name],[int_uid], [int_boss_check] from [bill_data].[dbo].[T_User] where int_uid !='"+MainActivity.main_uid+"' and  int_orgID =(select [int_orgID] from T_User where int_uid ='"+MainActivity.main_uid+"')" ;
                list = databaseHandle.SelectSQLname(sql);
                handler.sendEmptyMessage(0);
            }
        }).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String clickUid = list.get(i*3+1);
                Toast.makeText(getApplicationContext(),clickUid+"oo"+i,Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("uid",clickUid);
                Intent intent = new Intent(UserManageActivity.this,BossCheckUserActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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

    private List<String> getData(){
        List<String> list_get =new ArrayList<String>();
        for (int i=0;i<list.size();i=i+3){
            if (list.get(i+2).toString().equals("0"))
                list_get.add(list.get(i)+"---未审核");
            else
                list_get.add(list.get(i));
        }
        return list_get;
    }
}