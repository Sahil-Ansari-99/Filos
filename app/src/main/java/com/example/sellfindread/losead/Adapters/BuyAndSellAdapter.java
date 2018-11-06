package com.example.sellfindread.losead.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sellfindread.losead.BuyAndSell.BuyAndSellComment;
import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.Models.BuyAndSellCommentModel;
import com.example.sellfindread.losead.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class BuyAndSellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView bnsSeller,bnsContact,bnsTitle,bnsPrice,bnsDescription,bnsPubDate;
    public ImageView bnsImage;

    public BuyAndSellViewHolder(@NonNull View bnsItemView) {
        super(bnsItemView);

        bnsTitle=(TextView)bnsItemView.findViewById(R.id.bnsTitle);
        bnsContact=(TextView)bnsItemView.findViewById(R.id.bnsContact);
        bnsPrice=(TextView)bnsItemView.findViewById(R.id.bnsPrice);
        bnsDescription=(TextView)bnsItemView.findViewById(R.id.bnsDescription);
        bnsPubDate=(TextView)bnsItemView.findViewById(R.id.bnsPubDate);
        bnsSeller=(TextView)bnsItemView.findViewById(R.id.bnsSeller);
        bnsImage=(ImageView)bnsItemView.findViewById(R.id.bnsProduct);

        bnsItemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

public class BuyAndSellAdapter extends RecyclerView.Adapter<BuyAndSellViewHolder>{

    private List<BuyAndSell> itemList;
    private Context bnsContext;
    private LayoutInflater bnsInflater;
    String selectedKey;

    public BuyAndSellAdapter(List<BuyAndSell> itemList, Context bnsContext) {
        this.itemList = itemList;
        this.bnsContext = bnsContext;
        bnsInflater=LayoutInflater.from(bnsContext);
    }

    @NonNull
    @Override
    public BuyAndSellViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View bnsItemView=bnsInflater.inflate(R.layout.buy_and_sell_card,viewGroup,false);
        return new BuyAndSellViewHolder(bnsItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyAndSellViewHolder buyAndSellViewHolder, int i) {
        final BuyAndSell bnsItem=itemList.get(i);

        String imgUrl=bnsItem.getImage();

        buyAndSellViewHolder.bnsTitle.setText(bnsItem.getTitle());
        buyAndSellViewHolder.bnsSeller.setText("Seller: "+bnsItem.getSeller());
        buyAndSellViewHolder.bnsPubDate.setText(bnsItem.getPubDate());
        buyAndSellViewHolder.bnsPrice.setText(bnsItem.getPrice());
        buyAndSellViewHolder.bnsDescription.setText(bnsItem.getDesc());
        buyAndSellViewHolder.bnsContact.setText(bnsItem.getContact());

        Log.e("Test",bnsItem.getPrice());

        Picasso.with(bnsContext).load(imgUrl).into(buyAndSellViewHolder.bnsImage);

        buyAndSellViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedKey=bnsItem.getKey();
                SharedPreferences selectedItemSP=bnsContext.getSharedPreferences("Selected Key",Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor=selectedItemSP.edit();
                spEditor.putString("keyBNS",selectedKey);
                spEditor.commit();
                bnsContext.startActivity(new Intent(bnsContext,BuyAndSellComment.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
