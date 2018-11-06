package com.example.sellfindread.losead.ReportBug;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.sellfindread.losead.Models.ReportBug;
import com.example.sellfindread.losead.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportBugMain extends AppCompatActivity {

    private FirebaseAuth reportBugAuth;
    private FirebaseUser reportBugUser;
    private EditText reportBugText;
    private Button reportBugSubmit;
    private FirebaseDatabase reportBugDB;
    private DatabaseReference reportBugRef;
    private Toolbar reportBugToolbar;
    private ProgressDialog reportBugProgress;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_bug_main);

        reportBugToolbar=(Toolbar)findViewById(R.id.reportBugToolbar);
        reportBugToolbar.setTitle("Report Bug");
        setSupportActionBar(reportBugToolbar);

        reportBugText=(EditText)findViewById(R.id.reportBugText);
        reportBugSubmit=(Button)findViewById(R.id.reportBugSubmit);

        reportBugAuth=FirebaseAuth.getInstance();
        reportBugUser=reportBugAuth.getCurrentUser();
        reportBugDB=FirebaseDatabase.getInstance();
        reportBugRef=reportBugDB.getReference("Bugs");

        reportBugProgress=new ProgressDialog(this);

        reportBugSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bugTitle=reportBugText.getText().toString().trim();
                String bugUserId=reportBugUser.getUid();

                if(!bugTitle.equals("")){
                    reportBugProgress.setMessage("Sending...");

                    Date currDate = Calendar.getInstance().getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    DateFormat tFormat = new SimpleDateFormat("H:m");
                    String currTime = tFormat.format(currDate);
                    final String bugPubDate = sdf.format(currDate) + " " + currTime;

                    ReportBug reportBug=new ReportBug(bugTitle, bugPubDate, bugUserId);

                    reportBugRef.push().setValue(reportBug).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            reportBugProgress.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Reported...Thank You",Toast.LENGTH_LONG).show();
                                reportBugText.setText("");
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Failed to report bug",Toast.LENGTH_LONG);
                            }
                        }
                    });
                }

                else if(bugTitle.equals("")){
                    reportBugText.setError("Field cannot remain empty");
                    reportBugText.requestFocus();
                }
            }
        });

    }

}
