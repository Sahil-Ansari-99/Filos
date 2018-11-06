package com.example.sellfindread.losead.LostAndFound;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.sellfindread.losead.Adapters.MyPostAdapter;
import com.example.sellfindread.losead.Adapters.MyPostLostAdapter;
import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.Models.LostAndFound;
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

public class LostAndFoundPost extends AppCompatActivity {

    private FirebaseDatabase bnsPostDatabase;
    private DatabaseReference bnsPostRef;
    private FirebaseAuth bnsPostAuth;
    private FirebaseUser bnsPostUser;
    private MyPostLostAdapter myPostAdapter;
    private List<LostAndFound> bnsPostList;
    private RecyclerView bnsPostRecyclerView;
    private Parcelable bnsPostState;
    private Toolbar bnsPostToolbar;
    private ProgressDialog myPostProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_post_main);

        bnsPostToolbar = (Toolbar) findViewById(R.id.myPostToolbar);
        bnsPostToolbar.setTitle("My Posts");
        setSupportActionBar(bnsPostToolbar);

        bnsPostAuth = FirebaseAuth.getInstance();
        bnsPostUser = bnsPostAuth.getCurrentUser();
        bnsPostDatabase = FirebaseDatabase.getInstance();
        bnsPostRef = bnsPostDatabase.getReference("Lost And Found");

        bnsPostRecyclerView = (RecyclerView) findViewById(R.id.myPostRecyclerView);
        LinearLayoutManager bnsPostLM = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, true);
        bnsPostLM.setStackFromEnd(true);
        bnsPostRecyclerView.setLayoutManager(bnsPostLM);
        bnsPostRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bnsPostRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        myPostProgress = new ProgressDialog(this);
        myPostProgress.setMessage("Loading...");
        myPostProgress.show();

        bnsPostList = new ArrayList<>();

//        bnsPostRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                BuyAndSell myPostListItem=null;
//                Log.e("My Post","Test");
//
//                if(dataSnapshot.child("userid").equals(bnsPostUser.getUid())){
//                    myPostListItem=dataSnapshot.getValue(BuyAndSell.class);
//                    bnsPostList.add(myPostListItem);
//                }
//
//                myPostAdapter=new MyPostAdapter(bnsPostList,getApplicationContext());
//                bnsPostRecyclerView.setAdapter(myPostAdapter);
//                myPostAdapter.notifyDataSetChanged();
//
//                myPostProgress.dismiss();
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        bnsPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bnsPostState = bnsPostRecyclerView.getLayoutManager().onSaveInstanceState();
                bnsPostRecyclerView.getLayoutManager().onRestoreInstanceState(bnsPostState);
                bnsPostList.clear();

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    HashMap<String, String> bnsMap = new HashMap<>();

                    if (postSnapShot.child("userid").getValue(String.class).equals(bnsPostUser.getUid())) {
                        for (DataSnapshot ds : postSnapShot.getChildren()) {
                            if ((!ds.getKey().equals("comments"))) {
                                bnsMap.put(ds.getKey(), ds.getValue(String.class));
                            }
//                        else if(ds.getKey().equals("contact")){
//                            bnsMap.put(ds.getKey(),ds.getValue(Long.class).toString());
//                        }
                        }

                        bnsPostList.add(new LostAndFound(bnsMap.get("contact"), bnsMap.get("desc"), bnsMap.get("image"), postSnapShot.getKey(), bnsMap.get("price"), bnsMap.get("seller"), bnsMap.get("pubDate"), bnsMap.get("title"), bnsMap.get("userid")));
                        Log.e("test",bnsMap.get("userid"));
                    }
                }

                myPostProgress.dismiss();
                myPostAdapter=new MyPostLostAdapter(bnsPostList,getApplicationContext());
                bnsPostRecyclerView.setAdapter(myPostAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}