package com.example.sellfindread.losead.BuyAndSell;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sellfindread.losead.Adapters.BuyAndSellCommentAdapter;
import com.example.sellfindread.losead.Models.BuyAndSellCommentModel;
import com.example.sellfindread.losead.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyAndSellComment extends AppCompatActivity {

    private RecyclerView bnsCommentRecyclerView;
    private FirebaseAuth bnsCommentAuth;
    private FirebaseUser bnsCommentUser;
    private FirebaseDatabase bnsCommentDatabase;
    private DatabaseReference bnsCommentRef,bnsUserRef;
    private BuyAndSellCommentAdapter bnsCommentAdapter;
    private List<BuyAndSellCommentModel> commentList;
    private FloatingActionButton bnsCommentSend;
    private EditText bnsCommentAdd;
    private ProgressDialog bnsCommentProgress;
    private TextView bnsCommentSeller;
    String bnsCommentInvestor,bnsCommentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_and_sell_comment);

        SharedPreferences sharedPreferences=getSharedPreferences("Selected Key",MODE_PRIVATE);
        bnsCommentKey=sharedPreferences.getString("keyBNS","null");

        bnsCommentAuth=FirebaseAuth.getInstance();
        bnsCommentUser=bnsCommentAuth.getCurrentUser();
        bnsCommentDatabase=FirebaseDatabase.getInstance();
        bnsCommentRef=bnsCommentDatabase.getReference().child("Buy And Sell").child(bnsCommentKey).child("comments");
        bnsUserRef=bnsCommentDatabase.getReference().child("Users");
        bnsCommentRef.keepSynced(true);

        bnsCommentAdd=(EditText)findViewById(R.id.bnsComment);
        bnsCommentSeller=(TextView)findViewById(R.id.bnsCommentSeller);
        bnsCommentSend=(FloatingActionButton)findViewById(R.id.bnsCommentSend);
        bnsCommentProgress=new ProgressDialog(this);

        bnsCommentRecyclerView=(RecyclerView)findViewById(R.id.bnsCommentRecyclerView);
        bnsCommentRecyclerView.setHasFixedSize(true);
        bnsCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList=new ArrayList<>();

        bnsCommentDatabase.getReference().child("Buy And Sell").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String seller=dataSnapshot.child(bnsCommentKey).child("seller").getValue(String.class);
                bnsCommentSeller.setText("Seller: "+seller);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bnsUserRef.child(bnsCommentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bnsCommentInvestor=dataSnapshot.child("Name").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bnsCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });

    }

    private void startPosting(){
        bnsCommentProgress.setMessage("Posting...");

        final String bnsCommentVal=bnsCommentAdd.getText().toString();
        final String seller=bnsCommentInvestor;

        if(!TextUtils.isEmpty(bnsCommentVal)){
            bnsCommentProgress.show();

            DatabaseReference newComment=bnsCommentRef.push();

            Map<String,String> dataToSave=new HashMap<>();
            dataToSave.put("comment",seller+": "+bnsCommentVal);
            dataToSave.put("userId",bnsCommentUser.getUid());

            newComment.setValue(dataToSave);
            bnsCommentProgress.dismiss();
            bnsCommentAdd.setText("");

//            startActivity(new Intent(getApplicationContext(),BuyAndSellComment.class));
        }

    }

    protected void onStart(){
        super.onStart();

        bnsCommentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BuyAndSellCommentModel bnsCommentItem=null;
                bnsCommentItem=dataSnapshot.getValue(BuyAndSellCommentModel.class);
                commentList.add(bnsCommentItem);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bnsCommentAdapter=new BuyAndSellCommentAdapter(commentList,getApplicationContext());
        bnsCommentRecyclerView.setAdapter(bnsCommentAdapter);
        bnsCommentRecyclerView.scrollToPosition(commentList.size()-1);
        bnsCommentAdapter.notifyDataSetChanged();

    }
}
