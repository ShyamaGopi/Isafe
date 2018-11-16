package com.example.dell.isafe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.MyViewHolder> {
List<BeanContact> beanList;
Context context;
String uid="";

    public AdapterContact(ViewContacts viewContacts, ArrayList<BeanContact> beanList, String uid) {
        this.beanList=beanList;
        context=viewContacts;
        this.uid= uid;
    }

    @Override
    public AdapterContact.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecontact,parent,false);

        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(AdapterContact.MyViewHolder holder, int position) {
        BeanContact bc= beanList.get(position);
        holder.name.setText(bc.getName());
        holder.phnno.setText(bc.getPhnno());
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,phnno;
        Button addcontact;

        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvName);
            phnno=itemView.findViewById(R.id.tvNumber);
            addcontact=itemView.findViewById(R.id.btn_addcontact);
            addcontact.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Toast.makeText(context, "Clicked :"+pos, Toast.LENGTH_SHORT).show();
            addemergency a=new addemergency();

            BeanContact bbc= beanList.get(pos);

            String contactname = bbc.getName();
            String contactno=bbc.getPhnno();

            a.execute(uid,contactname,contactno);
        }
    }


    private class addemergency extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            WebServiceCaller w=new WebServiceCaller();
            w.setSoapObject("addContacts");
            w.addProperty("uid",strings[0]);
            w.addProperty("contactname",strings[1]);
            w.addProperty("contactno",strings[2]);
            w.callWebService();
            return w.getResponse();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("success"))
            {
                Toast.makeText(context, "Successfully Added Contact", Toast.LENGTH_SHORT).show();

            }
            else
            {
                Log.d("Hello :",s);
                Toast.makeText(context, "Not Added Number "+s, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
