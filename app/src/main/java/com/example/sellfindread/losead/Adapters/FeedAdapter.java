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
import com.example.sellfindread.losead.R;

import java.util.List;

class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    public TextView txtTitle, txtPubDate, txtContent, txtUpvotes, txtDownvotes, txtPostedBy;
    public ImageButton upvoteButton,removeNews,downvoteButton;
    public ItemClickListener itemClickListener;

    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);

        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView) itemView.findViewById(R.id.txtPubDate);
        txtContent = (TextView) itemView.findViewById(R.id.txtContent);
//        txtUpvotes=(TextView)itemView.findViewById(R.id.upvoteNumber);
//        txtDownvotes=(TextView)itemView.findViewById(R.id.downvoteNumber);
        txtPostedBy=(TextView)itemView.findViewById(R.id.newsOwner);

//        upvoteButton=(ImageButton)itemView.findViewById(R.id.upvote);
        removeNews=(ImageButton)itemView.findViewById(R.id.removeNews);
//        downvoteButton=(ImageButton)itemView.findViewById(R.id.downvote);

//        if(AdminLogin.isAdmin){
//            removeNews.setVisibility(View.VISIBLE);
//        }

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

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private List<NewsFeed> newsList;
    private Context mContext;
    private LayoutInflater inflater;

    public FeedAdapter(Context mContext, List<NewsFeed> newsList) {
        this.mContext = mContext;
        this.newsList=newsList;
        inflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=inflater.inflate(R.layout.news_feed_card,viewGroup,false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  FeedViewHolder feedViewHolder, final int i) {
        Log.e("Inside BindView",newsList.get(i).getTitle());
        feedViewHolder.txtTitle.setText(newsList.get(i).getTitle());
        feedViewHolder.txtPubDate.setText(newsList.get(i).getPubDate());
        feedViewHolder.txtContent.setText(newsList.get(i).getDescription());
//        feedViewHolder.txtUpvotes.setText(newsList.get(i).getUpvotes().toString());
//        feedViewHolder.txtDownvotes.setText(newsList.get(i).getDownvotes().toString());
        feedViewHolder.txtPostedBy.setText(newsList.get(i).getPostedBy());

//        feedViewHolder.upvoteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long currUpvotes=newsList.get(i).getUpvotes();
//                long newUpvotes=currUpvotes+1;
////                MainActivity.dbReference.child(newsList.get(i).getItemKey()).child("upvotes").setValue(newUpvotes);
//            }
//        });

//        feedViewHolder.downvoteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long currDownvotes=newsList.get(i).getDownvotes();
//                long newDownvotes=currDownvotes+1;
////                MainActivity.dbReference.child(newsList.get(i).getItemKey()).child("downvotes").setValue(newDownvotes);
//            }
//        });

//        feedViewHolder.removeNews.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String removeId=newsList.get(i).getItemKey();
////                MainActivity.dbReference.child(removeId).removeValue();
//            }
//        });

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