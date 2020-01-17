package com.bunglon2.bunglonpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GetStartedAct extends AppCompatActivity {

    Button btn_Signin, btn_new_account_create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        btn_Signin = findViewById(R.id.btn_Signin);
        btn_new_account_create = findViewById(R.id.btn_new_account_create);

        btn_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosignin = new Intent(GetStartedAct.this, SignInAct.class);
                startActivity(gotosignin);
                finish();
            }
        });

        btn_new_account_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(GetStartedAct.this, RegisterOneAct.class);
                startActivity(gotoregisterone);
                finish();
            }
        });



    }
}
