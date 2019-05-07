package com.example.ebookstore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.ViewHolder> {


    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();


    public void update(String nanme , String url){
        items.add(nanme);
        urls.add(url);
        notifyDataSetChanged();

    }


    public MyAdapter(RecyclerView recyclerView , Context context , ArrayList<String> items , ArrayList<String> urls){
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls = urls;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameOfFile.setText(items.get(position));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfFile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfFile = itemView.findViewById(R.id.nameOfFile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = recyclerView.getChildLayoutPosition(view);
                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls.get(position)));
                    context.startActivity(intent);
                }
            });

        }
    }


}
