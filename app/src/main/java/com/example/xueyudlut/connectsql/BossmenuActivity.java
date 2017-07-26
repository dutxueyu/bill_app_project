package com.example.xueyudlut.connectsql;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BossmenuActivity extends Activity implements View.OnClickListener{

    Button btn_goCheckbill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bossmenu);

        btn_goCheckbill = findViewById(R.id.btn_billchecked);
        btn_goCheckbill.setOnClickListener(this);
        findViewById(R.id.btn_usermanage).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_billchecked:
            {
                Intent it =new Intent(BossmenuActivity.this,SearchlogActivity.class);

            }
        }
    }
}
