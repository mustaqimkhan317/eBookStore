package com.example.ebookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class BookStoreView extends AppCompatActivity {


    public ArrayList<BooksList> listOfBooks = new ArrayList<BooksList>();

    private DatabaseReference reference ;

    ListView listView;

    ArrayList<String> arrayList ;
    ArrayAdapter arrayAdapter ;

    SearchView searchView;

    long val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store_view);

        reference = FirebaseDatabase.getInstance().getReference().child("Books");

        arrayList = new ArrayList<>();

        listView = findViewById(R.id.bookView);

        searchView = (SearchView) findViewById(R.id.search_bar);

        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_expandable_list_item_1 , arrayList);
        listView.setAdapter(arrayAdapter);


        Bundle extras = getIntent().getExtras();
        final String user_id = extras.getString("Uid");
        Log.i("ID " , user_id);

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

        //Log.i("Val Checking" , String.valueOf(val));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("BookList");


                user_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("BookList").push();

                //Log.i("Val" , String.valueOf(val));


                String book_name = listOfBooks.get(position).getFileName();
                String book_price = listOfBooks.get(position).getPrice();
                String book_url = listOfBooks.get(position).getUrl();

                user_ref.child("BookName").setValue(book_name);
                user_ref.child("Price").setValue(book_price);
                user_ref.child("Url").setValue(book_url);

                //BookStoreView.this.finish();

                startActivity(new Intent(BookStoreView.this , MainActivity.class));





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




    }
}
