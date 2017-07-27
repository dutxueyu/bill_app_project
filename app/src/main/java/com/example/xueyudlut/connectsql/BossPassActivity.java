package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;

public class BossPassActivity extends Activity implements View.OnClickListener{

    boolean is_firstChecked =false;
    private String msg_id;
    private  RadioButton btn_passed;
    private  RadioButton btn_nopass;
    private Button btn_submit;
    private EditText ed_nopassReason;
    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    List<String> list;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                if (list.get(0).equals("1"))
                    btn_passed.setChecked(true);
                if (list.get(0).equals("-1"))
                    btn_nopass.setChecked(true);
                ed_nopassReason.setText(list.get(1).toString());
            }
            if (msg.what==4){
                Toast.makeText(getApplicationContext(),"提交成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            if (msg.what==3)
                Toast.makeText(getApplicationContext(),"提交失败",Toast.LENGTH_SHORT).show();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_pass);
        Bundle  bundle = this.getIntent().getExtras();
        btn_passed = findViewById(R.id.rb_passed);
        btn_nopass =findViewById(R.id.rb_nopassed);
        btn_nopass.setChecked(true);
        btn_submit =findViewById( R.id.btn_submit);
        ed_nopassReason = findViewById(R.id.edit_nopassReson);
        msg_id = bundle.getString("msg_id");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql ="USE [bill_data] select [int_boss_checkstate],[Str_readme] from [bill_data].[dbo].[T_bossCheckMsg] where [int_cout_m] ='"+msg_id+"'";
                is_firstChecked = databaseHandle.SelectSQL(sql);
                if (is_firstChecked){
                    list = databaseHandle.SelectSQLname(sql);
                    handler.sendEmptyMessage(1);
                }
                handler.sendEmptyMessage(0);


            }
        }).start();
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String sql;
                int i;
                if (btn_passed.isChecked())
                    i =1;
                else
                    i=-1;
                if (is_firstChecked)//update
                    sql= "USE [bill_data] update  [dbo].[T_bossCheckMsg] set [int_boss_checkstate] ='"+ i+"',[Str_readme] ='"+ed_nopassReason.getText()+"' where [int_cout_m] ='"+msg_id+"'";
                else
                    sql = " USE [bill_data] insert into [T_bossCheckMsg]([int_cout_m],[int_boss_checkstate],[Str_readme]) values ('"+msg_id+"','"+i+"','"+ed_nopassReason.getText()+"')";
                int get = databaseHandle.excuteSQL(sql);
                handler.sendEmptyMessage(get+3);//异常2，错误3，正确4
            }
        });
        thread.start();
    }
}
