package com.example.sellfindread.losead.NewsFeed;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sellfindread.losead.MainActivity;
import com.example.sellfindread.losead.Models.NewsFeed;
import com.example.sellfindread.losead.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNews extends AppCompatActivity {

    Toolbar addNewsToolBar;
    EditText title,description,link;
    Button submitNews;
    RadioGroup addNewsGroup;
    RadioButton addNewsName;
    private FirebaseAuth addNewsAuth;
    private FirebaseUser addNewsUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_feed_add);

        addNewsToolBar=(Toolbar)findViewById(R.id.addNewsToolbar);
        addNewsToolBar.setTitle("Add News");
        setSupportActionBar(addNewsToolBar);

        title=(EditText)findViewById(R.id.addNewsTitle);
        description=(EditText)findViewById(R.id.addNewsDescription);
        link=(EditText)findViewById(R.id.addNewsLink);
        addNewsGroup=(RadioGroup)findViewById(R.id.addNewsRG);
        submitNews=(Button)findViewById(R.id.btn_addNewsSubmit);

        addNewsAuth=FirebaseAuth.getInstance();
        addNewsUser=addNewsAuth.getCurrentUser();

        submitNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addTitle=title.getText().toString();
                String addDesc=description.getText().toString();
                String addLink=link.getText().toString();
                long addUpvotes=0;
                long addDownvotes=0;
                boolean exec=true;
                String addNewsOwner="";

                int selectedId=addNewsGroup.getCheckedRadioButtonId();

                if(selectedId==R.id.addNewsAnonymous){
                    addNewsOwner="Anonymous";
                }

                else if(selectedId==R.id.addNewsWithName){
                    addNewsOwner=MainActivity.userName;
                }

                if(addTitle.equals("")||addDesc.equals("")){
                    Toast.makeText(getApplicationContext(),"Input Required",Toast.LENGTH_LONG);
                    exec=false;
                }

                if(exec){
                    Date currDate = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    DateFormat tFormat = new SimpleDateFormat("H:m");
                    String currTime = tFormat.format(currDate);
                    String addPubDate = sdf.format(currDate) + " " + currTime;

                    NewsFeed addNewsFeed=new NewsFeed(addTitle, addPubDate, addDesc, addUpvotes, addDownvotes, addLink, NewsFeedMain.dbReference.getKey(), addNewsOwner, addNewsUser.getUid());

                    NewsFeedMain.dbReference.push().setValue(addNewsFeed).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"News Added Successfully",Toast.LENGTH_LONG).show();
                                title.setText("");
                                description.setText("");
                                link.setText("");
                            }

                        }
                    });

                }



            }
        });

    }
}
