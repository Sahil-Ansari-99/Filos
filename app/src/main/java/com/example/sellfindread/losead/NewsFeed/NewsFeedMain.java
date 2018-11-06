package com.example.sellfindread.losead.NewsFeed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.sellfindread.losead.Adapters.FeedAdapter;
import com.example.sellfindread.losead.MainActivity;
import com.example.sellfindread.losead.Models.NewsFeed;
import com.example.sellfindread.losead.R;
import com.example.sellfindread.losead.ReportBug.ReportBugMain;
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

public class NewsFeedMain extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar newsToolbar;
    FirebaseDatabase fbDatabase;
    public static DatabaseReference dbReference;
    private FirebaseAuth nfAuth;
    private FirebaseUser nfUser;
    List<NewsFeed> newsList;
    FeedAdapter adapter;
    private Parcelable recyclerViewState;
    public ProgressDialog progressDialog;
    private FloatingActionButton nfButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_main);

        newsToolbar = (Toolbar) findViewById(R.id.newsToolbar);
        newsToolbar.setTitle("News Feed");
        setSupportActionBar(newsToolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        nfButton=(FloatingActionButton)findViewById(R.id.nfFloatingActionButton);

        newsList = new ArrayList<>();

        fbDatabase = FirebaseDatabase.getInstance();
        dbReference = fbDatabase.getReference("News Feed");
        nfAuth=FirebaseAuth.getInstance();
        nfUser=nfAuth.getCurrentUser();

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                newsList.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    String key1 = postSnapShot.getKey();
                    Log.e("Key Test", key1);
                    HashMap<String, String> map = new HashMap<>();

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

                    Log.e("Test 2", map.get("pubDate"));

                    newsList.add(new NewsFeed(map.get("title"), map.get("pubDate"), map.get("description"), Long.parseLong(map.get("upvotes")), Long.parseLong(map.get("downvotes")), map.get("link"), postSnapShot.getKey(), map.get("postedBy"), map.get("userid")));

                }

                progressDialog.dismiss();
                adapter=new FeedAdapter(getBaseContext(),newsList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        nfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddNews.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_feed_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.addNews){
            startActivity(new Intent(NewsFeedMain.this,AddNews.class));

        }

        else if(item.getItemId()==R.id.nfPosts){
            startActivity(new Intent(getApplicationContext(),NewsFeedPost.class));
        }

        else if(item.getItemId()==R.id.userLogout){
            nfAuth.signOut();
            startActivity(new Intent(NewsFeedMain.this,MainActivity.class));
        }

        else if(item.getItemId()==R.id.reportBug){
            startActivity(new Intent(getApplicationContext(),ReportBugMain.class));
        }

        return true;
    }

}
