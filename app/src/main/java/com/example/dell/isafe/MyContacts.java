package com.example.dell.isafe;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyContacts extends AppCompatActivity {
    RecyclerView rv;
    TextView contact;
    SharedPreferences sp;
    String id;
    AdapterEmgcontact adp;
List<BeanEmgcontact> blist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);
        rv=(RecyclerView)findViewById(R.id.rv_emgcontact);
      //  contact = (TextView) findViewById(R.id.contacts);
        sp = getSharedPreferences("my_data", MODE_PRIVATE);
        id = sp.getString("userid", "");
        contacts c = new contacts();
        c.execute(id);
   blist=new ArrayList<>();

    }

    private class contacts extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller ws = new WebServiceCaller();
            ws.setSoapObject("viewcontacts");
            ws.addProperty("id", strings[0]);

            ws.callWebService();
            return ws.getResponse();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("false")) {
                Toast.makeText(MyContacts.this, "Details Not Available", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONArray ja = new JSONArray(s);

                    for(int i=0;i<ja.length();i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        String name = jo.getString("emgname");
                        String phn = jo.getString("emgcontact");
                        String prio=jo.getString("prior");
                     //   Toast.makeText(MyContacts.this, "contacts: " + name + " " + phn+"priority :"+prio, Toast.LENGTH_SHORT).show();
                        BeanEmgcontact bb = new BeanEmgcontact();
                        bb.setName(name);
                        bb.setPhnno(phn);
                        bb.setPrio(prio);
                        blist.add(bb);
                    }
                    adp=new AdapterEmgcontact(blist,MyContacts.this);
                    rv.setLayoutManager(new LinearLayoutManager(MyContacts.this));
                    rv.setAdapter(adp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

