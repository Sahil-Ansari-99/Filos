package com.example.sellfindread.losead.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sellfindread.losead.Models.BuyAndSellCommentModel;
import com.example.sellfindread.losead.R;

import java.util.List;

class BuyAndSellCommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView bnsCommentText;

    public BuyAndSellCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        bnsCommentText=(TextView)itemView.findViewById(R.id.bnsCommentRow);

        bnsCommentText.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

public class BuyAndSellCommentAdapter extends RecyclerView.Adapter<BuyAndSellCommentViewHolder> {

    private List<BuyAndSellCommentModel> bnsCommentList;
    private Context bnsCommentContext;
    private LayoutInflater bnsCommentInflater;

    public BuyAndSellCommentAdapter(List<BuyAndSellCommentModel> bnsCommentList, Context bnsCommentContext) {
        this.bnsCommentList = bnsCommentList;
        this.bnsCommentContext = bnsCommentContext;
        bnsCommentInflater=LayoutInflater.from(bnsCommentContext);
    }

    @NonNull
    @Override
    public BuyAndSellCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View bnsCommentView=bnsCommentInflater.inflate(R.layout.buy_and_sell_comment_card,viewGroup,false);
        return new BuyAndSellCommentViewHolder(bnsCommentView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAndSellCommentViewHolder buyAndSellCommentViewHolder, int i) {
        final BuyAndSellCommentModel bnsCommentItem=bnsCommentList.get(i);

        buyAndSellCommentViewHolder.bnsCommentText.setText(bnsCommentItem.getComment());

    }

    @Override
    public int getItemCount() {
        return bnsCommentList.size();
    }
}
