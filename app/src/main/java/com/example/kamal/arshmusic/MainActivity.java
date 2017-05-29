package com.example.kamal.arshmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.MediaController;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    Integer playcount=0; // count for play and pause
    Integer repeatcount=0; // count for repeat or looping
    boolean songOnsimplePlay=false; //flag for playing song without repeat and shuffle
    boolean shuffleboolean=false;
    MediaPlayer Song;
    TextView songtext;
    Integer pause1;
    Integer SongsInDatabase;
    Integer currentSong;
    String[] listsong;
    String[] listsinger;
    String[] songUrl;
    String[] songpics;
    ImageView repeat;
    ImageView shuffle;
    SeekBar seekvolume;
    Runnable runnable;
    Runnable runnable1;
    Handler handler;
    SeekBar volumeSeekbar = null;
    AudioManager audioManager = null;
    ImageView playbutton;
    ImageView pausebutton;
    ImageView stopbutton;
    ImageView nextbutton;
    ImageView previousbutton;
    Integer[] repeatarray ={0,0};
    ArrayList<Integer> songarrayList;
    ImageView songims;
    String defaultimg;

    // Test
    ArrayList<Integer> songPlayed;
    ArrayList<Integer> songForShuffle;
    Thread testth;
    Thread t3;


    Handler testHan=new Handler() {
        @Override
        public void publish(LogRecord record) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };



    public void initControls()
    {
        try
        {
            volumeSeekbar = (SeekBar)findViewById(R.id.volumebar);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void playCycle(){

        seekvolume.setProgress(Song.getCurrentPosition());
        if(Song.isPlaying())
        {
            runnable= new Runnable() {
                @Override
                public void run() {
              playCycle();
                }
            };
           // handler.postDelayed(runnable,1000);
        }
    }

    public void maincontrol() throws IOException
    {



        //   first
        // when shuffle is off and repeatonce is off and repeatall is off and only play button is clicked
        if(Song==null && shuffleboolean==false && repeatarray[0]==0 && repeatarray[1]==0 && songOnsimplePlay==true)
        {
            Log.d("in the loop",""+" 1");
            songOnsimplePlay=false;
            arraylistcontrol(1);
        }

        //      second
        //      shuffle
        else if (shuffleboolean==true && repeatarray[0]==0 && repeatarray[1]==0)
        {
            Log.d("in the loop",""+" 2");
            arraylistcontrol(3);
        }


        //   third
        //   shuffle and repeatall
        else if (shuffleboolean==true && repeatarray[0]==1 && repeatarray[1]==0)
        {
            Log.d("in the loop",""+" 3");
            arraylistcontrol(4);
        }

        //    fourth
        //    repeatall
        else if (shuffleboolean==false && repeatarray[0]==1 && repeatarray[1]==0)
        {
            Log.d("in the loop",""+" 4");
            arraylistcontrol(6);
        }

        //  fifth
        //  repeatonce
        else if (shuffleboolean==false && repeatarray[0]==1 && repeatarray[1]==1)
        {
            Log.d("in the loop",""+" 5");
            arraylistcontrol(5);
        }

       /* //    pause
        else if (!Song.isPlaying())
        {
            Log.d("in the loop",""+" 6");
            musicplay();
            listenforsong();
        }*/


        //    last
        else
        {
            Log.d("in the loop",""+" last");
            reseteverything();
        }
    }


    public void arraylistcontrol(int kamal) throws IOException
    {
        checkukhal();
        checkukhal1();
        switch (kamal)
        {
            case 1: //Play and next
            {
              //  Thread t3=new Thread(new Runnable() {
                //    @Override
                //    public void run() {
                        if (songarrayList.size()>0) {
                            Log.d("in the loop",""+" case 1");
                            currentSong = songarrayList.get(0);
                           /* Runnable cheking=new Runnable() {
                                @Override
                                public void run() {
                                    justfortest();
                                }
                            };
                            new Thread(cheking).start();*/
                            Log.d("playing song number 11",""+currentSong);
                            songPlayed.add(currentSong);
                           songarrayList.remove(new Integer(currentSong));
                            songForShuffle.remove(new Integer(currentSong));

                            try {
                                musicplay();
                                listenforsong();
                            }
                            catch (IOException e)
                            {}
                        }
                        else        // song to play list is empty
                        {
                            try {
                                reseteverything();
                            }
                            catch (IOException e)
                            {}
                        }
                 //   }
             //   });
              //  t3.start();


            }
            break;
            case 2: //previous
            {
                if (songPlayed.size()>0) {
                    Log.d("in the loop songplayed ",""+songPlayed);
                    Log.d("in the loop arraylist",""+songarrayList);
                    if (songarrayList.contains(songPlayed.get(songPlayed.size() - 1)))
                    {}
                    else {
                        songarrayList.add(0, songPlayed.get(songPlayed.size() - 2));
                        songForShuffle.add(songPlayed.get(songPlayed.size() - 2));
                        songPlayed.remove(songPlayed.size() - 1);
                    }
                    Log.d("in the loop arraylist",""+songarrayList);
                    currentSong = songarrayList.get(0);
                    musicplay();
                   listenforsong();

                }
                else   // played song list is empty
                {
                    reseteverything();
                }
            }
            break;
            case 3: //shuffle
            {
                if(songForShuffle.size()>0) {
                    Log.d("in the loop",""+" case 3");
                    currentSong = songForShuffle.get(0);
                    songPlayed.add(currentSong);
                    songarrayList.remove(new Integer(currentSong));
                    songForShuffle.remove(new Integer(currentSong));
                    musicplay();
                    listenforsong();
                }
                else   // played song list is empty
                {
                    reseteverything();
                }
            }
            break;
            case 4: //shuffle  and repeat
            {
                if(songForShuffle.size()>0) {
                    Log.d("in the loop",""+" case 4");
                    currentSong = songForShuffle.get(0);
                    songPlayed.add(currentSong);
                    songarrayList.remove(new Integer(currentSong));
                   songForShuffle.remove(new Integer(currentSong));
                    musicplay();
                    listenforsong();
                }
                else   // played song list is empty
                {

                    resetarrays();
                    currentSong = songForShuffle.get(0);
                    songPlayed.add(currentSong);
                    songarrayList.remove(new Integer(currentSong));
                    songForShuffle.remove(new Integer(currentSong));
                    musicplay();
                    listenforsong();
                }
            }
            break;
            case 5: //repeat
            {
                Log.d("in the loop",""+" case 5");
                musicplay();
                listenforsong();

            }
            break;
            case 6: //repeat all
            {
                if (songarrayList.size()>0) {
                    Log.d("in the loop",""+" case 6");
                    currentSong = songarrayList.get(0);
                    songPlayed.add(currentSong);
                   songarrayList.remove(new Integer(currentSong));
                    songForShuffle.remove(new Integer(currentSong));
                    musicplay();
                    listenforsong();
                }
                else
                {


                    resetarrays();
                    currentSong = songForShuffle.get(0);
                    songPlayed.add(currentSong);
                    songarrayList.remove(new Integer(currentSong));
                    songForShuffle.remove(new Integer(currentSong));
                    musicplay();
                    listenforsong();
                }
            }
            break;
            default:
                Log.d("in the loop",""+" case default");
                break;

        }
    }


    public void justfortest()

    {

        Log.d("song  played ", songPlayed.toString());
        Log.d("song  song to be play ", songarrayList.toString());
        Log.d("song  shuffle ", songForShuffle.toString());
        songtext.setText("\"" + listsong[currentSong] + "\"" + " by " + listsinger[currentSong]);

        Log.d("playing song number", "" + currentSong);
        if (Song == null) {

            String kamal = songUrl[currentSong];
            Log.d("Song", kamal);
            Song = new MediaPlayer();
            try {
                Song.setDataSource(kamal);
                Song.prepare();
            } catch (IOException e) {
            }
        }



    }





    public void musicplay() throws IOException
    {
        Picasso.with(this).load(songpics[currentSong]).into(songims);
     //   Thread t1=new Thread(new Runnable() {
        //    @Override
        //    public void run() {
                Log.d("song  played ",songPlayed.toString());
                Log.d("song  song to be play ",songarrayList.toString());
                Log.d("song  shuffle ",songForShuffle.toString());
                songtext.setText("\""+listsong[currentSong]+"\""+" by "+listsinger[currentSong]);

                Log.d("playing song number",""+currentSong);
                if(Song == null){
                 //   runnable1= new Runnable() {
                  //      @Override
                //        public void run() {

                            //    testth=new Thread(new Runnable() {
                                 //   @Override
                                  //  public void run() {
                                        String kamal = songUrl[currentSong];
                                        Log.d("Song", kamal);
                                        Song = new MediaPlayer();
                                        try {
                                            Song.setDataSource(kamal);
                                            Song.prepareAsync();
                                        }
                                        catch (IOException e)
                                        {}
                                 //   }
                            //    });

                                Song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {

                                        seekvolume.setMax(mp.getDuration());
                                       // playCycle();
                                        mp.start();


                                    }
                                });


                      //  }
                  //  };
                  //  runnable1.run();






                    // playCycle();
                    seekvolume.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener(){
                        @Override
                        public void onProgressChanged(SeekBar seekBar,int progress,boolean input)
                        {
                            if (input)
                            {
                                Song.seekTo(progress);

                            }
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar){
                            seekvolume.setProgress(Song.getCurrentPosition());
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar){

                        }
                    });





                    // Toast.makeText(MainActivity.this, "Song Play ", Toast.LENGTH_SHORT).show();
                }

                else if(!Song.isPlaying()){
                    Song.seekTo(pause1);
                    Song.start();
                    // Toast.makeText(MainActivity.this, "Song resumed", Toast.LENGTH_SHORT).show();
                }
         //   }
      //  });
      //  t1.start();






    }

    public void listenforsong() throws IOException
    {
       // Thread t2=new Thread(new Runnable() {
         //   @Override
        //    public void run() {
                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp=null;
                        try {
                            maincontrol();
                        }
                        catch (IOException e)
                        {

                        }
                    }
                });
         //   }
      //  });

       // t2.start();
    }

    public void nullsong()
    {
        Song.stop();
        Song = null;
    }

    public void reseteverything() throws  IOException
    {
        Song.release();
        Song=null;
        /*stopbutton.setClickable(false);
        nextbutton.setClickable(false);
        previousbutton.setClickable(false);
        shuffle.setClickable(false);
        repeat.setClickable(false);*/
        disablebuttons();
        playbutton.setImageResource(R.drawable.play);
        Picasso.with(this).load(defaultimg).into(songims);
        songtext.setText("no Song ");
        resetarrays();
      //  repeat.setImageResource(R.drawable.repeat);
       // shuffle.setImageResource(R.drawable.shuffle);
        shuffleboolean=false;
        playcount=0;
        repeatarray[0]=0;
        repeatarray[1]=0;
    }

    public void resetarrays() throws IOException
    {
        songarrayList.clear();
        songForShuffle.clear();
        songPlayed.clear();
        for (Integer i=0;i<SongsInDatabase;i++)
        {
            songarrayList.add(i);
            songForShuffle.add(i);
        }
        Collections.shuffle(songForShuffle);


    }

    public void mainplay(View view) throws IOException
    {
        if (playcount>=2)
        {
            playcount=0;
        }

        if(view==null)
        {
            currentSong =0;
            maincontrol();

            //musicplay(currentSong);
        }
        else if (!view.equals(null))
        {


            if (playcount==0)     // plays as play button
            {
               /* stopbutton.setClickable(true);
                nextbutton.setClickable(true);
                previousbutton.setClickable(true);
                shuffle.setClickable(true);
                repeat.setClickable(true);*/
               enablebutton();
             //   checkukhal();

                playbutton.setImageResource(R.drawable.pause);
                currentSong =0;
                songOnsimplePlay=true;

               maincontrol();
               // musicplay();
               // arraylistcontrol(1);

            }
            else if(playcount==1)  // plays as pause button
            {
                playbutton.setImageResource(R.drawable.play);

                playbutton.setEnabled(true);
                if(Song!= null){
                    Song.pause();
                    pause1 = Song.getCurrentPosition();
                    //  Toast.makeText(MainActivity.this, "Song Pause", Toast.LENGTH_SHORT).show();
                }
            }

            playcount++;

        }
    }

    public void mainnext(View view) throws IOException
    {
        Song.release();
        Song=null;
       // nullsong();

        arraylistcontrol(1);
    }

    public void mainPrevious(View view) throws IOException
    {
        Song.release();
        Song=null;
        arraylistcontrol(2);
    }

    public  void mainshuffle(View view) throws IOException
    {
        shuffleboolean=!shuffleboolean;
        if(shuffleboolean==true)
        {
            shuffle.setImageResource(R.drawable.shuffleenabled);
            if (repeatarray[1]==1)
            {
                repeatcount++;
                repeatarray[1]=0;
                repeat.setImageResource(R.drawable.repeat);
                listenforsong();

            }
        }
        else
        {
            shuffle.setImageResource(R.drawable.shuffle);
            listenforsong();
        }

    }

    public void mainrepeat(View view) throws IOException
    {
        if (repeatcount>=3)
        {
            repeatcount=0;
        }

        if (!view.equals(null))
        {
            if (repeatcount==0)
            {
                repeat.setImageResource(R.drawable.repeatall);
                repeatarray[0]=1;
                listenforsong();

            }
            else if (repeatcount==1 && shuffleboolean==false)
            {
                repeat.setImageResource(R.drawable.repeatonce);
                repeatarray[1]=1;
                listenforsong();
            }


            else if (repeatcount==2 || repeatcount==1)
            {
                repeat.setImageResource(R.drawable.repeat);
                repeatarray[0]=0;
                repeatarray[1]=0;
                if (repeatarray[1]==0)
                {
                    repeatcount++;
                }
                listenforsong();
            }
            repeatcount++;

        }
    }


    public void mainstop(View view) throws  IOException
    {
        Song.stop();
        reseteverything();
    }

    public void disablebuttons()
    {
        stopbutton.setClickable(false);
        nextbutton.setClickable(false);
        previousbutton.setClickable(false);
        shuffle.setClickable(false);
        repeat.setClickable(false);
        stopbutton.setImageResource(R.drawable.dstop);
        nextbutton.setImageResource(R.drawable.dnext);
        previousbutton.setImageResource(R.drawable.dprevious);
        repeat.setImageResource(R.drawable.drepeat);
        shuffle.setImageResource(R.drawable.dshuffle);
    }

    public void enablebutton()
    {
        stopbutton.setClickable(true);
        nextbutton.setClickable(true);
        previousbutton.setClickable(true);
        shuffle.setClickable(true);
        repeat.setClickable(true);
        stopbutton.setImageResource(R.drawable.stop);
        nextbutton.setImageResource(R.drawable.next);
        previousbutton.setImageResource(R.drawable.previous);
        repeat.setImageResource(R.drawable.repeat);
        shuffle.setImageResource(R.drawable.shuffle);
    }

    public void secondtry(Boolean kamal)
    {
        if (kamal==false)
        {
           /* previousbutton.setClickable(false);
            previousbutton.setImageResource(R.drawable.dprevious);*/
            Log.d("  int the cond","  false ");
        }
        else if(kamal==true)
        {
            /*previousbutton.setClickable(true);
            previousbutton.setImageResource(R.drawable.previous);*/
            Log.d("  int the cond","  true ");
        }
    }

    public void checkukhal() //previous
    {

                            if(songarrayList.size()==songUrl.length)
                            {
                                Log.d("  int the p cond 1 ", ""+songarrayList.size());
                                Log.d("  int the p  cond 1 ", ""+songUrl.length);
                                previousbutton.setClickable(false);
                                previousbutton.setImageResource(R.drawable.dprevious);
                            }
                            else //if(songarrayList.size()<songUrl.length)
                            {
                              //  Log.d("  int the cond","  false ");
                                Log.d("  int the p cond 2 ", ""+songarrayList.size());
                                Log.d("  int the p cond 2 ", ""+songUrl.length);
                                previousbutton.setClickable(true);
                                previousbutton.setImageResource(R.drawable.previous);
                            }

    }

    public void checkukhal1() //next
    {

        if(songarrayList.size()==0)
        {
            Log.d("  int the n cond 1 ", ""+songarrayList.size());
            Log.d("  int the n  cond 1 ", ""+songUrl.length);
            nextbutton.setClickable(false);
            nextbutton.setImageResource(R.drawable.dnext);
        }
        else //if(songarrayList.size()<songUrl.length)
        {
            Log.d("  int the n cond 2 ", ""+songarrayList.size());
            Log.d("  int the n cond 2 ", ""+songUrl.length);
            nextbutton.setClickable(true);
            nextbutton.setImageResource(R.drawable.next);
        }

    }

    public void playlist(View view)
    {
        Intent kill = new Intent(this, listactivity.class);
        startActivity(kill);
    }




   /* public void musicplay(int numbersong) throws IOException
    {
        songtext.setText("\""+listsong[numbersong]+"\""+" by "+listsinger[numbersong]);
        Picasso.with(this).load(songpics[numbersong]).into(songims);
      //  Log.d("playing song number",""+numbersong);
        if(Song == null){
            String kamal=songUrl[numbersong];
            Song=new MediaPlayer();
            Song.setDataSource(kamal);
            Song.prepare();



            Song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    seekvolume.setMax(Song.getDuration());
                    playCycle();
                    Song.start();
                    if(Song.isPlaying())
                    {
                 //      Log.d("Sstatus check : " ,"is playing");
                    }
                    else if(!Song.isPlaying())
                    {
                  //      Log.d("Sstatus check : " ,"is not playing");
                    }

                }
            });
           // playCycle();
            seekvolume.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener(){
                   @Override
                public void onProgressChanged(SeekBar seekBar,int progress,boolean input)
                   {
                       if (input)
                       {
                           Song.seekTo(progress);

                       }
                   }
                   @Override
                public void onStartTrackingTouch(SeekBar seekBar){
                       seekvolume.setProgress(Song.getCurrentPosition());
                   }
                   @Override
                public void onStopTrackingTouch(SeekBar seekBar){

                   }
            });




           // Toast.makeText(MainActivity.this, "Song Play ", Toast.LENGTH_SHORT).show();
             }

        else if(!Song.isPlaying()){
            Song.seekTo(pause1);
            Song.start();
           // Toast.makeText(MainActivity.this, "Song resumed", Toast.LENGTH_SHORT).show();
        }

    }





    public void songplay (final int numbersong)  throws IOException {
        //   first
        // when shuffle is off and repeatonce is off and repeatall is off and only play button is clicked
        if(shuffleboolean==false && repeatarray[0]==0 && repeatarray[1]==0 && songOnsimplePlay==true)
        {
            if (Song == null) {
                musicplay(numbersong);
                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        //is song is not playing
                       if (!Song.isPlaying()) {
                            songarrayList.remove(new Integer(numbersong));

                            songtext.setText("no Song ");
                            playbutton.setImageResource(R.drawable.play);
                            try {
                                stopplyaing(null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            playcount = 0;
                            songOnsimplePlay=false;

                            try {
                                currentSong=songarrayList.get(0);
                                songplay(currentSong);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }

        //      second
        else if (shuffleboolean==true) {
            //repeat at zero position



            if (repeatarray[0]==0 && repeatarray[1]==0 && songarrayList.size()!=0) {
             //   Log.d("father    1234 ", "" + songarrayList.toString());
             //   Log.d("number of song playing", "" +""+numbersong);
              //  Log.d("number of song playing", "" +""+currentSong);
                musicplay(numbersong);
                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        try {


                            songarrayList.remove(new Integer(numbersong));
                            if (songarrayList.size()!=0 && shuffleboolean==true) {
                             //   Log.d("purana arralist 1 ", "" + songarrayList.toString());
                               // //  Log.d("nawa arralist ",""+songarrayList.toString());

                                Song.stop();
                                Song = null;
                                // Log.d("value for Song",Song.toString());
                                // next(null);
                                randomarrayList();
                                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        try {
                                            if (songarrayList.size() != 0) {
                                                Song.stop();
                                                Song = null;
                                                songarrayList.remove(new Integer(currentSong));
                                                if (songarrayList.size() != 0 && shuffleboolean==true) {
                                                    currentSong = songarrayList.get(0);

                                                    // Log.d("purana arralist ",""+songarrayList.toString());

                                                    //   Log.d("number song to del 3",""+numbersong);

                                                  //  Log.d("purana arralist 3", "" + songarrayList.toString());
                                                    // Log.d("nava arralist ",""+songarrayList.toString());
                                                    songplay(currentSong);
                                                } else {
                                                    playbutton.setImageResource(R.drawable.play);
                                                    songtext.setText("no Song ");
                                                    Song = null;
                                                    resetsongArrayList();
                                                    playcount = 0;
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                            else
                            {
                                playbutton.setImageResource(R.drawable.play);
                                songtext.setText("no Song ");
                                Song = null;
                                resetsongArrayList();
                                playcount = 0;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            // repeat at first position
            else if (repeatarray[0]==1 && repeatarray[1]==0)
            {
               // Log.d("father    1234 ", "" + songarrayList.toString());
              //  Log.d("number of song playing", "" +""+numbersong);
              //  Log.d("number of song playing", "" +""+currentSong);
                musicplay(numbersong);
                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        try {


                            songarrayList.remove(new Integer(numbersong));
                            if (songarrayList.size()!=0 && shuffleboolean==true) {
                             //   Log.d("purana arralist 1 ", "" + songarrayList.toString());
                                //  Log.d("nawa arralist ",""+songarrayList.toString());

                                Song.stop();
                                Song = null;
                                // Log.d("value for Song",Song.toString());
                                // next(null);
                                randomarrayList();
                                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        try {
                                            if (songarrayList.size() != 0) {
                                                Song.stop();
                                                Song = null;
                                                songarrayList.remove(new Integer(currentSong));
                                                if (songarrayList.size() != 0 && shuffleboolean==true) {
                                                    currentSong = songarrayList.get(0);

                                                    // Log.d("purana arralist ",""+songarrayList.toString());

                                                    //   Log.d("number song to del 3",""+numbersong);

                                                   // Log.d("purana arralist 3", "" + songarrayList.toString());
                                                    // Log.d("nava arralist ",""+songarrayList.toString());
                                                    songplay(currentSong);
                                                } else {

                                                    Song = null;
                                                    resetsongArrayList();
                                                    songplay(songarrayList.get(0));
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                            else
                            {

                                Song = null;
                                resetsongArrayList();
                             songplay(songarrayList.get(0));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }



            // kill2(null);
        }

        //    third
        else if (shuffleboolean==false && (repeatarray[0]==1 || repeatarray[1]==1)) {
            //repeat all songs
            if (repeatarray[0]==1 && repeatarray[1]==0)
            {
                musicplay(numbersong);
                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            Song.stop();
                            Song = null;
                            next(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
            // repeat one song
            else if(repeatarray[0]==1 && repeatarray[1]==1)
            {
                musicplay(numbersong);
                Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            Song.stop();
                            Song = null;
                            songplay(numbersong);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }


        }

        else
        {
            songtext.setText("no Song ");
        }


        *//*Log.d("entered in repeat" ," true");
        songtext.setText("\""+listsong[numbersong]+"\""+" by "+listsinger[numbersong]);
        Log.d("playing song number",""+numbersong);
        if(Song == null){
            String pathofsong=songUrl[numbersong];
            Song=new MediaPlayer();
            Song.setDataSource(pathofsong);
            Song.prepare();

            Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    try {
                        Song.stop1();
                        Song=null;
                       // Log.d("value for Song",Song.toString());
                        kill2(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });



            Song.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekvolume.setMax(Song.getDuration());
                    playCycle();
                    Song.start();

                }
            });
            // playCycle();
            seekvolume.setOnSeekBarChangeListener(new  SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar seekBar,int progress,boolean input)
                {
                    if (input)
                    {
                        Song.seekTo(progress);
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar){

                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar){

                }
            });

            Toast.makeText(MainActivity.this, "Song Play ", Toast.LENGTH_SHORT).show();    }

        else if(!Song.isPlaying()){
            Song.seekTo(pause1);
            Song.start();
            Toast.makeText(MainActivity.this, "Song resumed", Toast.LENGTH_SHORT).show();     }*//*


        // new






    }


    public void randomarrayList() throws IOException {
        if (songarrayList.size()!=0) {
            Collections.shuffle(songarrayList);

            currentSong = songarrayList.get(0);
          //  Log.d("purana arralist 2",""+songarrayList.toString());
         //   Log.d("number song to del 2",""+currentSong);
            musicplay(currentSong);
        }
    }


    public void kill2(View view) throws IOException {
      //  playbutton.setEnabled(false);
       // pausebutton.setEnabled(true);
       // stopbutton.setEnabled(true);
       // nextbutton.setEnabled(true);
       // previousbutton.setEnabled(true);
        if (playcount>=2)
        {
            playcount=0;
        }
 
        if(view==null)
        {
            currentSong =0;
            Random rand = new Random();
            int  n = rand.nextInt(SongsInDatabase -1) + 0;
            currentSong =n;
           // Log.d("CONTEXT",""+n);

           songplay(currentSong);

            //musicplay(currentSong);
        }
        else if (!view.equals(null))
        {


            if (playcount==0)     // plays as play button
            {
                stopbutton.setClickable(true);
                nextbutton.setClickable(true);
                previousbutton.setClickable(true);
                playbutton.setImageResource(R.drawable.pause);
                currentSong =0;
                Random rand = new Random();
                int  n = rand.nextInt(SongsInDatabase -1) + 0;
                currentSong =n;
              //  Log.d("CONTEXT",""+n);
                songOnsimplePlay=true;
                songplay(currentSong);
               // songplay(2);
             //   musicplay(currentSong);
            }
            else if(playcount==1)  // plays as pause button
            {
                playbutton.setImageResource(R.drawable.play);

                playbutton.setEnabled(true);
                if(Song!= null){
                    Song.pause();
                    pause1 = Song.getCurrentPosition();
                  //  Toast.makeText(MainActivity.this, "Song Pause", Toast.LENGTH_SHORT).show();
                }
            }

            playcount++;

        }

       // songplay(currentSong);
    }


    public void songincrement(){
        if(currentSong < SongsInDatabase -1)
        {
            currentSong++;
        }
        else
        {
            currentSong =0;
        }
    }

    public void next(View view) throws IOException {
        if(view==null)
        {
            if(currentSong < SongsInDatabase -1)
            {
                currentSong++;
            }
            else
            {
                currentSong =0;
            }
            Song =null;

            songplay(currentSong);
        }
        else if (view!=null)
        {
            if(currentSong < SongsInDatabase -1)
            {
                currentSong++;
            }
            else
            {
                currentSong =0;
            }
            Song.stop();
            Song =null;
            //  songplay(currentSong);
            songplay(currentSong);
        }

    }


    public void previous(View view) throws IOException {
        if(currentSong >0)
        {
            currentSong--;
        }
        else
        {
            currentSong = SongsInDatabase -1;
        }
        Song.stop();
        Song =null;
      //  Log.d("song name",Song.toString());
      //  songplay(currentSong);
        musicplay(currentSong);
    }


    public void stopplyaing(View view) throws IOException {
        stopbutton.setClickable(false);
        nextbutton.setClickable(false);
        previousbutton.setClickable(false);
        Song.stop();
        playbutton.setImageResource(R.drawable.play);
        Picasso.with(this).load(defaultimg).into(songims);
        songtext.setText("no Song ");
        Song=null;
        resetsongArrayList();
        repeat.setImageResource(R.drawable.repeat);
        shuffle.setImageResource(R.drawable.shuffle);
        shuffleboolean=false;
        playcount=0;
        repeatarray[0]=0;
        repeatarray[1]=0;

    }



    public void repeat1(View view) throws IOException {
      // pause1(null);
      //  kill2(null);
      //repeatchecked=  repeat.isChecked();
     //   if (shuffle.isChecked())
      //  {
      //      repeat.setChecked(false);
      //  }

        if (repeatcount>=3)
        {
            repeatcount=0;
        }
        if (view.equals(null))
        {


        }
        else if (!view.equals(null))
        {
            if (repeatcount==0)
            {
                repeat.setImageResource(R.drawable.repeatall);
                repeatarray[0]=1;

            }
            else if (repeatcount==1 && shuffleboolean==false)
            {
                repeat.setImageResource(R.drawable.repeatonce);
                repeatarray[1]=1;
            }


            else if (repeatcount==2 || repeatcount==1)
            {
                repeat.setImageResource(R.drawable.repeat);
                repeatarray[0]=0;
                repeatarray[1]=0;
                if (repeatarray[1]==0)
                {
                    repeatcount++;
                }
            }
            repeatcount++;

        }
      //  Toast.makeText(MainActivity.this, "first value "+  repeatarray[0], Toast.LENGTH_SHORT).show();
       // Toast.makeText(MainActivity.this, "second value "+  repeatarray[1], Toast.LENGTH_SHORT).show();
      // Log.d("first value", "" + repeatarray[0]);
      //  Log.d("first value",""+repeatarray[1]);
    }

    public void shuffle1(View view)
    {
       // shufflechecked=shuffle.isChecked();
     //   if (shuffle.isChecked())
      //  {
      //      repeat.setChecked(false);
      //  }
        if (view.equals(null))
        {

        }
        else if (!view.equals(null))
        {
            shuffleboolean=!shuffleboolean;
            if(shuffleboolean==true)
            {
                shuffle.setImageResource(R.drawable.shuffleenabled);
                if (repeatarray[1]==1)
                {
                   repeatcount++;
                    repeatarray[1]=0;
                    repeat.setImageResource(R.drawable.repeat);

                }
            }
            else
            {
                shuffle.setImageResource(R.drawable.shuffle);
            }
        }
      //  Toast.makeText(MainActivity.this, "first value "+  repeatarray[0], Toast.LENGTH_SHORT).show();
      //  Toast.makeText(MainActivity.this, "second value "+  repeatarray[1], Toast.LENGTH_SHORT).show();
      //  Log.d("first value",""+repeatarray[0]);
      //  Log.d("first value",""+repeatarray[1]);


    }




    public void resetsongArrayList()
    {
        for (Integer i=0;i<SongsInDatabase;i++)
        {
            songarrayList.add(i);
        }

    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songtext=(TextView) findViewById(R.id.labelsong);
       // repeat =(Switch) findViewById(R.id.switch2);
       // shuffle =(Switch) findViewById(R.id.switch3);
        songtext.setText("no Song ");
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        playbutton=(ImageView) findViewById(R.id.play);
        //pausebutton=(ImageView) findViewById(R.id.pause);
        stopbutton=(ImageView) findViewById(R.id.stop);
        nextbutton=(ImageView) findViewById(R.id.next);
        previousbutton=(ImageView) findViewById(R.id.previous);
        repeat=(ImageView) findViewById(R.id.repeat);
        shuffle=(ImageView) findViewById(R.id.shuffle);
        songarrayList=new ArrayList<Integer>();
        songForShuffle=new ArrayList<Integer>();
        songPlayed=new ArrayList<Integer>();

       /* stopbutton.setClickable(false);
        nextbutton.setClickable(false);
        previousbutton.setClickable(false);
        shuffle.setClickable(false);
        repeat.setClickable(false);*/
       disablebuttons();
        defaultimg="http://icons.iconarchive.com/icons/cornmanthe3rd/plex/512/Media-play-music-icon.png";
        songims=(ImageView) findViewById(R.id.songimage1);
        Picasso.with(this).load(defaultimg).into(songims);
       // songims=(ImageView) findViewById(R.id.songimage1);
       // songims.setImageResource("https://static.blugaa.com/thumbs/wjpai.jpg");

        //pausebutton.setEnabled(false);
       // stopbutton.setEnabled(false);
        //nextbutton.setEnabled(false);
       // previousbutton.setEnabled(false);

        initControls();

        seekvolume=(SeekBar) findViewById(R.id.songseek);
       handler = new Handler() {
           @Override
           public void publish(LogRecord record) {

           }

           @Override
           public void flush() {

           }

           @Override
           public void close() throws SecurityException {

           }
       };
        // handler=new Handler();
      //  ListView listView=(ListView)findViewById(R.id.listView);
     //   CustomAdapter customAdapter=new CustomAdapter();
      //  listView.setAdapter(customAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference root = database.getReference("ARSH");

        root.removeValue();
        DatabaseReference song1=root.push();
        DatabaseReference song2=root.push();
        DatabaseReference song3=root.push();
        DatabaseReference song4=root.push();
        DatabaseReference song5=root.push();
        DatabaseReference song6=root.push();
        DatabaseReference song7=root.push();
        DatabaseReference song8=root.push();



       /* song1.child("song_name").setValue("http://srv7.soundcloudmp3.org/api/download/eyJpdiI6IkJSblI5QnN5T25HTHhyNlE1MWRVOWc9PSIsInZhbHVlIjoiMFNWN2pmaFBKb1FXclpxV2VRZlJQT0NGQmpJTlJqUzBlRUxFbzQ4T1NKS1JPXC85bjU1NWtjRGltZmdDUjJweTJPckhaemZqNE00aGdWTTNyOEhaY21OV0xGYVhUT2pQVm5lb2tWR01ac3BcL3dJRVNJcVErdXkySzY5TFRDVTJpSCIsIm1hYyI6IjI4MGM5ZDE2YTM2ZmYzNGM2NDk4YWI1N2UwYzgzZmY5YzNiYzkzZWE5NDkyZTQxNjU3YTg0OWI4MDYwZDU5ZGYifQ==");
        song1.child("song").setValue("The Rising Sun(Shinsuke Nakamura");
        song1.child("singer").setValue("Lee England Jr.");
        song1.child("SongPic").setValue("http://cdn3-www.wrestlezone.com/assets/uploads/2016/01/shinsuke-nakamura.jpg");

        song2.child("song_name").setValue("http://srv7.soundcloudmp3.org/api/download/eyJpdiI6InpxZDRuSWx2K1lwdUY4TFZQZ3lzU2c9PSIsInZhbHVlIjoid0ZEY3BKSjRXUDBINDlVaHdJaFo0cTlxNDhFR2FIek5EMzJYTjNKWlYwS2FIcHo1eVZ6alwvMk04ZDY4cU43YjVkeFwvRlE2amV4N3hKcHFMZlBsRG1nZWVvTldTNzg3cWxnN0JmU0l6OUp5WT0iLCJtYWMiOiJlMTAwYjkwY2M4MDQwMTVmY2U5YTUwYjMyNTIyYTlhNzJiNDQ0YWRkOTlmYmNhMjljODg0NGFhNjg4ZDFmZWQ0In0=");
        song2.child("song").setValue("Shape of you");
        song2.child("singer").setValue("Ed Sheeran");
        song2.child("SongPic").setValue("https://i.ytimg.com/vi/JGwWNGJdvx8/hqdefault.jpg");

        song3.child("song_name").setValue("http://srv9.soundcloudmp3.org/api/download/eyJpdiI6IjRIRGZobXdSbWdUREI3VzR2Z1pBV3c9PSIsInZhbHVlIjoiTXZhQStDV3lPTzRzY2ZTMUxNVEhIcjJlSUNPQ2s2eXVyZmtBVWpzWWF2NXVqenVHbFMzNDJpTGJzb2JtREZFRk40cnhGbEd3cEgwT3U2QSt2UFVrVXd2SGZ6aEpNNEVDT08xQUp3THZrSnZIQ2tlUHFpNGFoMkpxc3NuWURVS0UiLCJtYWMiOiI4MzY4MDJhYTY2Y2Q3ODQ3ZmEwNWRjYTNiYjQzNmE2OWFlNzNiNDlmMjc3ZjkxZWU2NjVlZGJkNjk3YTg2YjlkIn0=");
        song3.child("song").setValue("wiggle wiggle");
        song3.child("singer").setValue("Jason Derulo");
        song3.child("SongPic").setValue("http://2.bp.blogspot.com/-CUlESTexIso/U9K09ip9SrI/AAAAAAAACUI/vpg_k5tdGi4/s1600/wiggle.jpg");

        song4.child("song_name").setValue("http://srv9.soundcloudmp3.org/api/download/eyJpdiI6ImNNdmJsRWszWFh2NHVyZkFFTVU2SVE9PSIsInZhbHVlIjoiZTFoVDdnYXQzblZrZUdZK2k1cGdYWmptcVB6WDJKYWQrYjdQWE12eFVqSXZPY05VVFl2RjV0ZlQzTlwvS3hVZytYR3VwM1l0b3dFTnNTNTNxK09VUUlTbHRXRHhcL2Eydjd4WGtoNWNFNStVeVJFNUdcLzJJV3RPOFk5cFVmWEtveTVCbTlRK3V1a0Y1aUtuU2luWWc5WHd3PT0iLCJtYWMiOiJhMDA1YjQzYzlmYTJlNjU0YjA0ZWUzZWJiYzdmMTdkMGViZTNlZmJhYmVjOGI5NTllNmFkZDc0ZTA3YzFmZmVmIn0=");
        song4.child("song").setValue("See you Again");
        song4.child("singer").setValue("Wiz khalifa");
        song4.child("SongPic").setValue("http://www.videosonglyrics.com/wp-content/uploads/2017/05/see-you-again.jpg");

        song5.child("song_name").setValue("http://srv7.soundcloudmp3.org/api/download/eyJpdiI6Ikkzd2oxeStkYTRnb1QrYkd6d21id0E9PSIsInZhbHVlIjoiXC9pOTBnUmx6R3JpdjF5MHd2XC9SUHNtXC8rNU5ZRDF2QnZMZHBaVkRWeGVPbG9YK3BoMU1lTVdmZE5YcjBXKzlVWlpxeEZOV09aMVBOVGxxSmxjQndnU3ZTTHZFSEFPc3VhQnhtMWxrcTVSZEcrZEhoMzJlSHRTWFhlanN1akRKaUoyN3ZkamcreUdiY09qVStOcTNyRERRPT0iLCJtYWMiOiJmNTE3NjEwYjQ2NTdjZjI5Yjk2NTJiZTJkYmIzNGJlZjk1MDBiOGM5YjE5MzhlMzYwOTkyMTc2MTEzOTEwZmRkIn0=");
        song5.child("song").setValue("Cheerleader");
        song5.child("singer").setValue("OMI");
        song5.child("SongPic").setValue("https://i.ytimg.com/vi/jGflUbPQfW8/maxresdefault.jpg");

        song6.child("song_name").setValue("http://srv7.soundcloudmp3.org/api/download/eyJpdiI6InRqNDRFN3hcL3ZsbE5oNlwvU2EwK2thdz09IiwidmFsdWUiOiJrOCtwaVdtdGVNRzZsWHlvdmNDT2M2eWFYVjdISWxZXC94WXdXRkt2QjhCSTcwcm9KZWwyMVBDdDczOHcyRDJOT25mN0Jtb0d3aTlSdWlPSFo5TUdacWs1R0NTSm9MTlFlb0dZMDFHYWJSSkFmODZ0WDk3cXZtUWJhNzhvK1l4bUJUaFJWbUdrZlBWTmQxZjhaQWJOckRnPT0iLCJtYWMiOiIwYmMwYmNiZTZmZDBjOWNkZTQ4ZjMyMThhMzg5MDYzYmZiYmFlOGM2ZDk3MzFiNzJjNmQ4MDRlYzFhNGNkYjAxIn0=");
        song6.child("song").setValue("Lean On");
        song6.child("singer").setValue("Major Lazer");
        song6.child("SongPic").setValue("http://refitrev.com/wp-content/uploads/2015/10/lean-on.jpg");

        song7.child("song_name").setValue("http://srv7.soundcloudmp3.org/api/download/eyJpdiI6InFSdnFzSldBQXVDcU95VmRWVVNJamc9PSIsInZhbHVlIjoiT29DYVNEN2hOYXc2YnhGcmU1MXNNYk9KY2pwSWs1YTVyeVRpc25iOXFybWFpUmkrTk12MmtaTzVJZzJ6RlFjS3Qwam5HdlA1YTVMb05SVFlwN1FyUEFMa2tvVGdTR3A0S3BMdWpRZ3d6M0hsbDBCaGd1SnRocWJzU1I3MVVSMVIiLCJtYWMiOiI3OWVmNzgxZmU2ZjY3YmYyMTE4YTgzNjcyZWU3YWVlNjBhY2EwNjI5YWQxOTllYzk2ZjY4MjA3OGRjOTZmODY1In0=");
        song7.child("song").setValue("Chandelier");
        song7.child("singer").setValue("Sia");
        song7.child("SongPic").setValue("https://i.ytimg.com/vi/Iv8Df437ptw/hqdefault.jpg");

        song8.child("song_name").setValue("http://srv7.soundcloudmp3.org/api/download/eyJpdiI6IlF3T2tsd1B4UG1wQlc5Wm1Ud216Tmc9PSIsInZhbHVlIjoib0Q2b1piVk93djZONmY5cnJWMGxTcFFuZ3lIcThYak90S1JuMDQxVjNRV2pZNTErTXJ4VExKN05CSGJUOW1JNUxFUjZjbFVIV3JMMFFoamRReHVSbkRZZFwvdGs0aW92S0hrdzdnaENkZGtWVHBXMFRseEtubVAxN3ltSlFSbkJ6IiwibWFjIjoiNzUyM2ZmOWQzYTE1OTQzNGU4YzUxOGNhN2FjZjIxNzI3YmJiODMyZmY3ZGZjMjFkNGFjOTY4OTExNDcwZTg0NyJ9");
        song8.child("song").setValue("Cheap thrills");
        song8.child("singer").setValue("Sia");
        song8.child("SongPic").setValue("https://i.ytimg.com/vi/nYh-n7EOtMA/maxresdefault.jpg");*/

       song1.child("song_name").setValue("http://s1281.ve.vc/data/128/39536/283190/Bomb%20Jatt%20-%20Amrit%20Maan%20(DjPunjab.Com).mp3");
       // song1.child("song_name").setValue("http://srv7.soundcloudmp3.org/api/download/eyJpdiI6InpxZDRuSWx2K1lwdUY4TFZQZ3lzU2c9PSIsInZhbHVlIjoid0ZEY3BKSjRXUDBINDlVaHdJaFo0cTlxNDhFR2FIek5EMzJYTjNKWlYwS2FIcHo1eVZ6alwvMk04ZDY4cU43YjVkeFwvRlE2amV4N3hKcHFMZlBsRG1nZWVvTldTNzg3cWxnN0JmU0l6OUp5WT0iLCJtYWMiOiJlMTAwYjkwY2M4MDQwMTVmY2U5YTUwYjMyNTIyYTlhNzJiNDQ0YWRkOTlmYmNhMjljODg0NGFhNjg4ZDFmZWQ0In0=");
        song1.child("song").setValue("Bamb Jatt");
        song1.child("singer").setValue("Ghussa");
        song1.child("SongPic").setValue("https://cover.djpunjab.com/39536/300x700/Bamb-Jatt-Amrit-Maan.jpg");

        song2.child("song_name").setValue("http://s1281.ve.vc/data/128/39673/283417/Saade%20Aala%20-%20Sharry%20Mann%20(DjPunjab.Com).mp3");
        song2.child("song").setValue("Saade aala");
        song2.child("singer").setValue("Sharry Maan");
        song2.child("SongPic").setValue("https://i.ytimg.com/vi/JGwWNGJdvx8/hqdefault.jpg");

        song3.child("song_name").setValue("http://s1281.ve.vc/data/128/39832/283635/Jind%20Mahi%20-%20Kulbir%20Jhinjer%20(DjPunjab.Com).mp3");
        song3.child("song").setValue("Jind Mahi");
        song3.child("singer").setValue("Kulbir Jhinjar");
        song3.child("SongPic").setValue("https://cover.djpunjab.com/39832/300x700/Jind-Mahi-Kulbir-Jhinjer.jpg");

      /*  song4.child("song_name").setValue("http://s1281.ve.vc/data/128/39181/282530/Backbone%20-%20Hardy%20Sandhu%20(DjPunjab.Com).mp3");
        song4.child("song").setValue("Backbone");
        song4.child("singer").setValue("Hardy Sandhu");
        song4.child("SongPic").setValue("https://cover.djpunjab.com/39181/300x700/Backbone-Hardy-Sandhu.jpg");

        song5.child("song_name").setValue("http://s1281.ve.vc/data/128/39735/283516/Ring%20-%20Neha%20Kakkar%20(DjPunjab.Com).mp3");
        song5.child("song").setValue("Ring");
        song5.child("singer").setValue("Neha Kakkar");
        song5.child("SongPic").setValue("https://cover.djpunjab.com/39735/300x700/Ring-Neha-Kakkar.jpg");

        song6.child("song_name").setValue("http://s1281.ve.vc/data/128/38193/280695/Kala%20Chashma%20(Baar%20Baar%20Dekho)%20-%20Amar%20Arshi%20(DjPunjab.Com).mp3");
        song6.child("song").setValue("Kaala Chashma");
        song6.child("singer").setValue("Amar Arshi");
        song6.child("SongPic").setValue("https://cover.djpunjab.com/38193/300x700/Kala-Chashma-(Baar-Baar-Dekho)-Amar-Arshi.jpg");

        song7.child("song_name").setValue("http://s1281.ve.vc/data/128/39543/283197/Akhar%20(Lahoriye)%20-%20Amrinder%20Gill%20(DjPunjab.Com).mp3");
        song7.child("song").setValue("Akhar");
        song7.child("singer").setValue("Amrinder Gill");
        song7.child("SongPic").setValue("https://cover.djpunjab.com/39543/300x700/Akhar-(Lahoriye)-Amrinder-Gill.jpg");

        song8.child("song_name").setValue("http://s1281.ve.vc/data/128/39790/283585/Cadillac%20-%20Elly%20Mangat%20(DjPunjab.Com).mp3");
        song8.child("song").setValue("Cheap thrills");
        song8.child("singer").setValue("Sia");
        song8.child("SongPic").setValue("https://i.ytimg.com/vi/nYh-n7EOtMA/maxresdefault.jpg");*/


        // Read from the database
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SongsInDatabase = new Integer((int) dataSnapshot.getChildrenCount());
                try {
                    resetarrays();
                }
                catch(IOException e)
                {

                }
                listsong=new String[SongsInDatabase];
                listsinger=new String[SongsInDatabase];
               songUrl=new String[SongsInDatabase];
                songpics=new String[SongsInDatabase];
                int i=0;
                for (DataSnapshot child:dataSnapshot.getChildren()) {
                    String songurl = child.child("song").getValue(String.class);
                    String singerurl = child.child("singer").getValue(String.class);
                    String songfullurl = child.child("song_name").getValue(String.class);
                    String imageurl = child.child("SongPic").getValue(String.class);
                    listsong[i] = songurl;
                    listsinger[i] = singerurl;
                    songUrl[i]=songfullurl;
                    songpics[i]=imageurl;
                    i++;
                }
            }



            @Override
            public void onCancelled(DatabaseError error) {
            }
        });












    }





}
