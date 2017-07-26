package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
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
            if (list.get(6).toString().equals("0"))
                billChecked.setText("审核中");
            else
                billChecked.setText("已批准");

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

        Bundle bundle = this.getIntent().getExtras();
        num = bundle.getString("billnum");
        System.out.println(num);
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = databaseHandle.ReadDBImage(num);
                String sql = "USE [bill_data] select [s_name],[s_orgname],[bill_num],[bill_date],[bill_code],[bill_moneynum],[is_checked]  from [bill_data].[dbo].[View_message] where bill_num ='"+num+"'";
                list = databaseHandle.SelectSQLname(sql);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }
}
