package com.example.sellfindread.losead.LostAndFound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
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

import com.example.sellfindread.losead.Adapters.BuyAndSellAdapter;
import com.example.sellfindread.losead.Adapters.LostAndFoundAdapter;
import com.example.sellfindread.losead.BuyAndSell.BuyAndSellAdd;
import com.example.sellfindread.losead.BuyAndSell.BuyAndSellPost;
import com.example.sellfindread.losead.MainActivity;
import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.Models.LostAndFound;
import com.example.sellfindread.losead.NewsFeed.AddNews;
import com.example.sellfindread.losead.NewsFeed.NewsFeedMain;
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

public class LostAndFoundMain extends AppCompatActivity {

    private RecyclerView lnfRecyclerView;
    private FirebaseAuth lnfAuth;
    private FirebaseUser lnfUser;
    private FirebaseDatabase lnfDatabase;
    private DatabaseReference lnfReference;
    private List<LostAndFound> lnfList;
    private Toolbar lnfToolbar;
    private Parcelable recyclerViewState;
    private ProgressDialog progressDialog;
    private FloatingActionButton lnfButtonAdd;
    private LostAndFoundAdapter lnfAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_and_found_main);

        lnfToolbar = (Toolbar) findViewById(R.id.lnfToolbar);
        lnfToolbar.setTitle("Lost And Found");
        setSupportActionBar(lnfToolbar);

        lnfRecyclerView = (RecyclerView) findViewById(R.id.lnfRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        lnfRecyclerView.setLayoutManager(linearLayoutManager);
        lnfRecyclerView.setItemAnimator(new DefaultItemAnimator());
        lnfRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        lnfButtonAdd = (FloatingActionButton) findViewById(R.id.lnfFloatingButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        lnfList = new ArrayList<>();

        lnfDatabase = FirebaseDatabase.getInstance();
        lnfReference = lnfDatabase.getReference("Lost And Found");
        lnfAuth=FirebaseAuth.getInstance();
        lnfUser=lnfAuth.getCurrentUser();

        lnfReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recyclerViewState = lnfRecyclerView.getLayoutManager().onSaveInstanceState();
                lnfRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                lnfList.clear();

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    HashMap<String, String> bnsMap = new HashMap<>();

                    for (DataSnapshot ds : postSnapShot.getChildren()) {
                        if((!ds.getKey().equals("comments"))) {
                            bnsMap.put(ds.getKey(), ds.getValue(String.class));
                        }
                    }

                    Log.e("Test bns", bnsMap.get("pubDate"));

                    lnfList.add(new LostAndFound(bnsMap.get("contact"), bnsMap.get("desc"), bnsMap.get("image"), postSnapShot.getKey(),bnsMap.get("price"), bnsMap.get("seller"), bnsMap.get("pubDate"), bnsMap.get("title"), bnsMap.get("postedBy")));

                }

                progressDialog.dismiss();
                lnfAdapter = new LostAndFoundAdapter (lnfList, getApplicationContext());
                lnfRecyclerView.setAdapter(lnfAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lnfButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LostAndFoundAdd.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buy_and_sell_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.bnsAddPost){
            startActivity(new Intent(getApplicationContext(),LostAndFoundAdd.class));

        }

        else if(item.getItemId()==R.id.bnsMyPosts){
            startActivity(new Intent(getApplicationContext(),LostAndFoundPost.class));
        }

        else if(item.getItemId()==R.id.bnsLogOut){
            lnfAuth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        else if(item.getItemId()==R.id.bnsReport){
            startActivity(new Intent(getApplicationContext(),ReportBugMain.class));
        }

        return true;
    }

}

