package com.example.project_test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder>{

    Context context;
    ArrayList<Information> list;
    private OnEventListener mOnEventListener; //our interface

    public InfoAdapter(Context context,ArrayList<Information> list,OnEventListener onEventListener) {
        this.context = context;
        this.list = list;
        this.mOnEventListener = onEventListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_viewname;
        TextView tv_viewdes;
        TextView tv_viewphn;
        TextView tv_viewemail;
        TextView tv_dist;
        TextView tv_loc;
        ImageView iv_viewimg;
        TextView tv_date;

        OnEventListener onEventListener;

        public ViewHolder(@NonNull View itemView,OnEventListener onEventListener) {
            super(itemView);
            tv_viewname = itemView.findViewById(R.id.tv_viewname);
            iv_viewimg = itemView.findViewById(R.id.iv_viewimg);
            tv_viewphn = itemView.findViewById(R.id.tv_viewphn);
            tv_viewemail = itemView.findViewById(R.id.tv_viewemail);
            tv_dist = itemView.findViewById(R.id.tv_dist);
            tv_loc = itemView.findViewById(R.id.tv_loc);
            tv_date = itemView.findViewById(R.id.tv_date);

            this.onEventListener = onEventListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEventListener.onEventClicked(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design,parent,false);
        return new ViewHolder(v,mOnEventListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        String[] smonth={"Jan","Feb","March","April","May","Jun","July","August","Sept","Oct","Nov","Dec"};
        String str_date = String.valueOf(list.get(position).getYear())+" "+smonth[list.get(position).getMonth()]+" "+String.valueOf(list.get(position).getDay());
        holder.itemView.setTag(list.get(position));
        holder.tv_viewname.setText(list.get(position).getName());
        //holder.iv_viewimg.setImageURI(Uri.parse(list.get(position).getImage()));
        Picasso.with(context).load(list.get(position).getImage()).fit().centerCrop().into(holder.iv_viewimg);
        holder.tv_date.setText(str_date);
        holder.tv_viewphn.setText("Contact no. : "+list.get(position).getPhn());
        holder.tv_viewemail.setText("email : "+list.get(position).getEmail());
        holder.tv_dist.setText("distance : "+String.valueOf(list.get(position).getDist())+" km");
        holder.tv_loc.setText("location : "+list.get(position).getAddressline());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnEventListener{
        void onEventClicked(int position);
    }


}
