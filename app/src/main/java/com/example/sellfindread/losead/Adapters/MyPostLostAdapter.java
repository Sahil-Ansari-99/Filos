package com.example.sellfindread.losead.Adapters;

import android.content.Context;
import android.content.Intent;
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

import com.example.sellfindread.losead.BuyAndSell.BuyAndSellPost;
import com.example.sellfindread.losead.LostAndFound.LostAndFoundPost;
import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.Models.LostAndFound;
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

class MyPostLostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView myPostTitle,myPostPubDate,myPostDesc,myPostContact,myPostPrice,myPostSeller;
    public ImageView myPostImg;
    public Button myPostDelete;

    public MyPostLostViewHolder(@NonNull View itemView) {
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

public class MyPostLostAdapter extends RecyclerView.Adapter<MyPostLostViewHolder> {

    private List<LostAndFound> myPostList;
    private Context myPostContext;
    private LayoutInflater myPostInflater;
    private DatabaseReference myPostRef;
    public StorageReference myPostStorRef,delPhotRef;
    private String delKey, delImg;

    public MyPostLostAdapter(List<LostAndFound> myPostList, Context myPostContext) {
        this.myPostList = myPostList;
        this.myPostContext = myPostContext;
        myPostInflater=LayoutInflater.from(myPostContext);
    }

    @NonNull
    @Override
    public MyPostLostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myPostItemView=myPostInflater.inflate(R.layout.my_post_card,viewGroup,false);
        return new MyPostLostViewHolder(myPostItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostLostViewHolder myPostViewHolder, int i) {
        final LostAndFound bnsItem=myPostList.get(i);

        myPostViewHolder.myPostTitle.setText(bnsItem.getTitle());
        myPostViewHolder.myPostPrice.setText(bnsItem.getPrice());
        myPostViewHolder.myPostSeller.setText(bnsItem.getSeller());
        myPostViewHolder.myPostContact.setText(bnsItem.getContact());
        myPostViewHolder.myPostPubDate.setText(bnsItem.getPubDate());
        myPostViewHolder.myPostDesc.setText(bnsItem.getDesc());

        String myImgUrl=bnsItem.getImage();

        Picasso.with(myPostContext).load(myImgUrl).into(myPostViewHolder.myPostImg);

        myPostViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        myPostRef=FirebaseDatabase.getInstance().getReference("Lost And Found");
        myPostStorRef=FirebaseStorage.getInstance().getReference("LNF Product");

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
                FirebaseDatabase.getInstance().getReference("Lost And Found").child(delKey).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myPostList.size();
    }
}
