package com.example.dell.isafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public  class AdapterHelpline extends RecyclerView.Adapter<AdapterHelpline.MyViewHolder>{
    Context context;
    List<BeanHelpline> blist;

    public AdapterHelpline(List<BeanHelpline> blist, Helpline helpline) {
        this.blist=blist;
        context =helpline;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlehelpline,parent,false);

        return new MyViewHolder(itemview);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BeanHelpline b = blist.get(position);
        holder.lic.setText(b.getLicence());
        holder.place.setText(b.getPlace());
        holder.catg.setText(b.getCategory());
        holder.email.setText(b.getEmail());
holder.phn.setText(b.getPhn());
    }

    @Override
    public int getItemCount() {
        return blist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView lic,catg,place,email,phn;
        Button send;
        SharedPreferences sp;
        String uid;
        public MyViewHolder(View itemView) {
            super(itemView);
            lic=itemView.findViewById(R.id.licence);
            catg=itemView.findViewById(R.id.category);
            place=itemView.findViewById(R.id.place);
            email=itemView.findViewById(R.id.email);
            phn=itemView.findViewById(R.id.phone);
            send=itemView.findViewById(R.id.btn_complaint);
            send.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int a= getAdapterPosition();
            BeanHelpline bh = blist.get(a);


                    String licno=bh.getLicence();
                    Intent i = new Intent(context,Complaint.class);
                    i.putExtra("licenseno",licno);
                    context.startActivity(i);

        }
    }
}
