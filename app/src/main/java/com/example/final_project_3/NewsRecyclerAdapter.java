package com.example.final_project_3;

import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    List<Article> articleList;
    NewsRecyclerAdapter(List<Article> articleList){
            this.articleList= articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            Article article = articleList.get(position);
            holder.titleTextView.setText(article.getDescription());
            holder.sourceTextView.setText(article.getSource().getName());
            Picasso.get().load(article.getUrlToImage())
                    .error(R.drawable.not_available)
                    .placeholder(R.drawable.not_available)
                    .into(holder.imageView);
            holder.itemView.setOnClickListener((v-> {
                Intent i = new Intent(v.getContext(), NewsFullActivity.class);
                i.putExtra("url",article.getUrl());
                v.getContext().startActivity(i);
            }));
    }

    void updateData(List<Article> data){
        articleList.clear();
        articleList.addAll(data);
    }
    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class  NewsViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView, sourceTextView;
        ImageView imageView;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView=itemView.findViewById(R.id.article_title);
            sourceTextView=itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);


        }
    }

}
