package com.example.sellfindread.losead;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sellfindread.losead.AppMain.AppMain;
import com.example.sellfindread.losead.SignIn.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference, userReference;
    private FirebaseUser user;
    private Button login;
    private EditText email,password;
    private ProgressDialog pro;
    private TextView forget,signIn;
    private String userId;
    public static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference("Users");

        login =  findViewById(R.id.btn_login);
        signIn = findViewById(R.id.link_signup);
        email =  findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        forget=findViewById(R.id.link_forget);

        pro=new ProgressDialog(this);

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user= mAuth.getCurrentUser();
                if (user!=null)
                {
                    userId=user.getUid();
                    userReference=mReference.child(userId).child("Name");
                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userName=dataSnapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(MainActivity.this,"Successfully signed in",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this,AppMain.class));
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"not signed in",Toast.LENGTH_SHORT).show();
                }
            }
        };

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid=email.getText().toString();
                if (TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(MainActivity.this,"enter correct emailid",Toast.LENGTH_LONG).show();
                }
                else {
                    mAuth.sendPasswordResetEmail(emailid).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"password reset mail is sent",Toast.LENGTH_LONG).show();
                                finish();
                                forget.setTextColor((Color.rgb(0,0,255)));
                            }
                            else {
                                Toast.makeText(MainActivity.this,"email id is not registered or network problem",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(email.getText().toString())&& !TextUtils.isEmpty(password.getText().toString())){
                    String emailid=email.getText().toString();
                    String pwd=password.getText().toString();
                    login(emailid,pwd);
                }
                else {
                    Toast.makeText(MainActivity.this,"check your internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignIn.class));
            }
        });


    }

    public void login(String emailid, String pwd) {
        pro.setMessage("logging in ...  ");
        pro.show();
        mAuth.signInWithEmailAndPassword(emailid,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "successfully logged in", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(MainActivity.this, postActivity.class));
                            finish();


                            pro.dismiss();

                        } else {
                            Toast.makeText(MainActivity.this, "emailid or password does not exist", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "Please signup", Toast.LENGTH_SHORT).show();
                            pro.dismiss();
                        }
                    }
                });
    }

    private void checkEmailVerification(){
        user=mAuth.getCurrentUser();
        if (user.isEmailVerified()){

            Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, AppMain.class));
            finish();
        }
        else {
            Log.d("check","1");
            Toast.makeText(MainActivity.this, "check your smail to verify your account", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //android.os.Process.killProcess(android.os.Process.myPid());
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}



