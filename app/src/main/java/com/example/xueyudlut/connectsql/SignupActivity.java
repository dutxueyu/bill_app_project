package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;

public class SignupActivity extends Activity {

    private EditText et_username;
    private EditText et_psword1;
    private EditText et_psword2;
    private EditText et_org;
    private EditText et_orgname;
    private EditText et_name;
    private Button btn_signup;
    private ProgressBar progressBar;
    private RadioGroup rg;
     RadioButton rd_boss;
     RadioButton rd_use;

    private DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        et_username = findViewById(R.id.txt_username);
        et_psword1 = findViewById(R.id.txt_password1);
        et_psword2 =findViewById(R.id.txt_password2);
        et_org  =findViewById(R.id.txt_org);
        et_orgname = findViewById(R.id.txt_orgname);
        et_org.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        et_name = findViewById(R.id.txt_name);
        rd_boss = findViewById(R.id.radiobtn_boss);
        rd_use = findViewById(R.id.radioBtn_user);
        rd_boss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"点击检测！"+rd_use.isChecked(),Toast.LENGTH_SHORT).show();
               et_orgname.setVisibility(View.VISIBLE);
                et_org.setVisibility(View.INVISIBLE);

            }
        });

        rd_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"点击检测2！",Toast.LENGTH_SHORT).show();
               et_orgname.setVisibility(View.INVISIBLE);
                et_org.setVisibility(View.VISIBLE);
            }
        });
        //测试用
