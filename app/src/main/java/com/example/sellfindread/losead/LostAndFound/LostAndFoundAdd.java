package com.example.sellfindread.losead.LostAndFound;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.sellfindread.losead.BuyAndSell.BuyAndSellMain;
import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.Models.LostAndFound;
import com.example.sellfindread.losead.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LostAndFoundAdd extends AppCompatActivity {

    private ImageButton lnfAddImg;
    private EditText lnfAddTitle, lnfAddDesc, lnfAddContact, lnfAddPrice;
    private Button lnfAddSubmit;
    private FirebaseDatabase lnfAddDatabase;
    private DatabaseReference lnfAddReference, lnfUserReference;
    private StorageReference lnfStrReference;
    private FirebaseUser lnfAddUser;
    private FirebaseAuth lnfAddAuth;
    private Uri lnfAddImgUri = null;
    private ProgressBar lnfAddProgress;
    private ProgressDialog lnfAddProgressDialog;
    private static final int galleryCode = 1;
    private String lnfAddSeller;
    private RadioGroup lnfRadioGroup;
    private RadioButton lnfRadioButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_and_found_add);

        lnfAddAuth=FirebaseAuth.getInstance();
        lnfAddUser=lnfAddAuth.getCurrentUser();
        lnfAddDatabase=FirebaseDatabase.getInstance();
        lnfAddReference=lnfAddDatabase.getReference("Lost And Found");
        lnfStrReference=FirebaseStorage.getInstance().getReference();
        lnfUserReference=lnfAddDatabase.getReference("Users");

        lnfAddImg=(ImageButton)findViewById(R.id.lnfAddImg);
        lnfAddTitle=(EditText)findViewById(R.id.lnfAddTitle);
        lnfAddDesc=(EditText)findViewById(R.id.lnfAddDescription);
        lnfAddContact=(EditText)findViewById(R.id.lnfAddContact);
        lnfAddSubmit=(Button)findViewById(R.id.lnfAddSubmit);
        lnfRadioGroup=(RadioGroup)findViewById(R.id.lnfRadioGroup);

        lnfAddProgressDialog=new ProgressDialog(this);

        lnfAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addGallery=new Intent(Intent.ACTION_GET_CONTENT);
                addGallery.setType("image/*");
                startActivityForResult(addGallery,galleryCode);
            }
        });

        lnfAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnfUserReference.child(lnfAddUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        lnfAddSeller=dataSnapshot.child("Name").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                startPosting();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==galleryCode && resultCode==RESULT_OK){
            lnfAddImgUri=data.getData();
            lnfAddImg.setImageURI(lnfAddImgUri);
        }
    }

    private void startPosting(){
        lnfAddProgressDialog.setMessage("Posting...");
        final String lnfStrTitle=lnfAddTitle.getText().toString().trim();
        final String lnfStrDesc=lnfAddDesc.getText().toString().trim();
        final String lnfStrContact=lnfAddContact.getText().toString();

        int selectedId=lnfRadioGroup.getCheckedRadioButtonId();
        lnfRadioButton=findViewById(selectedId);

        final String lnfStrPrice=lnfRadioButton.getText().toString();

        Date currDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat tFormat = new SimpleDateFormat("H:m");
        String currTime = tFormat.format(currDate);
        final String lnfAddPubDate = sdf.format(currDate) + " " + currTime;

        if(!TextUtils.isEmpty(lnfStrContact)&&!TextUtils.isEmpty(lnfStrDesc)&&!TextUtils.isEmpty(lnfStrPrice)&&!TextUtils.isEmpty(lnfStrTitle)){
            lnfAddProgressDialog.show();
            final StorageReference filepath=lnfStrReference.child("LNF Product").child(lnfAddImgUri.getLastPathSegment());
            filepath.putFile(lnfAddImgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        DatabaseReference newpost=lnfAddReference.push();
                        LostAndFound lnfAdd=new LostAndFound(lnfStrContact,lnfStrDesc,downloadUri.toString(),lnfAddReference.getKey(),lnfStrPrice,lnfAddSeller,lnfAddPubDate,lnfStrTitle,lnfAddUser.getUid());
                        newpost.setValue(lnfAdd);
                        lnfAddProgressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),LostAndFoundMain.class));
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Upload failed: ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}
