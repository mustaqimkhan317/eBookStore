package com.example.ebookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AdminBookView extends AppCompatActivity {


    public ArrayList<BooksList> listOfBooks = new ArrayList<BooksList>();

    private DatabaseReference reference ;

    ArrayList<String> arrayList ;
    ArrayAdapter arrayAdapter ;

    SearchView searchView;

    ListView adminView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book_view);


        adminView = findViewById(R.id.adminView);

        reference = FirebaseDatabase.getInstance().getReference().child("Books");

        arrayList = new ArrayList<>();

        searchView = (SearchView) findViewById(R.id.search_view);

        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_expandable_list_item_1 , arrayList);


        adminView.setAdapter(arrayAdapter);


        //Bundle extras = getIntent().getExtras();
        //final String user_id = extras.getString("Uid");
        //Log.i("ID " , user_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0 ;
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    //listOfBooks.clear();

                    Map<String , Object> map = (Map<String, Object>) ds.getValue();
                    Object name = map.get("Name");
                    Object price = map.get("Price");
                    Object url = map.get("Url");

                    BooksList bk = new BooksList((String) name , String.valueOf(price) , (String)url);

                    listOfBooks.add(bk);

                    //arrayList.add(listOfBooks.get(i).getFileName()+" : " +listOfBooks.get(i).getPrice());

                    //Log.i("Val Checking" , listOfBooks.get(i).getFileName());
                    //Log.i("Val Checking" , listOfBooks.get(i).getPrice());


                    arrayList.add(listOfBooks.get(i).getFileName() + "\n"+ listOfBooks.get(i).getPrice() +" BDT");
                    Log.i("Val Checking" , arrayList.get(i));
                    arrayAdapter.notifyDataSetChanged();

                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        adminView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(AdminBookView.this , UserPdfView.class);

                intent.putExtra("PDF" , listOfBooks.get(position).getUrl());
                startActivity(intent);
            }
        });




    }
}
