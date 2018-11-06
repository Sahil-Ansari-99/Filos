package com.example.sellfindread.losead.BuyAndSell;

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
import com.example.sellfindread.losead.LostAndFound.LostAndFoundAdd;
import com.example.sellfindread.losead.MainActivity;
import com.example.sellfindread.losead.Models.BuyAndSell;
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

public class BuyAndSellMain extends AppCompatActivity {

    private RecyclerView bnsRecyclerView;
    private FirebaseDatabase bnsDatabase;
    private DatabaseReference bnsReference;
    private FirebaseAuth bnsAuth;
    private FirebaseUser bnsUser;
    private List<BuyAndSell> bnsList;
    private BuyAndSellAdapter bnsAdapter;
    private FloatingActionButton bnsButtonAdd;
    private Parcelable recyclerViewState;
    public ProgressDialog progressDialog;
    private Toolbar bnsToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_and_sell_main);

        bnsToolbar=(Toolbar)findViewById(R.id.bnsToolbar);
        bnsToolbar.setTitle("Buy And Sell");
        setSupportActionBar(bnsToolbar);

        bnsRecyclerView=(RecyclerView)findViewById(R.id.bnsRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        bnsRecyclerView.setLayoutManager(linearLayoutManager);
        bnsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bnsRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        bnsButtonAdd=(FloatingActionButton)findViewById(R.id.bnsFloatingButton);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        bnsList=new ArrayList<>();

        bnsDatabase=FirebaseDatabase.getInstance();
        bnsReference=bnsDatabase.getReference("Buy And Sell");
        bnsAuth=FirebaseAuth.getInstance();
        bnsUser=bnsAuth.getCurrentUser();

        bnsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recyclerViewState=bnsRecyclerView.getLayoutManager().onSaveInstanceState();
                bnsRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                bnsList.clear();

                for(DataSnapshot postSnapShot : dataSnapshot.getChildren()){
                    HashMap<String,String> bnsMap=new HashMap<>();

                    for(DataSnapshot ds : postSnapShot.getChildren()){
                        if((!ds.getKey().equals("comments"))) {
                            bnsMap.put(ds.getKey(), ds.getValue(String.class));
                        }
//                        else if(ds.getKey().equals("price")){
//                            bnsMap.put(ds.getKey(), ds.getValue(Long.class).toString());
//                        }
//                        else if(ds.getKey().equals("contact")){
//                            bnsMap.put(ds.getKey(),ds.getValue(Long.class).toString());
//                        }
                    }

                    Log.e("Test bns", bnsMap.get("pubDate"));

                    bnsList.add(new BuyAndSell(bnsMap.get("contact"), bnsMap.get("desc"), bnsMap.get("image"), postSnapShot.getKey(), bnsMap.get("price"), bnsMap.get("seller"), bnsMap.get("pubDate"), bnsMap.get("title"), bnsMap.get("postedBy")));

                }

                progressDialog.dismiss();
                bnsAdapter=new BuyAndSellAdapter(bnsList,getApplicationContext());
                bnsRecyclerView.setAdapter(bnsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bnsButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BuyAndSellAdd.class));
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
            startActivity(new Intent(getApplicationContext(),BuyAndSellAdd.class));

        }

        else if(item.getItemId()==R.id.bnsMyPosts){
            startActivity(new Intent(getApplicationContext(),BuyAndSellPost.class));
        }

        else if(item.getItemId()==R.id.bnsLogOut){
            bnsAuth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        else if(item.getItemId()==R.id.bnsReport){
            startActivity(new Intent(getApplicationContext(),ReportBugMain.class));
        }

        return true;
    }

}
