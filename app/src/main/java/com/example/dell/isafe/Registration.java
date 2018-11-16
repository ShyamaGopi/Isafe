package com.example.dell.isafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Registration extends AppCompatActivity implements View.OnClickListener {
EditText name,dob,house,adhar,phnno,email;
Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        name=(EditText)findViewById(R.id.edt_name);
        dob=(EditText)findViewById(R.id.edt_dob);
        house=(EditText)findViewById(R.id.edt_house);
        adhar=(EditText)findViewById(R.id.edt_adhar);
        phnno=(EditText)findViewById(R.id.edt_phone);
        email=(EditText)findViewById(R.id.edt_email);
        register=(Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