//        et_username.setText("a");
//        et_psword1.setText("a");
//        et_psword2.setText("a");
//        et_org.setText("a");
//        et_name.setText("a");

        btn_signup = findViewById(R.id.btn_signin);
        progressBar = findViewById(R.id.processbar);
        rg = findViewById(R.id.radiogroup);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                btn_signup.setEnabled(true);
               if(msg.what==1){
                   et_username.setError("用户名已经存在！");
                   Toast.makeText(getApplicationContext(),"用户名已经存在！",Toast.LENGTH_LONG).show();
                   Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                   long[] patter = {100,400,100,400};
                   v.vibrate(patter,-1);
                   progressBar.setVisibility(View.GONE);
               }
               if (msg.what==2){
                   et_org.setError("公司ID不存在！");
                   Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                   long[] patter = {100,400,100,400};
                   v.vibrate(patter,-1);
                   progressBar.setVisibility(View.GONE);
               }
                if(msg.what==0){
                    Toast.makeText(getApplicationContext(),"用户名可用！",Toast.LENGTH_LONG).show();
                    boolean is_boss = rd_boss.isChecked();
//                    if (is_boss){
//                        new Thread(new Runnable() {
//                            String username = et_username.getText().toString();
//                            String psword1 =et_psword1.getText().toString();
//                            String orgyid = et_org.getText().toString();
//                            String name = et_name.getText().toString();
//                            String orgname= et_orgname.getText().toString();
//                            String insertsql =" USE [bill_data] insert into [T_User]([s_name],[int_usermode],[int_orgID],[s_loginName],[s_password]) values ('"+name
//                                    +"','1','"+orgyid+"','"+username+"','"+psword1+"')";
//                            String insertORG = "USE [bill_data] insert into [T_org] ([s_orgname]) values ('"+orgname+"')";
//                            @Override
//                            public void run() {
//                                int count = databaseHandle.excuteSQL(insertsql);
//                                System.out.println("/////"+count);
//                                handler.sendEmptyMessage(3);
//                            }
//                        }).start();
//                    }else{
                        new Thread(new Runnable() {
                            String username = et_username.getText().toString();
                            String psword1 =et_psword1.getText().toString();
                            String orgyid = et_org.getText().toString();
                            String name = et_name.getText().toString();


                            String insertsql =" USE [bill_data] insert into [T_User]([s_name],[int_usermode],[int_orgID],[s_loginName],[s_password]) values ('"+name
                                    +"','2','"+orgyid+"','"+username+"','"+psword1+"')";

                            @Override
                            public void run() {
                                int count = databaseHandle.excuteSQL(insertsql);
                                System.out.println("/////"+count);
                                handler.sendEmptyMessage(3);
                            }
                        }).start();
                }
                if (msg.what==3){
                    Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_LONG).show();
                    finish();
                }
                if (msg.what==12){
                    et_org.setError("该公司ID已存在！");
                    Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                    long[] patter = {100,400,100,400};
                    v.vibrate(patter,-1);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"该公司ID已存在！",Toast.LENGTH_LONG).show();
                }
                if (msg.what==10){
                    List<String> ls =(List<String>) msg.obj;
                    final String orgyid= ls.get(0);
                    new Thread(new Runnable() {
                            String username = et_username.getText().toString();
                            String psword1 =et_psword1.getText().toString();
                            String name = et_name.getText().toString();
                            String insertsql =" USE [bill_data] insert into [T_User]([s_name],[int_usermode],[int_orgID],[s_loginName],[s_password],[int_boss_check]) values ('"+name
                                    +"','1','"+orgyid+"','"+username+"','"+psword1+"','1')";
                            @Override
                            public void run() {
                                int count = databaseHandle.excuteSQL(insertsql);
                                System.out.println("/////"+count);
                                handler.sendEmptyMessage(3);
                            }
                        }).start();
                }



            }
        };

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                User_Signup();
            }
        });
    }
    private void User_Signup(){
        String username = et_username.getText().toString();
        String psword1 =et_psword1.getText().toString();
        String psword2 =et_psword2.getText().toString();

        final String org = et_org.getText().toString();
        String name = et_name.getText().toString();
        //内容验证
        if (username==null||username.trim().equals("")){
            et_username.setError("用户名不能为空！");
            return;
        }
        if (psword1==null||psword1.trim().equals("")){
            et_psword1.setError("此项内容不能为空！");
            return;
        }
        if (psword2==null||psword2.trim().equals("")){
            et_psword2.setError("此项内容不能为空！");
            return;
        }
        if (org==null||org.trim().equals("")){
            et_org.setError("此项内容不能为空！");

        }
        if (name==null||name.trim().equals("")){
            et_name.setError("此项内容不能为空！");
            return;
        }
        if (!psword1.equals(psword2)){
            et_psword2.setError("密码输入不一致！");
            return;
        }
        btn_signup.setEnabled(false);
        if(rd_boss.isChecked()){
//            Toast.makeText(getApplicationContext(),"暂不接受管理员注册！",Toast.LENGTH_SHORT).show();
//            管理员注册
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String name = et_username.getText().toString();
                    String orgname = et_orgname.getText().toString();
                    String sql = "USE [bill_data] SELECT *  FROM [bill_data].[dbo].[T_User] where [s_loginName] ='"+name+"'";
                    if (databaseHandle.SelectSQL(sql)){
                        handler.sendEmptyMessage(1);
                    }else {
                        String orgsql =  "USE [bill_data] insert into [T_org] ([s_orgname]) values ('"+orgname+"')";
                        int t =databaseHandle.excuteSQL(orgsql);
                        String selectorgsql =  "USE [bill_data] select  [int_orgid] FROM [dbo].[T_org] where [s_orgname] ='"+orgname+"'";
                        List<String> list = databaseHandle.SelectSQLname(selectorgsql);
                        Message msg = new Message();
                        msg.obj = list;
                        msg.what =10;
                        handler.sendMessage(msg);
                    }
                }
            }).start();

        }else  {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String name = et_username.getText().toString();
                    String sql = "USE [bill_data] SELECT *  FROM [bill_data].[dbo].[T_User] where [s_loginName] ='"+name+"'";
                    if (databaseHandle.SelectSQL(sql)){
                        handler.sendEmptyMessage(1);
                    }else {
                        String orgid = et_org.getText().toString();
                        String orgsql = "USE [bill_data] SELECT *  FROM [bill_data].[dbo].[T_org] where [int_orgid] ='" + orgid + "'";
                        if (!databaseHandle.SelectSQL(orgsql))
                            handler.sendEmptyMessage(2);
                        else {
                            handler.sendEmptyMessage(0);
                        }
                    }
                }
            }).start();
        }



      //  rg.getCheckedRadioButtonId();

    }
}
