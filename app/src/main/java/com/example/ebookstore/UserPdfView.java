package com.example.ebookstore;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UserPdfView extends AppCompatActivity {


    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pdf_view);

        pdfView = (PDFView) findViewById(R.id.pdfView);

        Bundle extras = getIntent().getExtras();
        final String url = extras.getString("PDF");

        //new RetrievePdfStream().execute("https://firebasestorage.googleapis.com/v0/b/ebookstore-30837.appspot.com/o/Books%2FThe%20Impact?alt=media&token=8e3e7bc4-5172-41f6-9c0b-80d5901a5e58");
        new RetrievePdfStream().execute(url);





    }

    class RetrievePdfStream extends AsyncTask<String , Void , InputStream>{

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try{

                URL url = new URL(strings[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if(urlConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }


            } catch (Exception e){
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }
}
