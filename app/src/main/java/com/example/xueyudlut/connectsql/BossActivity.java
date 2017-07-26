package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class BossActivity extends Activity implements View.OnClickListener{

    private TextView billcode;
    private TextView billnum;
    private TextView billmoney;
    private TextView billdate;
    private TextView person;
    private TextView username;
    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<String> name = (List<String>) msg.obj;
            if (msg.what==1){
                username.setText("");
                for (String s:name)
                {
                    username.append(s+"\n");
                }
            }
            if (msg.what==2){
                int count = 0;
                for (String s:name) {
                    switch (count%5){
                        case 0:
                            person.append("\n"+s);
                            break;
                        case 1:
                            billcode.append("\n"+s);
                            break;
                        case 2:
                            billnum.append("\n"+s);
                            break;
                        case 3:
                            billdate.append("\n"+s);
                            break;
                        case  4:
                            billmoney.append("\n"+s);
                            break;
                    }
                    count++;

                }

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss);
        billcode  = findViewById(R.id.textViewcode);
        billdate = findViewById(R.id.textViewbilldate);
        billmoney = findViewById(R.id.textViewbillmoney);
        billnum = findViewById(R.id.textViewbillnum);
        person = findViewById(R.id.tx_providername);
        username = findViewById(R.id.users_name);
        findViewById(R.id.btn_finduser).setOnClickListener(this);
        findViewById(R.id.btn_findmsg).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_finduser:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sql ="USE [bill_data] SELECT [s_name] FROM T_User where int_orgid = (select int_orgid  FROM T_User where int_uid ='"+MainActivity.main_uid+"');";
                        List<String> name =databaseHandle.SelectSQLname(sql);
                        Message msg = handler.obtainMessage();
                        msg.what =1;
                        msg.obj = name;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
            case  R.id.btn_findmsg:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sql = "USE [bill_data] select [s_name], [bill_code],[bill_num],[bill_date],[bill_moneynum] from [bill_data].[dbo].[View_message] where int_orgid = (select int_orgid  FROM T_User where int_uid ='"+MainActivity.main_uid+"');";
                        List<String> mssage =databaseHandle.SelectSQLname(sql);
                        Message msg = handler.obtainMessage();
                        msg.what =2;
                        msg.obj = mssage;
                        handler.sendMessage(msg);
                    }
                }).start();


        }
    }
}
