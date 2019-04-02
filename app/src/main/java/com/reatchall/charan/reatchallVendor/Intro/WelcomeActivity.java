package com.reatchall.charan.reatchallVendor.Intro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.reatchall.charan.reatchallVendor.Login.LoginActivity;
import com.reatchall.charan.reatchallVendor.R;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 05/02/18.
 */

public class WelcomeActivity extends YouTubeBaseActivity {

    private static final String TAG = "WelcomeActivity";

    FontTextView customer,business,hindi,telugu,english,skip;

    LinearLayout langLayout;

    YouTubePlayerView video;

    private int CB=0,LG=0;

    private YouTubePlayer.OnInitializedListener onInitializedListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        customer=(FontTextView)findViewById(R.id.customer);
        business=(FontTextView)findViewById(R.id.business);
        hindi=(FontTextView)findViewById(R.id.hindi);
        telugu=(FontTextView)findViewById(R.id.telugu);
        english=(FontTextView)findViewById(R.id.english);
        langLayout=(LinearLayout)findViewById(R.id.lang_layout);
        skip=(FontTextView)findViewById(R.id.skip);



        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.drawable.welcome_blue_white_border);
                business.setBackgroundResource(R.drawable.welcome_blue_light_white_border);

                langLayout.setVisibility(View.VISIBLE);

                CB=1;
                decideUrl(CB,LG);
            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.drawable.welcome_blue_light_white_border);

                business.setBackgroundResource(R.drawable.welcome_blue_white_border);
                langLayout.setVisibility(View.VISIBLE);
                CB=2;
                decideUrl(CB,LG);
            }
        });




        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                telugu.setBackgroundResource(R.drawable.welcome_blue_light_white_border);
                english.setBackgroundResource(R.drawable.welcome_blue_light_white_border);

                hindi.setBackgroundResource(R.drawable.welcome_blue_white_border);
                LG=1;
                decideUrl(CB,LG);
            }
        });



        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hindi.setBackgroundResource(R.drawable.welcome_blue_light_white_border);
                english.setBackgroundResource(R.drawable.welcome_blue_light_white_border);

                telugu.setBackgroundResource(R.drawable.welcome_blue_white_border);
                LG=2;
                decideUrl(CB,LG);
            }
        });


        english.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                hindi.setBackgroundResource(R.drawable.welcome_blue_light_white_border);
                telugu.setBackgroundResource(R.drawable.welcome_blue_light_white_border);

                english.setBackgroundResource(R.drawable.welcome_blue_white_border);
                LG=3;
                decideUrl(CB,LG);
            }
        });


    }


    private void decideUrl(int cb,int lg){
        if(cb!=0 && lg !=0){
            if(cb==1){
                switch (lg){
                    case 1: //play hindi
                        playVideoIntent("Rbs9UjQQxYw");
                        Log.e(TAG, "decideUrl: CUS HINDI" );
                        break;
                    case 2: //play hindi
                        playVideoIntent("Aahj3atxdS4");
                        Log.e(TAG, "decideUrl: CUS TELUGU" );
                        break;
                    case 3: //play hindi
                        playVideoIntent("AWTZVpvPrQ");
                        Log.e(TAG, "decideUrl: CUS ENG" );
                        break;
                }
            }else{
                switch (lg){
                    case 1: //play hindi
                        playVideoIntent("1JldyRLYFy0");
                        Log.e(TAG, "decideUrl: BUS HINDI" );
                        break;
                    case 2: //play hindivWD6kUP9RTY
                        playVideoIntent("vWD6kUP9RTY");
                        Log.e(TAG, "decideUrl: BUS TELUGU" );
                        break;
                    case 3: //play hindi
                        playVideoIntent("CtHTpf_dE_k");
                        Log.e(TAG, "decideUrl: BUS ENG" );
                        break;
                }


            }
        }else{
            if(lg==0)
            Toast.makeText(WelcomeActivity.this,"Please Select Language",Toast.LENGTH_LONG).show();
        }
    }


    public void playVideoIntent(String url){

        Intent intent = YouTubeStandalonePlayer.createVideoIntent(WelcomeActivity.this, "AIzaSyCIIlCYTpnM0Evk_arnUhi_RzHuYd6dFNI", url,100,true,false);
        startActivity(intent);
    }


   public void playVideo(final  String url){
        video=(YouTubePlayerView) findViewById(R.id.video);
        video.setVisibility(View.VISIBLE);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                    if(youTubePlayer.isPlaying()){
                        youTubePlayer.release();
                        youTubePlayer.loadVideo(url);
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                        youTubePlayer.play();

                    }else{
                        youTubePlayer.loadVideo(url);
                        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                        youTubePlayer.play();
                    }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        video.initialize("AIzaSyCIIlCYTpnM0Evk_arnUhi_RzHuYd6dFNI",onInitializedListener);

    }
}
