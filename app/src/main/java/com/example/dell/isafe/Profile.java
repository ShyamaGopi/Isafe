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

public class Profile extends AppCompatActivity implements View.OnClickListener {
Button btn_ok;
EditText edtName,edtDob,edtHouse,edtPlace,edtDistrict,edtState,edtPin,edtPhone,edtEmail,edtAdhar;
    SharedPreferences sp;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        edtAdhar=(EditText) findViewById(R.id.edt_adhar);
        edtName=(EditText)findViewById(R.id.edt_name);
        edtDob=(EditText)findViewById(R.id.edt_dob);
        edtHouse=(EditText)findViewById(R.id.edt_house);
        edtPlace=(EditText)findViewById(R.id.edt_place);
        edtDistrict=(EditText)findViewById(R.id.edt_district);
        edtPin=(EditText)findViewById(R.id.edt_pin);
        edtState=(EditText)findViewById(R.id.edt_state);
        edtPhone=(EditText)findViewById(R.id.edt_phone);
        edtEmail=(EditText)findViewById(R.id.edt_email);
        btn_ok=(Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        sp=getSharedPreferences("my_data",MODE_PRIVATE);
        uid=sp.getString("userid","");
        profile p=new profile();
        p.execute(uid);
    }

    @Override
    public void onClick(View view)
    {
        String username=edtName.getText().toString();
        String userdob=edtDob.getText().toString();
        String userhouse=edtHouse.getText().toString();
        String userphone=edtPhone.getText().toString();
        String userpin=edtPin.getText().toString();
        String useremail=edtEmail.getText().toString();
        String useradhar=edtAdhar.getText().toString();
        Editprofile ep=new Editprofile();
        ep.execute(uid,username,userdob,userhouse,userpin,userphone,useremail,useradhar);
    }

    private class profile extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            WebServiceCaller w=new WebServiceCaller();
            w.setSoapObject("Profile");
            w.addProperty("userid",strings[0]);

            w.callWebService();
            return w.getResponse();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            Log.d("alliswell",s);
            if(s.equals("false"))
            {
                Toast.makeText(Profile.this, "Details Not Available", Toast.LENGTH_SHORT).show();
            }
            else
                {
                try
                {
                    JSONArray ja=new JSONArray(s);
                    JSONObject jo=ja.getJSONObject(0);
                    String name=jo.getString("uname");
                    String dob=jo.getString("udob");
                    String house=jo.getString("uhouse");
                    String place=jo.getString("uplace");
                    String dist=jo.getString("udist");
                    String state=jo.getString("ustate");
                    String pin=jo.getString("upin");
                    String adhar=jo.getString("uadhar");
                    String phone=jo.getString("uphone");
                    String email=jo.getString("uemail");
                    edtAdhar.setText(adhar);
                    edtName.setText(name);
                    edtDob.setText(dob);
                    edtHouse.setText(house);
                    edtPlace.setText(place);
                    edtDistrict.setText(dist);
                    edtPin.setText(pin);
                    edtState.setText(state);
                    edtPhone.setText(phone);
                    edtEmail.setText(email);



                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    private class Editprofile extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            WebServiceCaller ws = new WebServiceCaller();
            ws.setSoapObject("Editprofile");
            ws.addProperty("uid",strings[0]);
            ws.addProperty("uname",strings[1]);
            ws.addProperty("udob",strings[2]);
            ws.addProperty("uhouse",strings[3]);
            ws.addProperty("upin",strings[4]);
            ws.addProperty("uphone",strings[5]);
            ws.addProperty("uemail",strings[6]);
            ws.addProperty("uadhar",strings[7]);
            ws.callWebService();
            return ws.getResponse();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            if(s.equals("success"))
            {
                Toast.makeText(Profile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
            }
            else
                {
                    Log.d("Hello :",s);
                 Toast.makeText(Profile.this, "Details Not Updated! Please enter Valid Details", Toast.LENGTH_SHORT).show();
                }


        }
    }
}
