package com.example.sellfindread.losead.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sellfindread.losead.BuyAndSell.BuyAndSellComment;
import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.Models.LostAndFound;
import com.example.sellfindread.losead.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class LostAndFoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView lnfSeller,lnfContact,lnfTitle,lnfPrice,lnfDescription,lnfPubDate;
    public ImageView lnfImage;

    public LostAndFoundViewHolder(@NonNull View bnsItemView) {
        super(bnsItemView);

        lnfTitle=(TextView)bnsItemView.findViewById(R.id.lnfTitle);
        lnfContact=(TextView)bnsItemView.findViewById(R.id.lnfContact);
        lnfPrice=(TextView)bnsItemView.findViewById(R.id.lnfPrice);
        lnfDescription=(TextView)bnsItemView.findViewById(R.id.lnfDescription);
        lnfPubDate=(TextView)bnsItemView.findViewById(R.id.lnfPubDate);
        lnfSeller=(TextView)bnsItemView.findViewById(R.id.lnfSeller);
        lnfImage=(ImageView)bnsItemView.findViewById(R.id.lnfProduct);

        bnsItemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundViewHolder>{

    private List<LostAndFound> itemList;
    private Context lnfContext;
    private LayoutInflater lnfInflater;
    String selectedKey;

    public LostAndFoundAdapter(List<LostAndFound> itemList, Context bnsContext) {
        this.itemList = itemList;
        this.lnfContext = bnsContext;
        lnfInflater=LayoutInflater.from(bnsContext);
    }

    @NonNull
    @Override
    public LostAndFoundViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View bnsItemView=lnfInflater.inflate(R.layout.lost_and_found_card,viewGroup,false);
        return new LostAndFoundViewHolder(bnsItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LostAndFoundViewHolder buyAndSellViewHolder, int i) {
        final LostAndFound lnfItem=itemList.get(i);

        String imgUrl=lnfItem.getImage();

        buyAndSellViewHolder.lnfTitle.setText(lnfItem.getTitle());
        buyAndSellViewHolder.lnfSeller.setText("Posted By: "+lnfItem.getSeller());
        buyAndSellViewHolder.lnfPubDate.setText(lnfItem.getPubDate());
        buyAndSellViewHolder.lnfPrice.setText(lnfItem.getPrice());
        buyAndSellViewHolder.lnfDescription.setText(lnfItem.getDesc());
        buyAndSellViewHolder.lnfContact.setText(lnfItem.getContact().toString());

        Picasso.with(lnfContext).load(imgUrl).into(buyAndSellViewHolder.lnfImage);

        buyAndSellViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedKey=lnfItem.getKey();
                SharedPreferences selectedItemSP=lnfContext.getSharedPreferences("Selected Key",Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor=selectedItemSP.edit();
                spEditor.putString("keyLNF",selectedKey);
                spEditor.commit();
//                lnfContext.startActivity(new Intent(lnfContext,LostAndFoundComment.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

