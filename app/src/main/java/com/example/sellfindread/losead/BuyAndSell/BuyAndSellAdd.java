package com.example.sellfindread.losead.BuyAndSell;

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
import android.widget.Toast;

import com.example.sellfindread.losead.Models.BuyAndSell;
import com.example.sellfindread.losead.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class BuyAndSellAdd extends AppCompatActivity {

    private ImageButton bnsAddImg;
    private EditText bnsAddTitle,bnsAddDesc,bnsAddContact,bnsAddPrice;
    private Button bnsAddSubmit;
    private FirebaseDatabase bnsAddDatabase;
    private DatabaseReference bnsAddReference,bnsUserReference;
    private StorageReference bnsStrReference;
    private FirebaseUser bnsAddUser;
    private FirebaseAuth bnsAddAuth;
    private Uri bnsAddImgUri=null;
    private ProgressBar bnsAddProgress;
    private ProgressDialog bnsAddProgressDialog;
    private static final int galleryCode=1;
    private String bnsAddSeller;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_and_sell_add);

        bnsAddAuth=FirebaseAuth.getInstance();
        bnsAddUser=bnsAddAuth.getCurrentUser();
        bnsAddDatabase=FirebaseDatabase.getInstance();
        bnsAddReference=bnsAddDatabase.getReference("Buy And Sell");
        bnsStrReference=FirebaseStorage.getInstance().getReference();
        bnsUserReference=bnsAddDatabase.getReference("Users");

        bnsAddImg=(ImageButton)findViewById(R.id.bnsAddImg);
        bnsAddTitle=(EditText)findViewById(R.id.bnsAddTitle);
        bnsAddDesc=(EditText)findViewById(R.id.bnsAddDescpription);
        bnsAddPrice=(EditText)findViewById(R.id.bnsAddPrice);
        bnsAddContact=(EditText)findViewById(R.id.bnsAddContact);
        bnsAddSubmit=(Button)findViewById(R.id.bnsAddSubmit);

        bnsAddProgressDialog=new ProgressDialog(this);

        bnsAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addGallery=new Intent(Intent.ACTION_GET_CONTENT);
                addGallery.setType("image/*");
                startActivityForResult(addGallery,galleryCode);
            }
        });

        bnsAddSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bnsUserReference.child(bnsAddUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        bnsAddSeller=dataSnapshot.child("Name").getValue(String.class);
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
            bnsAddImgUri=data.getData();
            bnsAddImg.setImageURI(bnsAddImgUri);
        }
    }

    private void startPosting(){
        bnsAddProgressDialog.setMessage("Posting...");
        final String bnsStrTitle=bnsAddTitle.getText().toString();
        final String bnsStrDesc=bnsAddDesc.getText().toString();
        final String bnsStrPrice="₹"+bnsAddPrice.getText().toString();
        final String bnsStrContact=bnsAddContact.getText().toString();

        Date currDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat tFormat = new SimpleDateFormat("H:m");
        String currTime = tFormat.format(currDate);
        final String bnsAddPubDate = sdf.format(currDate) + " " + currTime;

        if(!TextUtils.isEmpty(bnsStrContact)&&!TextUtils.isEmpty(bnsStrDesc)&&!TextUtils.isEmpty(bnsStrPrice)&&!TextUtils.isEmpty(bnsStrTitle)){
            bnsAddProgressDialog.show();
            final StorageReference filepath=bnsStrReference.child("BNS Product").child(bnsAddImgUri.getLastPathSegment());
//            filepath.putFile(bnsAddImgUri).addOnSuccessListener(new OnSuccessListener<>() {
//
//                @Override
//                public void onSuccess(Uri uri) {
//                    Uri bnsDownload=uri;
//
//                    BuyAndSell bnsAdd=new BuyAndSell(Long.parseLong(bnsStrContact),bnsStrDesc,bnsDownload.toString(),bnsAddReference.getKey(),Long.parseLong(bnsStrPrice),bnsAddSeller,bnsAddPubDate,bnsStrTitle,bnsAddUser.getUid());
//
//                    bnsAddReference.push().setValue(bnsAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if(task.isSuccessful()){
//                                bnsAddProgressDialog.dismiss();
//                                Toast.makeText(getApplicationContext(),"Post Added Successfully",Toast.LENGTH_LONG).show();
//                                finish();
//                            }
//                        }
//                    });
//
//                }
//            });

            filepath.putFile(bnsAddImgUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        DatabaseReference newpost=bnsAddReference.push();
                        BuyAndSell bnsAdd=new BuyAndSell(bnsStrContact,bnsStrDesc,downloadUri.toString(),bnsAddReference.getKey(), bnsStrPrice,bnsAddSeller,bnsAddPubDate,bnsStrTitle,bnsAddUser.getUid());
                        newpost.setValue(bnsAdd);
                        bnsAddProgressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),BuyAndSellMain.class));
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Upload failed: ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}
