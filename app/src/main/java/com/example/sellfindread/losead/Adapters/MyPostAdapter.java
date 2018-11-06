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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sellfindread.losead.BuyAndSell.BuyAndSellComment;
import com.example.sellfindread.losead.BuyAndSell.BuyAndSellPost;
import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

class MyPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView myPostTitle,myPostPubDate,myPostDesc,myPostContact,myPostPrice,myPostSeller;
    public ImageView myPostImg;
    public Button myPostDelete;

    public MyPostViewHolder(@NonNull View itemView) {
        super(itemView);

        myPostTitle=(TextView)itemView.findViewById(R.id.myPostTitle);
        myPostPubDate=(TextView)itemView.findViewById(R.id.myPostPubDate);
        myPostDesc=(TextView)itemView.findViewById(R.id.myPostDesc);
        myPostContact=(TextView)itemView.findViewById(R.id.myPostContact);
        myPostPrice=(TextView)itemView.findViewById(R.id.myPostPrice);
        myPostSeller=(TextView)itemView.findViewById(R.id.myPostSeller);
        myPostImg=(ImageView)itemView.findViewById(R.id.myPostImg);
        myPostDelete=(Button) itemView.findViewById(R.id.myPostDelete);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

public class MyPostAdapter extends RecyclerView.Adapter<MyPostViewHolder> {

    private List<BuyAndSell> myPostList;
    private Context myPostContext;
    private LayoutInflater myPostInflater;
    private DatabaseReference myPostRef;
    public StorageReference myPostStorRef,delPhotRef;
    private String delKey, delImg,selKey;

    public MyPostAdapter(List<BuyAndSell> myPostList, Context myPostContext) {
        this.myPostList = myPostList;
        this.myPostContext = myPostContext;
        myPostInflater=LayoutInflater.from(myPostContext);
    }

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myPostItemView=myPostInflater.inflate(R.layout.my_post_card,viewGroup,false);
        return new MyPostViewHolder(myPostItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder myPostViewHolder, int i) {
        final BuyAndSell bnsItem=myPostList.get(i);

        myPostViewHolder.myPostTitle.setText(bnsItem.getTitle());
        myPostViewHolder.myPostPrice.setText(bnsItem.getPrice().toString());
        myPostViewHolder.myPostSeller.setText(bnsItem.getSeller());
        myPostViewHolder.myPostContact.setText(bnsItem.getContact().toString());
        myPostViewHolder.myPostPubDate.setText(bnsItem.getPubDate());
        myPostViewHolder.myPostDesc.setText(bnsItem.getDesc());

        String myImgUrl=bnsItem.getImage();

        Picasso.with(myPostContext).load(myImgUrl).into(myPostViewHolder.myPostImg);

        myPostViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selKey=bnsItem.getKey();
                SharedPreferences selectedItemSP=myPostContext.getSharedPreferences("Selected Key",Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor=selectedItemSP.edit();
                spEditor.putString("keyBNS",selKey);
                spEditor.commit();
                myPostContext.startActivity(new Intent(myPostContext,BuyAndSellComment.class));
            }
        });
        myPostRef=FirebaseDatabase.getInstance().getReference("Buy And Sell");
        myPostStorRef=FirebaseStorage.getInstance().getReference("BNS Product");

        myPostViewHolder.myPostDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delKey=bnsItem.getKey();
                myPostRef.child(delKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        delImg=dataSnapshot.child("image").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
//                delPhotRef=FirebaseStorage.getInstance().getReferenceFromUrl(delImg);
//                delPhotRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(myPostContext,"Successfully Deleted",Toast.LENGTH_LONG).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(myPostContext,"Failed...Please Try Again",Toast.LENGTH_LONG).show();
//                    }
//                });
                FirebaseDatabase.getInstance().getReference("Buy And Sell").child(delKey).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myPostList.size();
    }
}
