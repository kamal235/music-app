package com.example.kamal.arshmusic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class First extends AppCompatActivity {
ImageView homeims;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
     // String defaultimg="https://cdn.pixabay.com/photo/2016/12/01/07/30/music-1874621_960_720.jpg";
        homeims=(ImageView) findViewById(R.id.ims);
        homeims.setImageResource(R.drawable.pic);
      //  Picasso.with(this).load(defaultimg).into(homeims);
 /*        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Toast.makeText(First.this, "welcome to chukhall's music player ", Toast.LENGTH_SHORT).show();

            }
        }, 5000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               chukhal();
            }
        }, 10000);*/
        chukhal();


    }

    public void chukhal()
    {
        Intent kill = new Intent(this, MainActivity.class);
        startActivity(kill);
    }
}
