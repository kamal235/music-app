package com.example.kamal.arshmusic;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

/**
 * Created by kamal on 2017-05-21.
 */

public class test extends MainActivity{

    public void maincontrol() throws IOException
    {

         //    pause
         if (!Song.isPlaying())
         {

            musicplay();
            listenforsong();
         }

        //   first
        // when shuffle is off and repeatonce is off and repeatall is off and only play button is clicked
        if(Song==null && shuffleboolean==false && repeatarray[0]==0 && repeatarray[1]==0 && songOnsimplePlay==true)
        {
            songOnsimplePlay=false;
            arraylistcontrol(1);
        }

        //      second
        //      shuffle
        else if (shuffleboolean==true && repeatarray[0]==0 && repeatarray[1]==0)
        {
            arraylistcontrol(3);
        }


        //   third
        //   shuffle and repeatall
        else if (shuffleboolean==true && repeatarray[0]==1 && repeatarray[1]==0)
        {
            arraylistcontrol(4);
        }

        //    fourth
        //    repeatall
        else if (shuffleboolean==false && repeatarray[0]==1 && repeatarray[1]==0)
        {
            arraylistcontrol(6);
        }

        //  fifth
        //  repeatonce
        else if (shuffleboolean==false && repeatarray[0]==1 && repeatarray[1]==1)
        {
            arraylistcontrol(5);
        }


        //    last
        else
        {
            reseteverything();
        }
    }


    public void arraylistcontrol(int kamal) throws IOException
    {
        switch (kamal)
        {
            case 1: //Play and next
            {
                if (songarrayList.size()>=0) {
                    currentSong = songarrayList.get(0);
                    songPlayed.add(currentSong);
                    songarrayList.remove(new Integer(currentSong));
                    songForShuffle.remove(new Integer(currentSong));
                     musicplay();
                    listenforsong();
                }
                else        // song to play list is empty
                {
                    reseteverything();
                }

            }
            case 2: //previous
            {
                if (songPlayed.size()>=0) {
                    songarrayList.add(0, songPlayed.get(songPlayed.size() - 1));
                    songForShuffle.add(songPlayed.get(songPlayed.size() - 1));
                    currentSong = songarrayList.get(0);
                    musicplay();
                    listenforsong();
                }
                else   // played song list is empty
                {
                    reseteverything();
                }
            }
            case 3: //shuffle
            {
                if(songForShuffle.size()>=0) {
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
            case 4: //shuffle  and repeat
            {
                if(songForShuffle.size()>0) {
                    currentSong = songForShuffle.get(0);
                    songPlayed.add(currentSong);
                    songarrayList.remove(new Integer(currentSong));
                    songForShuffle.remove(new Integer(currentSong));
                    musicplay();
                    listenforsong();
                }
                else   // played song list is empty
                {
                    currentSong = songForShuffle.get(0);
                    resetarrays();
                    musicplay();
                    listenforsong();
                }
            }
            case 5: //repeat
            {
                musicplay();
                listenforsong();
            }
            case 6: //repeat all
            {
                if (songarrayList.size()>0) {
                    currentSong = songarrayList.get(0);
                    songPlayed.add(currentSong);
                    songarrayList.remove(new Integer(currentSong));
                    songForShuffle.remove(new Integer(currentSong));
                    musicplay();
                    listenforsong();
                }
                else
                {
                    currentSong = songForShuffle.get(0);
                    resetarrays();
                    musicplay();
                    listenforsong();
                }
            }


        }
    }


    public void musicplay() throws IOException
    {
        songtext.setText("\""+listsong[currentSong]+"\""+" by "+listsinger[currentSong]);
        Picasso.with(this).load(songpics[currentSong]).into(songims);
        //  Log.d("playing song number",""+numbersong);
        if(Song == null){
            String kamal=songUrl[currentSong];
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

    public void listenforsong() throws IOException
    {
        Song.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nullsong();
                try {
                    maincontrol();
                }
                catch (IOException e)
                {

                }
            }
        });
    }

    public void nullsong()
    {
        Song.stop();
        Song = null;
    }

    public void reseteverything() throws  IOException
    {
        Song.stop();
        Song=null;
        stopbutton.setClickable(false);
        nextbutton.setClickable(false);
        previousbutton.setClickable(false);
        playbutton.setImageResource(R.drawable.play);
        Picasso.with(this).load(defaultimg).into(songims);
        songtext.setText("no Song ");
        resetarrays();
        repeat.setImageResource(R.drawable.repeat);
        shuffle.setImageResource(R.drawable.shuffle);
        shuffleboolean=false;
        playcount=0;
        repeatarray[0]=0;
        repeatarray[1]=0;
    }

    public void resetarrays() throws IOException
    {
        songarrayList=null;
        songForShuffle=null;
        songPlayed=null;
        for (Integer i=0;i<SongsInDatabase;i++)
        {
            songarrayList.add(i);
            songForShuffle.add(i);
        }
        Collections.shuffle(songForShuffle);
        songPlayed=null;

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
                stopbutton.setClickable(true);
                nextbutton.setClickable(true);
                previousbutton.setClickable(true);
                playbutton.setImageResource(R.drawable.pause);
                currentSong =0;
                songOnsimplePlay=true;
                maincontrol();

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
        nullsong();
        arraylistcontrol(1);
    }

    public void mainPrevious(View view) throws IOException
    {
        nullsong();
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
        reseteverything();
    }


}
