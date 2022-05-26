package com.example.ushopcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ushopqrcodescanner.R;

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
    public void onBindViewHolder(UrlListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String url = urlList.get(position);
        String domainName = getUrlDomainName(url);

        char first = domainName.charAt(0);
        Log.d("Domain", "Domain" +first);
//        holder.txtDomain.setText(first);
        holder.txtDomain.setText(first+"");

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
        TextView txtDomain;
        public UrlListViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDomain = itemView.findViewById(R.id.txtDomain);
        }

    }


    public String getUrlDomainName(String url) {
        String domainName = new String(url);

        int index = domainName.indexOf("://");

        if (index != -1) {
            // keep everything after the "://"
            domainName = domainName.substring(index + 3);
        }

        index = domainName.indexOf('/');

        if (index != -1) {
            // keep everything before the '/'
            domainName = domainName.substring(0, index);
        }

        // check for and remove a preceding 'www'
        // followed by any sequence of characters (non-greedy)
        // followed by a '.'
        // from the beginning of the string
        domainName = domainName.replaceFirst("^www.*?\\.", "");

        return domainName;
    }
}
