package com.example.ebookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class UserPage extends AppCompatActivity {


    private EditText user;
    public String user_id;

    private Button btn , logOut;

    private TextView txt , priceView;

    private ArrayList<BooksList> list_book = new ArrayList<BooksList>();

    private ArrayList<String> aslist = new ArrayList<String>();
    private ArrayAdapter adapter;

    private DatabaseReference mDatabase ;
    private ListView listView;

    String st;
    String name;

    int price;
    long total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);


       // user = findViewById(R.id.editText);
        txt = findViewById(R.id.textView);
        adapter = new ArrayAdapter(this , android.R.layout.simple_expandable_list_item_1 , aslist);
        listView = findViewById(R.id.userBookListShow);
        listView.setAdapter(adapter);

        priceView = findViewById(R.id.priceView);

        logOut = findViewById(R.id.logOut);

        //mDatabase = FirebaseDatabase.getInstance().getReference("Users/"+user_id+"/Name");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn = findViewById(R.id.userPage);


        Bundle extras = getIntent().getExtras();
        user_id = extras.getString("key");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPage.this , BookStoreView.class);
                intent.putExtra("Uid" , user_id);
                startActivity(intent);
            }
        });



        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 name = (String) dataSnapshot.child("Name").getValue();

                 if(name != null) {
                     Log.i("Check", name);
                 }
                 txt.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("BookList");

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0 ;
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Map<String , Object> map = (Map<String, Object>) ds.getValue();
                    Object name = map.get("BookName");
                    Object price = map.get("Price");
                    Object url = map.get("Url");
                    Log.i("User check" , (String) name);

                    BooksList bk_user = new BooksList((String) name , String.valueOf(price) , (String)url);
                    list_book.add(bk_user);
                    aslist.add(list_book.get(i).getFileName() + " "+ list_book.get(i).getPrice());
                    adapter.notifyDataSetChanged();

                    Log.i("Val Checking" , aslist.get(i));
                    String s = list_book.get(i).getPrice();
                    total = total + Long.parseLong(s);
                    //price = price + Integer.p(s);
                    priceView.setText("Total BDT "+String.valueOf(total));
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(UserPage.this , MainActivity.class));

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(UserPage.this , UserPdfView.class);

                intent.putExtra("PDF" , list_book.get(position).getUrl());
                startActivity(intent);
            }
        });




    }


}
