package com.example.dell.isafe;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Emergency extends AppCompatActivity implements LocationListener, View.OnClickListener {
    LocationManager locationManager;
    String lattitude;
    String longitude;
    Button stop;
    String loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        getLocation();
        SharedPreferences sp = getSharedPreferences("my_data", MODE_PRIVATE);
        String id = sp.getString("userid", "");
        stop=(Button)findViewById(R.id.btn_stop);
        stop.setOnClickListener(this);
    }
    public void getLocation() {

        try {
            Log.d("alliswell", "Inside getLocation");
            locationManager= (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        lattitude = location.getLatitude() + "";
        longitude = location.getLongitude() + "";

        try {
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//  locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
            //addresses.get(0).getAddressLine(1);
//  Toast.makeText(this, addresses.get(0).getAddressLine(0) + ", " +
            // addresses.get(0).getAddressLine(1), Toast.LENGTH_SHORT).show();
            //  loc=locationText.getText().toString();
            loc = "Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude() + "\n" + addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1);
            Toast.makeText(this, "Location" + loc, Toast.LENGTH_SHORT).show();
            Log.d("alliswell", "hello");
            SharedPreferences sp = getSharedPreferences("my_data", MODE_PRIVATE);
            String id = sp.getString("userid", "");

            String myloc = addresses.get(0).getAddressLine(0) + "";


            contacts c = new contacts();
            c.execute(id);

            //  String myloc= addresses.get(0).getAddressLine(0)+"";

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        locationManager.removeUpdates((LocationListener) locationManager);
        locationManager=null;
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

                    Toast.makeText(Emergency.this, "Details Not Available", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray ja = new JSONArray(s);
                        int count = 0;
                        for(int i=0;i<ja.length();i++) {
                            JSONObject jo = ja.getJSONObject(i);

                            String name = jo.getString("emgname");
                            String phn = jo.getString("emgcontact");
                            String prio=jo.getString("prior");
                            if(prio.equals("1")){
                                sendSMS(phn,loc);
                                count++;
                            }
                            else if(prio.equals("2")){
                                sendSMS(phn,loc);
                                count++;
                            }

                            //Toast.makeText(MyContacts.this, "contacts: " + name + " " + phn+"priority :"+prio, Toast.LENGTH_SHORT).show();

                        }
                        if(count==0) {
                            for (int i = 0; i < 2; i++) {
                                JSONObject jo = ja.getJSONObject(i);

                                String name = jo.getString("emgname");
                                String phn = jo.getString("emgcontact");
                                String prio = jo.getString("prior");
                                sendSMS(phn,loc);
                                count++;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    public void  sendSMS(String no, String msg){
        msg="Help... My Location:"+msg;
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        no = no.trim();
        //Get the SmsManager instance and call the sendTextMessage method to send message
//        SmsManager sms = getSmsManagerForSubscriptionId(getDefaultSmsSubscriptionId());
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(no, null, msg, null,pi);

//        Boolean b = SimUtil.sendSMS(this,1,no,null,msg,pi,null);
//        if(b) {
           /* Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                    Toast.LENGTH_LONG).show();*/

    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


}
