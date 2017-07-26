package com.example.xueyudlut.connectsql;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;



public class MainActivity extends Activity implements View.OnClickListener{

    private   RadioButton rb_boss;
    private RadioButton rb_user;
    private EditText et_username;
    private EditText et_password;
    private ProgressBar progressBar;
    // 执行语句
    private String sql = "USE [bill_data] insert into [Test_table]([id],[name]) values ('2','aaa')";

    public static  int main_uid = -1;
    // handler处理对象，用于在跨线程时，在线程间的响应，用于控制主线程的控件（不能跨线程控制控件）
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0)
                Toast.makeText(getApplicationContext(),"用户名或密码错误" +
                        "！",Toast.LENGTH_LONG).show();
            if (msg.what==1){
                Toast.makeText(getApplicationContext(),"登陆成功！",Toast.LENGTH_LONG).show();
                //跳转管理员界面
                startActivity(new Intent(MainActivity.this,BossmenuActivity.class));
            }
             if (msg.what==2){
                 Toast.makeText(getApplicationContext(),"登陆成功！",Toast.LENGTH_LONG).show();
                 //跳转用户界面
                 startActivity(new Intent(MainActivity.this,UserMenuActivity.class));
             }
            progressBar.setVisibility(View.GONE);
        }
    };
    // 执行结果，受影响行数
    private int resultCount;
    DatabaseHandle databaseHandle = DatabaseHandle.getDatabaseHandle();
    String test = "USE [bill_data] SELECT *  FROM [bill_data].[dbo].[T_User] where [s_loginName] ='xueyu'";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_sign).setOnClickListener(this);
        rb_boss = findViewById(R.id.radiobtn_boss_log);
        rb_user = findViewById(R.id.radioBtn_user_log);
        et_username = findViewById(R.id.txt_username);
        et_password = findViewById(R.id.txt_password);
        progressBar = findViewById(R.id.processbar);
        et_username.setText("1");
        et_password.setText("1");
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {


        progressBar.setVisibility(View.VISIBLE);
        switch (view.getId()){
            case R.id.btn_login:{
                String psw = et_password.getText().toString();
                String unamw = et_username.getText().toString();
                if(unamw.equals(null)||unamw.trim().equals("")){
                    et_username.setError("用户名不能为空！");
                    return;
                }
                if (psw.equals(null)||psw.trim().equals("")){
                    et_password.setError("密码不能为空！");
                    return;
                }
                new Thread(new Runnable() {
                    String logsql = "USE [bill_data] SELECT *  FROM [bill_data].[dbo].[T_User] where [s_loginName] ='"+et_username.getText().toString()+"' and [s_password]='"+et_password.getText().toString()+"'";
                    @Override
                    public void run() {
                        int []rs =databaseHandle.LoginSQL(logsql);
                        int get =rs[0];
                        main_uid = rs[1];

                        if(get>0){
                            handler.sendEmptyMessage(get);
                        }else{
                            handler.sendEmptyMessage(0);
                        }
                    }
                }).start();
                break;
            }

            case R.id.btn_sign:
               //跳转注册
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                break;
            default:
                break;
        }

    }
}
