package com.example.dell.isafe;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Complaint extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
Spinner category;
String id[]=null;
String name[]=null;
SharedPreferences sp;
String uid;
String catid="";
EditText complaint;
Button send;
ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        category=(Spinner)findViewById(R.id.sp_category);
        complaint=(EditText)findViewById(R.id.edt_complaint);
        sp=getSharedPreferences("my_data",MODE_PRIVATE);
        uid=sp.getString("userid","");
        send=(Button)findViewById(R.id.btn_sendcomplaint);
        send.setOnClickListener(this);
     helpline c=new helpline();
     c.execute();
     category.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        catid=id[category.getSelectedItemPosition()];
       // Toast.makeText(this, "Clicked : "+catid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
      String des=complaint.getText().toString();
      sendComplaint sc=new sendComplaint();
      sc.execute(uid,catid,des);
    }

    private class helpline extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller w=new WebServiceCaller();
            w.setSoapObject("Helpline");
            w.callWebService();
            return w.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("false")) {
                Toast.makeText(Complaint.this, "Details Not Available", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    JSONArray ja = new JSONArray(s);
                    id= new String[ja.length()];
                    name=new String[ja.length()];
                    for(int i=0;i<ja.length();i++) {
                        JSONObject jo = ja.getJSONObject(i);

                         id[i] = jo.getString("catgid");
                         name[i] = jo.getString("catgname");

                    }
                    adapter=new ArrayAdapter<String>(Complaint.this,android.R.layout.simple_spinner_item,name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    category.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class sendComplaint extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller ws = new WebServiceCaller();
            ws.setSoapObject("sendcomplaint");
            ws.addProperty("uid",strings[0]);
            ws.addProperty("comtypeid",strings[1]);
            ws.addProperty("comdes",strings[2]);
            ws.callWebService();
            return ws.getResponse();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("success"))
            {
                Toast.makeText(Complaint.this, "Complaint send", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Log.d("Hello :",s);
                Toast.makeText(Complaint.this, "Cannot send your complaint"+s, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
