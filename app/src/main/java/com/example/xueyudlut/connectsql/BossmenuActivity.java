package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class BossmenuActivity extends Activity implements View.OnClickListener{

    Button btn_goCheckbill;
    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> list =(List<String>) msg.obj;
            tx.append(list.get(0));
        }
    };
    TextView tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bossmenu);
        btn_goCheckbill = findViewById(R.id.btn_billchecked);
        btn_goCheckbill.setOnClickListener(this);
        tx= findViewById(R.id.ttid);
        findViewById(R.id.btn_usermanage).setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "USE [bill_data] SELECT int_orgID from [dbo].[T_User] where int_uid = '"+MainActivity.main_uid+"'" ;
                List<String> list = databaseHandle.SelectSQLname(sql);
                Message msg = new Message();
                msg.obj =list;
                msg.what= 10;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_billchecked:
            {
                Intent it =new Intent(BossmenuActivity.this,SearchlogActivity.class);
                startActivity(it);
                break;
            }
            case R.id.btn_usermanage:
            {
                startActivity(new Intent(BossmenuActivity.this,UserManageActivity.class));
                break;
            }
        }
    }
}
