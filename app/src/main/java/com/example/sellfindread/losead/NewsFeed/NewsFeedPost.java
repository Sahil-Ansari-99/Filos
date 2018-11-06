package com.example.sellfindread.losead.NewsFeed;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.sellfindread.losead.Adapters.NewsFeedPostAdapter;
import com.example.sellfindread.losead.Models.NewsFeed;
import com.example.sellfindread.losead.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFeedPost extends AppCompatActivity {

    private FirebaseAuth nfPostAuth;
    private FirebaseUser nfPostUser;
    private FirebaseDatabase nfPostDatabase;
    private DatabaseReference nfPostRef;
    private RecyclerView nfPostRecyclerView;
    private List<NewsFeed> nfPostList;
    private ProgressDialog nfPostProgress;
    private Toolbar nfPostToolbar;
    private NewsFeedPostAdapter nfPostAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_post);

        nfPostToolbar=(Toolbar)findViewById(R.id.nfPostToolbar);
        nfPostToolbar.setTitle("My Posts");
        setSupportActionBar(nfPostToolbar);

        nfPostAuth=FirebaseAuth.getInstance();
        nfPostUser=nfPostAuth.getCurrentUser();
        nfPostDatabase=FirebaseDatabase.getInstance();
        nfPostRef=nfPostDatabase.getReference("News Feed");

        nfPostRecyclerView=(RecyclerView)findViewById(R.id.nfPostRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        nfPostRecyclerView.setLayoutManager(linearLayoutManager);
        nfPostRecyclerView.setItemAnimator(new DefaultItemAnimator());
        nfPostRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));

        nfPostProgress=new ProgressDialog(this);
        nfPostProgress.setMessage("Loading...");
        nfPostProgress.show();

        nfPostList=new ArrayList<>();
        Log.e("Inside Posts","Hello");

        nfPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nfPostList.clear();

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    String key1 = postSnapShot.getKey();
                    HashMap<String, String> map = new HashMap<>();
                    Log.e("Test","Inside Ref");

                    if (postSnapShot.child("userid").getValue(String.class).equals(nfPostUser.getUid())) {
                        Log.e("Key Test", key1);

                        for (DataSnapshot ds : postSnapShot.getChildren()) {
                            if (!ds.getKey().equals("upvotes") && !ds.getKey().equals("downvotes")) {
                                map.put(ds.getKey(), ds.getValue(String.class));
                                Log.e("Test", ds.getValue().toString());
                            } else if (ds.getKey().equals("upvotes")) {
                                map.put(ds.getKey(), ds.getValue(Long.class).toString());
                            } else if (ds.getKey().equals("downvotes")) {
                                map.put(ds.getKey(), ds.getValue(Long.class).toString());
                            }
                        }
                        nfPostList.add(new NewsFeed(map.get("title"), map.get("pubDate"), map.get("description"), Long.parseLong(map.get("upvotes")), Long.parseLong(map.get("downvotes")), map.get("link"), postSnapShot.getKey(), map.get("postedBy"), map.get("userid")));
                        Log.e("Test",map.get("userid"));
                    }
                }

                nfPostProgress.dismiss();
                nfPostAdapter=new NewsFeedPostAdapter(getApplicationContext(),nfPostList);
                nfPostRecyclerView.setAdapter(nfPostAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}