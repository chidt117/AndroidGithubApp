package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.MyViewHolder> {

    private List<SearchBean.ItemsBean> mItemsList;
    private Context context;

    public SearchUserAdapter(Context context, List<SearchBean.ItemsBean> itemsList){
        this.context = context;
        mItemsList = itemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user,parent,false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SearchBean.ItemsBean items = mItemsList.get(position);
        Glide.with(context).load(items.getAvatar_url()).into(holder.Headface);
        holder.tvLogin.setText("   "+items.getLogin());
        holder.repository.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RepositoryActivity.class);
                intent.putExtra("user",items.getLogin());
                context.startActivity(intent);
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Details",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,DetailsActivity.class);
                intent.putExtra("user",items.getLogin());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Button details;
        Button repository;
        ImageView Headface;
        TextView  tvLogin;
        View itemsView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemsView = itemView;
            details = itemView.findViewById(R.id.button);
            Headface = itemView.findViewById(R.id.ivAvatar);
            tvLogin  = itemView.findViewById(R.id.tvLogin);
            repository = itemView.findViewById(R.id.repository);
        }
    }
}
