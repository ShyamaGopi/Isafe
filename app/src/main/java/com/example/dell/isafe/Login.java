package com.example.dell.isafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity implements View.OnClickListener
{
EditText edt_uname,edt_pwd;
Button login,reg;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_uname=(EditText)findViewById(R.id.Username);
        edt_pwd=(EditText) findViewById(R.id.Password);
        login=(Button) findViewById(R.id.btn_login);
        reg=(Button)findViewById(R.id.btn_reg);
        reg.setOnClickListener(this);
        login.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == login) {


            String username = edt_uname.getText().toString();
            String password = edt_pwd.getText().toString();

            LoginCheck l = new LoginCheck();
            l.execute(username, password);
        }
      else if (view==reg){
            Intent i=new Intent(this,Registration.class);
            startActivity(i);
        }
    }

    private class LoginCheck extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            WebServiceCaller ws=new WebServiceCaller();
            ws.setSoapObject("login");
            ws.addProperty("username",strings[0]);
            ws.addProperty("password",strings[1]);

            ws.callWebService();
            return ws.getResponse();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("india",s);
            if(s.equals("false"))
            {
                Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONArray ja = new JSONArray(s);
                    JSONObject jo = ja.getJSONObject(0);
                    String id = jo.getString("userid");
                    String uname = jo.getString("username");
                    Toast.makeText(Login.this, "Welcome " + uname, Toast.LENGTH_SHORT).show();



                    SharedPreferences sp = getSharedPreferences("my_data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("userid", id);
                    editor.commit();
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }


        }
    }
}
