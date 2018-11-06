package com.example.sellfindread.losead.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sellfindread.losead.Interface.ItemClickListener;
import com.example.sellfindread.losead.MainActivity;
import com.example.sellfindread.losead.Models.NewsFeed;
import com.example.sellfindread.losead.NewsFeed.NewsFeedPost;
import com.example.sellfindread.losead.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class NewsFeedPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    public TextView txtTitle, txtPubDate, txtContent, txtPostedBy;
    public ImageButton removeNews;
    public ItemClickListener itemClickListener;

    public NewsFeedPostViewHolder(@NonNull View itemView) {
        super(itemView);

        txtTitle = (TextView) itemView.findViewById(R.id.nfPostTitle);
        txtPubDate = (TextView) itemView.findViewById(R.id.nfPostPubDate);
        txtContent = (TextView) itemView.findViewById(R.id.nfPostContent);
        txtPostedBy=(TextView)itemView.findViewById(R.id.nfPostNewsOwner);

        removeNews=(ImageButton)itemView.findViewById(R.id.removeNews);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);

    }

    @Override
    public boolean onLongClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), true);
        return true;
    }
}

public class NewsFeedPostAdapter extends RecyclerView.Adapter<NewsFeedPostViewHolder> {

    private List<NewsFeed> newsList;
    private Context mContext;
    private LayoutInflater inflater;

    public NewsFeedPostAdapter(Context mContext, List<NewsFeed> newsList) {
        this.mContext = mContext;
        this.newsList=newsList;
        inflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public NewsFeedPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=inflater.inflate(R.layout.news_feed_post_card,viewGroup,false);
        return new NewsFeedPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  NewsFeedPostViewHolder feedViewHolder, final int i) {
        Log.e("Inside BindView",newsList.get(i).getTitle());
        feedViewHolder.txtTitle.setText(newsList.get(i).getTitle());
        feedViewHolder.txtPubDate.setText(newsList.get(i).getPubDate());
        feedViewHolder.txtContent.setText(newsList.get(i).getDescription());
        feedViewHolder.txtPostedBy.setText(newsList.get(i).getPostedBy());


        feedViewHolder.removeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String removeId=newsList.get(i).getItemKey();
                FirebaseDatabase.getInstance().getReference("News Feed").child(removeId).removeValue();
//                mContext.startActivity(new Intent(mContext,NewsFeedPost.class));
//
            }
        });

        feedViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    String extLink=newsList.get(position).getLink();
                    Log.e("Link",extLink);
                    if(!extLink.equals("")){
                        Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(extLink));
                        mContext.startActivity(browserIntent);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
