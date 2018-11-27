package com.example.dell.isafe;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Helpline extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner place, catg;
    SharedPreferences sp;
    String uid;
    RecyclerView rv;
    List<BeanHelpline> blist;
    String catgid="",pid="";
    String id1[] = null;
    String name1[] = null;
    String id[] = null;
    String name[] = null;
    ArrayAdapter<String> adapter;
AdapterHelpline adp;
    Button search;

    String catid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpline);
        place = (Spinner) findViewById(R.id.sp_place);
        catg = (Spinner) findViewById(R.id.sp_catg);
        search = (Button) findViewById(R.id.btn_search);
        rv=(RecyclerView)findViewById(R.id.rv_helpline);
        blist=new ArrayList<>();

        sp = getSharedPreferences("my_data", MODE_PRIVATE);
        uid = sp.getString("userid", "");
        helpline c = new helpline();
        c.execute();
        place1 pl = new place1();
        pl.execute();


        catg.setOnItemSelectedListener(this);
        place.setOnItemSelectedListener(this);

        search.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view == catg) {
            catgid = id[catg.getSelectedItemPosition()];
        } else if (view == place) {
             pid = id1[i];

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (view == search) {
            viewhelp vh = new viewhelp();
            blist.clear();
String cid=id[catg.getSelectedItemPosition()];
String plid=id1[place.getSelectedItemPosition()];
vh.execute(cid,plid);
        }
    }

    private class helpline extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller w = new WebServiceCaller();
            w.setSoapObject("Helpline");
            w.callWebService();
            return w.getResponse();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("false")) {
                Toast.makeText(Helpline.this, "Details Not Available", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONArray ja = new JSONArray(s);
                    id = new String[ja.length()];
                    name = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        id[i] = jo.getString("catgid");
                        name[i] = jo.getString("catgname");


                    }

                    adapter = new ArrayAdapter<String>(Helpline.this, android.R.layout.simple_spinner_item, name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    catg.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class place1 extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller w = new WebServiceCaller();
            w.setSoapObject("ViewPlace");
            w.callWebService();
            return w.getResponse();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("false")) {
                Toast.makeText(Helpline.this, "Details Not Available", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONArray ja = new JSONArray(s);
                    id1 = new String[ja.length()];
                    name1 = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        id1[i] = jo.getString("placeid");
                        name1[i] = jo.getString("placename");

                    }

                    adapter = new ArrayAdapter<String>(Helpline.this, android.R.layout.simple_spinner_item, name1);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    place.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class viewhelp extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller w = new WebServiceCaller();
            w.setSoapObject("Viewhelpline");
            w.addProperty("catgid", strings[0]);
            w.addProperty("placeid", strings[1]);
            w.callWebService();
            return w.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("false")) {
                Toast.makeText(Helpline.this, "Details Not Available", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONArray ja = new JSONArray(s);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        String lic = jo.getString("license");
                        String pla=jo.getString("place");
                        String catg = jo.getString("category");
                        String mail = jo.getString("email");
                        String phn = jo.getString("phone");
                 //       Toast.makeText(Helpline.this, "License: " + lic + " category " + catg + "email ::" + mail + " phone :" + phn, Toast.LENGTH_SHORT).show();
                        BeanHelpline bb = new BeanHelpline();
                        bb.setLicence(lic);
                        bb.setPlace(pla);
                        bb.setCategory(catg);
                        bb.setEmail(mail);
                        bb.setPhn(phn);
                        blist.add(bb);
                    }

                    adp=new AdapterHelpline(blist,Helpline.this);
                    rv.setLayoutManager(new LinearLayoutManager(Helpline.this));
                    rv.setAdapter(adp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
