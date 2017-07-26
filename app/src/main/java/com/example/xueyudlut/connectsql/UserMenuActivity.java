package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserMenuActivity extends Activity implements View.OnClickListener{

    Button btn_2upload;
    Button btn_2billsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        findViewById(R.id.btn_billchecked).setOnClickListener(this);
        findViewById(R.id.btn_2upload).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_2upload:
                startActivity(new Intent(UserMenuActivity.this,UserActivity.class));
                break;
            case R.id.btn_billchecked:
                startActivity(new Intent(UserMenuActivity.this,SearchlogActivity.class));
                break;
            default:
                break;
        }
      //  Toast.makeText(getApplicationContext(),"dddd",Toast.LENGTH_LONG).show();
    }
}
