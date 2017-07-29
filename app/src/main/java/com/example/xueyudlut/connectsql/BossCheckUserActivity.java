package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BossCheckUserActivity extends Activity implements View.OnClickListener{

    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    List<String> list ;
    TextView tx_checkname;
    TextView tx_logname;
    TextView tx_checkstate;
    Button btnacpt;
    Button btndlt;
    static String uid;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==10){
                tx_checkname.setText(list.get(0));
                tx_logname.setText(list.get(1));
                if (list.get(2).toString().equals("1")){
                    btnacpt.setVisibility(View.GONE);
                    tx_checkstate.setText("已通过");
                }else{
                    tx_checkstate.setText("未审核");
                }

            }
           if (msg.what==1)
           {
               Toast.makeText(getApplicationContext(),"操作成功",Toast.LENGTH_LONG);
               finish();
           }


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_check_user);
        tx_checkname =findViewById(R.id.checkname);
        tx_logname = findViewById(R.id.log_name);
        tx_checkstate= findViewById(R.id.txt_check_state);
        btnacpt= findViewById(R.id.btn_acceptUser);
        btndlt = findViewById(R.id.btn_deleteUser);
        btndlt.setOnClickListener(this);
        btnacpt.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql ="USE [bill_data] select [s_name],[s_loginName],[int_boss_check]  from [bill_data].[dbo].[T_User] where int_uid ='"+uid+"'" ;
                list = databaseHandle.SelectSQLname(sql);
                handler.sendEmptyMessage(10);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_acceptUser:
            {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sql= "USE [bill_data] update [bill_data].[dbo].[T_User]  set [int_boss_check] ='1' where int_uid ='"+uid+"'" ;
                        handler.sendEmptyMessage(databaseHandle.excuteSQL(sql)) ;
                    }
                }).start();
                break;
            }
            case R.id.btn_deleteUser:
            {
                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sql= "USE [bill_data]  delete from  [bill_data].[dbo].[T_User] where int_uid ='"+uid+"'" ;
                        handler.sendEmptyMessage(databaseHandle.excuteSQL(sql)) ;
                    }
                }).start();
                break;
            }
            default:
                break;

        }
    }
}
