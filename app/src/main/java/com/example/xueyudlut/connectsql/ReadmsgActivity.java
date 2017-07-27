package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ReadmsgActivity extends Activity {


    String num ;
    Bitmap bitmap =null;
    List<String> list;
    private ImageView imageView;
    private TextView billcode;
    private TextView  billnum;
    private TextView billdate;
    private TextView billmoney;
    private TextView billOrg;
    private TextView billChecked;
    private  TextView billusername;
    private  TextView billcheckmsg;
    private Button btn_reupload;
    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            imageView.setImageBitmap(bitmap);
            billusername.setText(list.get(0));
            billOrg.setText(list.get(1));
            billnum.setText(list.get(2));
            billdate.setText(list.get(3));
            billcode.setText(list.get(4));
            billmoney.setText(list.get(5));
            if (list.get(6)==null || list.get(6).toString().equals("0"))
                billChecked.setText("审核中");
            else if (list.get(6).toString().equals("1")){
                billChecked.setText("已批准");
                btn_reupload.setVisibility(View.GONE);
            }

            else
                billChecked.setText("未通过");
            billcheckmsg.setText(list.get(7));

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readmsg);
        imageView = findViewById(R.id.read_img);
        billcode = findViewById(R.id.read_billcode);
        billnum = findViewById(R.id.read_billnum);
        billdate = findViewById(R.id.read_billdate);
        billmoney = findViewById(R.id.read_money);
         billOrg= findViewById(R.id.read_org);
        billChecked= findViewById(R.id.read_billcheckstate);
        billusername = findViewById(R.id.read_username);
        billcheckmsg = findViewById(R.id.read_msg);
        btn_reupload = findViewById(R.id.btn_reUpload);
        if (MainActivity.main_usermode==1)
            btn_reupload.setVisibility(View.GONE);
        final Bundle bundle = this.getIntent().getExtras();
        num = bundle.getString("billnum");
        System.out.println(num);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = databaseHandle.ReadDBImage(num);
                String sql = "USE [bill_data] select [s_name],[s_orgname],[bill_num],[bill_date],[bill_code],[bill_moneynum],[int_boss_checkstate], [Str_readme] from [bill_data].[dbo].[View_message] where bill_num ='"+num+"'";
                list = databaseHandle.SelectSQLname(sql);
                handler.sendEmptyMessage(0);
            }
        }).start();
        btn_reupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ReadmsgActivity.this,UserActivity.class);
                it.putExtras(getIntent().getExtras());
                startActivity(it);
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
}
