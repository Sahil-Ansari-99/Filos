package com.example.sellfindread.losead.SignIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sellfindread.losead.MainActivity;
import com.example.sellfindread.losead.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {
    private EditText name,email,pwd,course;
    private TextView alreadyMember;
    private Button register;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);

        name = findViewById(R.id.input_name);
        email = findViewById(R.id.input_email);
        pwd = findViewById(R.id.input_password);
        course = findViewById(R.id.input_course);
        register = findViewById(R.id.btn_signup);
        alreadyMember=findViewById(R.id.link_login);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    private void createNewAccount() {
        progress.setMessage("Signing In...  ");
        final String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPass = pwd.getText().toString();
        final String userCourse = course.getText().toString().trim();
        final int passLength=userPass.length();

        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userEmail)&&!TextUtils.isEmpty(userPass)&&!TextUtils.isEmpty(userCourse)&&passLength>=6&&userEmail.contains("@smail.iitm.ac.in")){

            progress.show();

            mAuth.createUserWithEmailAndPassword(userEmail,userPass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult!=null){

                                String user = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserdb = mDatabaseReference.child(user);
                                currentUserdb.child("Name").setValue(userName);
                                currentUserdb.child("Course").setValue(userCourse);
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignIn.this, "Successfully registered", Toast.LENGTH_LONG).show();
                                            finish();
                                            Intent intent = new Intent(SignIn.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(SignIn.this, "Verification mail has been sent to your Smail.", Toast.LENGTH_LONG).show();
                                            Toast.makeText(SignIn.this, "please check your Smail", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(SignIn.this, "try again", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                                progress.dismiss();

                            }
                            else{
                                progress.dismiss();
                                Toast.makeText(SignIn.this,"email id already exists, try to reset your password by clicking forget password",Toast.LENGTH_LONG).show();
                            }

                        }}
                    );
        }
        else {
            if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(userEmail)||TextUtils.isEmpty(userPass)||TextUtils.isEmpty(userCourse)) {
                Toast.makeText(SignIn.this, "fill all the sections", Toast.LENGTH_LONG).show();
            }
            else if (passLength<6)
            {
                Toast.makeText(SignIn.this,"password should contain atleast 6 characters",Toast.LENGTH_LONG).show();
            }
            else if (!userEmail.contains("@smail.iitm.ac.in")){
                Toast.makeText(SignIn.this,"email must be your smail id",Toast.LENGTH_LONG).show();
            }

        }
    }
}
