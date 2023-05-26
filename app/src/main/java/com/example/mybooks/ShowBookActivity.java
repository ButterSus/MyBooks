package com.example.mybooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.annotations.concurrent.Background;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowBookActivity extends AppCompatActivity {
    TextView content, title, author, genre, year;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);
        imageView = findViewById(R.id.showStoryImageView);
        content = findViewById(R.id.textViewShowStory);
        title = findViewById(R.id.titleShowStory);
        author = findViewById(R.id.showStoryAuthor);
        genre = findViewById(R.id.showStoryGenre);
        year = findViewById(R.id.showStoryYear);
        imageView.setEnabled(false);
        Intent i = getIntent();
        BookReceiver bookReceiver = (BookReceiver) i.getParcelableExtra("extra");

        content.setText("Описание: " + bookReceiver.getContent());
        title.setText("Название: " + bookReceiver.getTitle());
        author.setText("Автор: " + bookReceiver.getAuthor());
        genre.setText("Жанр: " + bookReceiver.getGenre());
        year.setText("Год: " + bookReceiver.getYear());

        if(!bookReceiver.getImgUrl().equals("null")){
            new Thread(new Runnable(){
                @Override
                public void run() {
                    imageView.setEnabled(true);
                    DownloadImageFromPath(bookReceiver.getImgUrl());
                }
            }).start();
        }else{
            imageView.setEnabled(false);
            imageView.getLayoutParams().height = 0;
            imageView.getLayoutParams().width = 0;
        }


    }


    public void DownloadImageFromPath(String path) {
        InputStream in = null;
        Bitmap bmp = null;
        int responseCode = -1;
        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();

            }

        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }

        Bitmap finalBmp = bmp;
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                imageView.setImageBitmap(finalBmp);

            }
        });

    }
}