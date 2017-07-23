package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class UserActivity extends Activity implements View.OnClickListener{
    EditText et_bill_code;
    EditText et_bill_num;
    EditText et_bill_date;
    EditText et_bill_money;
    Button btn_upload;
    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    public static final int REQUEST_CODE = 111;
    //处理消息
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1)
                et_bill_num.setError("该发票数据已存在！");
            if (msg.what==0)
                Toast.makeText(getApplicationContext(),"数据提交成功！",Toast.LENGTH_LONG).show();
            btn_upload.setClickable(true);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        et_bill_code = findViewById(R.id.editText_billcode);
        et_bill_num = findViewById(R.id.editText_billnum);
        et_bill_date = findViewById(R.id.editText_date);
        et_bill_money = findViewById(R.id.editText_billmoney);
        btn_upload = findViewById(R.id.btn_UpLoadData);
        findViewById(R.id.btn_UpLoadData).setOnClickListener(this);
        findViewById(R.id.btn_QR).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;}
                if (bundle.getInt(CodeUtils.RESULT_TYPE)
                        == CodeUtils.RESULT_SUCCESS) {
                    String result =
                            bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                    String []message = result.split(",");
//                    for ( int i = 0;i<message.length;i++){
//                        System.out.println(i+"  ----- "+message[i]);
//                    }
                    et_bill_code.setText(message[2]);
                    et_bill_num.setText(message[3]);
                    et_bill_money.setText(message[4]);
                    et_bill_date.setText(message[5]);

                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_QR:
                startActivityForResult(new Intent(UserActivity.this,CaptureActivity.class),REQUEST_CODE);
                break;
            case R.id.btn_UpLoadData:

                String billcode =  et_bill_code.getText().toString();
                String billnum = et_bill_num.getText().toString();
                String billdata = et_bill_date.getText().toString();
                String billmoney = et_bill_money.getText().toString();
                if(billcode.equals(null)||billcode.equals("")){
                    et_bill_code.setError("此项内容不能为空！");
                    return;
                }
                if (billnum.equals(null)||billnum.equals("")){
                    et_bill_num.setError(" 此项内容不能为空！");
                    return;
                }
                if (billdata.equals(null)||billdata.equals("")){
                    et_bill_date.setError("此项内容不能为空!");
                    return;
                }
                if (billmoney.equals(null)||billmoney.equals("")){
                    et_bill_money.setError("此项内容不能为空!");
                    return;
                }
                btn_upload.setClickable(false);
                new Thread(new Runnable() {
                    String billcode =  et_bill_code.getText().toString();
                    String billnum = et_bill_num.getText().toString();
                    String billdata = et_bill_date.getText().toString();
                    String billmoney = et_bill_money.getText().toString();
                    int uid = MainActivity.main_uid;
                    @Override
                    public void run() {
                        String selectsql ="USE [bill_data] SELECT *  FROM [bill_data].[dbo].[T_Message] where [bill_num]='"+billnum+"'";
                        String insertsql ="USE [bill_data] INSERT INTO [dbo].[T_Message] ([int_uid],[bill_code],[bill_num],[bill_date],[bill_moneynum],[is_checked]"+
                                ")Values ('"+uid+"','"+billcode+"','"+billnum+"','"+billdata+"','"+billmoney+"','0')";
                        if (databaseHandle.SelectSQL(selectsql))
                            handler.sendEmptyMessage(1);
                        else
                            {
                           int t =  databaseHandle.excuteSQL(insertsql);
                            handler.sendEmptyMessage(t-1);
                        }
                    }
                }).start();
                break;
        }
    }
}
