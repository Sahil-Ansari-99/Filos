package com.example.sellfindread.losead.AppMain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.sellfindread.losead.BuyAndSell.BuyAndSellAdd;
import com.example.sellfindread.losead.BuyAndSell.BuyAndSellMain;
import com.example.sellfindread.losead.LostAndFound.LostAndFoundMain;
import com.example.sellfindread.losead.MainActivity;
import com.example.sellfindread.losead.NewsFeed.NewsFeedMain;
import com.example.sellfindread.losead.R;
import com.example.sellfindread.losead.ReportBug.ReportBugMain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppMain extends AppCompatActivity {

    ImageButton bnsButton,newsButton,lnfButton;
    private FirebaseAuth appMainAuth;
    private FirebaseUser appMainUser;
    private Toolbar appMainToolbar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        appMainToolbar=(Toolbar)findViewById(R.id.appMainToolbar);
        appMainToolbar.setTitle(R.string.app_name);
        setSupportActionBar(appMainToolbar);

        bnsButton=(ImageButton)findViewById(R.id.btn_bns);
        newsButton=(ImageButton)findViewById(R.id.btn_news);
        lnfButton=(ImageButton)findViewById(R.id.btn_lnf);

        appMainAuth=FirebaseAuth.getInstance();
        appMainUser=appMainAuth.getCurrentUser();

        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppMain.this,NewsFeedMain.class));
            }
        });

        bnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),BuyAndSellMain.class));
            }
        });

        lnfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LostAndFoundMain.class));
            }
        });

    }

    protected void onStart() {
        super.onStart();

        if(appMainUser.isEmailVerified()){

        }
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Email Not Verified")
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            appMainAuth.signOut();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setMessage("Please go to your inbox and verify your mail")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appMainAuth.signOut();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).create().show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.appMainLogOut){
            appMainAuth.signOut();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            Toast.makeText(getApplicationContext(),"Please Log In!",Toast.LENGTH_LONG).show();
        }

        else if(item.getItemId()==R.id.appMainBug){
            startActivity(new Intent(getApplicationContext(),ReportBugMain.class));
        }
        return true;
    }


}
