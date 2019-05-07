package com.example.ebookstore;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadPdf extends AppCompatActivity {


    public ArrayList<BooksList> listOfBooks = new ArrayList<BooksList>();

    private Button selectFile , upload  , fetch , store;

    private TextView notification;

    private FirebaseStorage storage;
    private FirebaseDatabase database;

    private EditText bookName , setPrice;

    Uri pdfUri;
    ProgressDialog progressDialog;

    public long val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        selectFile = findViewById(R.id.selectFile);
        upload = findViewById(R.id.upload);
        notification = findViewById(R.id.notification);

        fetch = findViewById(R.id.fetchFiles);
        store = findViewById(R.id.goStore);

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadPdf.this , AdminBookView.class));
            }
        });


        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(UploadPdf.this , BookStoreView.class);
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid() ;
                intent.putExtra( "key",id);
                startActivity(intent); */

                fetch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MainActivity mn = new MainActivity();
                        //mn.signingOut();
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        startActivity(new Intent(UploadPdf.this , MainActivity.class));


                    }
                });



            }
        });


        bookName = findViewById(R.id.bookName);
        setPrice = findViewById(R.id.setPrice);


        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Books");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        val = dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                if(ContextCompat.checkSelfPermission(UploadPdf.this , Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(UploadPdf.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 9);
                }

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri != null){
                    uploadPdf(pdfUri);
                } else {
                    Toast.makeText(UploadPdf.this , "Please Select a File" , Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void uploadPdf(Uri pdfUri) {


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading");
        progressDialog.setProgress(0);
        progressDialog.show();



        String nameBook = bookName.getText().toString();
        String priceSet = setPrice.getText().toString();

        final String fileName = nameBook;
        final String price = priceSet;

        //final String fileName = System.currentTimeMillis()+".pdf";
        //final String fileName = System.currentTimeMillis()+"";
        final StorageReference storageReference = storage.getReference();


        storageReference.child("Books").child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //String url = storageReference.getDownloadUrl().toString();

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uri.isComplete());
                        Uri url = uri.getResult();
                        String s = url.toString();

                        //DatabaseReference reference = database.getReference().child(fileName);

                        //val++;

                        DatabaseReference reference = database.getReference().child("Books").push();


                        //BooksList bk = new BooksList(fileName , price , s);
                        //listOfBooks.add(bk);

                        //BooksList srt = listOfBooks.get(0);
                        //Log.i("Checking BookList" , bk.getPrice());
                        //Log.i("Checking BookList" , srt.getPrice());

                        reference.child("Price").setValue(price);
                        reference.child("Name").setValue(fileName);
                        reference.child("Url").setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(UploadPdf.this , "Done Uploading" , Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(UploadPdf.this , "Didn't work" , Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadPdf.this , "File not in Storage" , Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);


            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPdf();
        } else {
            Toast.makeText(UploadPdf.this , "He is an Asshole" , Toast.LENGTH_LONG).show();
        }
    }

    private void selectPdf() {


        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent , 86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 86 && resultCode == RESULT_OK && data != null){


            pdfUri = data.getData();
            notification.setText("A file is selected : "+ data.getData().getLastPathSegment());

        } else {
            Toast.makeText(UploadPdf.this , "please Select a pdf" , Toast.LENGTH_LONG).show();
        }
    }
}

