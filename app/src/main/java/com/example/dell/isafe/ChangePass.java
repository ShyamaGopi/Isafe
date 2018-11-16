package com.example.dell.isafe;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePass extends AppCompatActivity implements View.OnClickListener {
Button change;
String uid;
EditText old,newp,confirm;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        sp=getSharedPreferences("my_data",MODE_PRIVATE);
        uid=sp.getString("userid","");
        old=(EditText)findViewById(R.id.edt_oldpwd);
        newp=(EditText)findViewById(R.id.edt_newpwd);
        confirm=(EditText)findViewById(R.id.edt_confirmpwd);
        change=(Button)findViewById(R.id.btn_changepwd);
        change.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String edtold=old.getText().toString();
        String edtnew=newp.getText().toString();
        String cpass=confirm.getText().toString();
        if (edtnew.equals(cpass)) {
            changepass cp = new changepass();
            cp.execute(uid, edtold, edtnew);
        }
        else
        {
            Toast.makeText(this, "Password Missmatch", Toast.LENGTH_SHORT).show();
        }
    }


    private class changepass extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller ws = new WebServiceCaller();
            ws.setSoapObject("Password");
            ws.addProperty("uid",strings[0]);
            ws.addProperty("oldpwd",strings[1]);
            ws.addProperty("newpwd",strings[2]);
            ws.callWebService();
            return ws.getResponse();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("success")){
                Toast.makeText(ChangePass.this, "Successfully Changed "+s, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ChangePass.this, "Details not Updated"+s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
