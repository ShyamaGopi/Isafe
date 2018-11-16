package com.example.dell.isafe;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewContacts extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<BeanContact> beanList;
    AdapterContact adpt;
    SharedPreferences sp;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        rv=(RecyclerView)findViewById(R.id.rv_contact);
        beanList= new ArrayList<>();

        displayContacts();


    }



    public void displayContacts() {
        BeanContact bc=null;
        //Toast.makeText(this, "Syama", Toast.LENGTH_SHORT).show();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //    Toast.makeText(this, "Name: " + name + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();
                        Log.d("alliswell",name+"\n"+phoneNo);
                        bc=new BeanContact();
                        bc.setName(name);
                        bc.setPhnno(phoneNo);

                        beanList.add(bc);

                    }
                    pCur.close();
                    sp=getSharedPreferences("my_data",MODE_PRIVATE);
                    uid=sp.getString("userid","");

                    adpt= new AdapterContact(ViewContacts.this, beanList,uid);
                    rv.setLayoutManager(new LinearLayoutManager(ViewContacts.this));
                    rv.setAdapter(adpt);

                }
            }
        }
    }

}
