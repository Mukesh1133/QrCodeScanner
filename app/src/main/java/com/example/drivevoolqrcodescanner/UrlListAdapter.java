package com.example.drivevoolqrcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UrlListAdapter extends RecyclerView.Adapter<UrlListAdapter.UrlListViewHolder> {

    private ArrayList<String> urlList;
    Context context;

    public UrlListAdapter(ArrayList<String> urlLists, Context context1) {
        this.urlList = urlLists;
        this.context = context1;
    }

    @NonNull
    @Override
    public UrlListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.url_item_list,parent,false);
        UrlListViewHolder urlListViewHolder = new UrlListViewHolder(view);
        return urlListViewHolder;
    }

    @Override
    public void onBindViewHolder( UrlListViewHolder holder, int position) {

        String url = urlList.get(position);
        
        String urlDomain = String.format("https://t3.gstatic.com/faviconV2?client=SOCIAL&type=FAVICON&fallback_opts=TYPE,SIZE,URL&url=%s&size=64",url);
        Glide.with(holder.imgUrlText.getContext()).load(urlDomain).into(holder.imgUrlText);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,WebViewActivity.class).putExtra("URL_PATH", urlList.get(position));
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class UrlListViewHolder extends RecyclerView.ViewHolder{
        ImageView imgUrlText;
        public UrlListViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUrlText = itemView.findViewById(R.id.imgUrList);
        }

    }

    private class LinkSpan extends URLSpan{
        private LinkSpan(String url){
            super(url);
        }
    }
}
