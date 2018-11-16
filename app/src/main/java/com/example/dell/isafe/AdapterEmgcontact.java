package com.example.dell.isafe;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdapterEmgcontact extends RecyclerView.Adapter<AdapterEmgcontact.MyViewHolder>{
Context context;
String prioArray[]=null;
    List<BeanEmgcontact>blist;
    public AdapterEmgcontact(List<BeanEmgcontact> blist, MyContacts myContacts) {
    this.blist=blist;
    context=myContacts;
    prioArray= new String[blist.size()];
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.singleemgcontact,parent,false);

        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            BeanEmgcontact b = blist.get(position);
            holder.name.setText(b.getName());
            holder.no.setText(b.getPhnno());
    }

    @Override
    public int getItemCount() {
        return blist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {
        TextView name,no;
        SharedPreferences sp;
        String uid;
        Button update;
        EditText priority;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvcname);
            no=itemView.findViewById(R.id.tvcno);
            priority=itemView.findViewById(R.id.edt_priority);
            update=itemView.findViewById(R.id.btnupdate);
            update.setOnClickListener(this);
            priority.addTextChangedListener(this);
        }

        @Override
        public void onClick(View view) {

            int a= getAdapterPosition();
            BeanEmgcontact bb = blist.get(a);

            String cname=bb.getName();
            String cno=bb.getPhnno();
            String prio=priority.getText().toString();
            Toast.makeText(context, "Successfully set priority as :"+prio, Toast.LENGTH_SHORT).show();
            sp=context.getSharedPreferences("my_data",MODE_PRIVATE);

            uid=sp.getString("userid","");
            Log.d("alliswell",cname+"\n"+cno+"\n"+prio+"\n"+uid+"\n PRIOARRAY:"+prioArray[a]);
            setpriority p= new setpriority();
            p.execute(uid,prio,cname,cno);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            int aa= getAdapterPosition();
            prioArray[aa]= priority.getText().toString();
            //Toast.makeText(context, "CurrentPrio:"+prioArray[aa], Toast.LENGTH_SHORT).show();


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        private class setpriority extends AsyncTask<String,String,String> {
            @Override
            protected String doInBackground(String... strings) {
                WebServiceCaller ws = new WebServiceCaller();
                ws.setSoapObject("Setpriority");
                ws.addProperty("uid",strings[0]);
                ws.addProperty("priority",strings[1]);
                ws.addProperty("cname",strings[2]);
                ws.addProperty("cphn",strings[3]);
                ws.callWebService();
                return ws.getResponse();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("success"))
                {
                    Log.d("Hi : ",s);
                    Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("Hello :",s);Toast.makeText(context, "Priority Not Updated! Please enter a  Valid priority"+s, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
