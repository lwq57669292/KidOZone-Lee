package com.hellofit.kidozone.activityService;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.hellofit.kidozone.R;
import com.hellofit.kidozone.common.RestClient;
import com.hellofit.kidozone.entity.FoodInfo;
import com.hellofit.kidozone.entity.WasteInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/***
 *  This class is the main page of the app
 *
 *  Created by Mingzhe Liu on 08/30/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 3.3
 *
 *  Final modified date: 10/14/2019 by Weiqiang Li
 */

public class MainActivity extends AppCompatActivity {

    // The list to contain the food entity which using in the game
    private ArrayList<FoodInfo> foodInfos;
    // The list to contain the waste entity which using in the game
    private ArrayList<WasteInfo> wasteInfos;

    MediaPlayer mp;
    int media_length;
    boolean isMute;

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCustonDensity(MainActivity.this,this.getApplication());

        Button imagePuzzle = (Button) findViewById(R.id.imagePuzzleGame);
        Button imageWaste = (Button) findViewById(R.id.imageWasteGame);
        Button imageLunch = (Button) findViewById(R.id.imageLunchGame);
        Button buttonAboutUs = (Button) findViewById(R.id.aboutUsButton);
        final Button btn_mute = (Button) findViewById(R.id.btn_main_mute);

        foodInfos = new ArrayList<FoodInfo>();
        wasteInfos = new ArrayList<WasteInfo>();

        SharedPreferences sp = getSharedPreferences("SystemSP", MODE_PRIVATE);
        mp = MediaPlayer.create(MainActivity.this, R.raw.background_music);
        isMute = sp.getBoolean("isMute", false);
        if (!isMute) {
            Glide.with(MainActivity.this).asBitmap().load(R.drawable.btn_main_mute).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Drawable drawable = new BitmapDrawable(resource);
                    btn_mute.setBackground(drawable);
                }
            });
        } else {
            mp.stop();
            Glide.with(MainActivity.this).asBitmap().load(R.drawable.btn_main_muted).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Drawable drawable = new BitmapDrawable(resource);
                    btn_mute.setBackground(drawable);
                }
            });
        }

        buttonAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.pause();
                media_length = mp.getCurrentPosition();
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("SystemSP", MODE_PRIVATE).edit();
                editor.putBoolean("isMute", isMute);
                editor.putInt("mp_length", media_length);
                editor.commit();
                startActivity(intent);
            }
        });

        imageLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Intent intent = new Intent(MainActivity.this, LunchBoxIntroActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("SystemSP", MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String json = gson.toJson(foodInfos);
                editor.putString("foodList", json);
                editor.putBoolean("isMute", isMute);
                editor.commit();
                startActivity(intent);
            }
        });


        imageWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Intent intent = new Intent(MainActivity.this, WasteIntroActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("SystemSP", MODE_PRIVATE).edit();
                Gson gson = new Gson();
                String json = gson.toJson(wasteInfos);
                editor.putString("wasteList", json);
                editor.putBoolean("isMute", isMute);
                editor.commit();
                startActivityForResult(intent, 1);
            }
        });

        imagePuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Intent intent = new Intent(MainActivity.this, PuzzleIntroActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("SystemSP", MODE_PRIVATE).edit();
                editor.putBoolean("isMute", isMute);
                editor.commit();
                startActivity(intent);
            }
        });

        btn_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMute) {
                    mp = MediaPlayer.create(MainActivity.this, R.raw.background_music);
                    mp.start();
                    Glide.with(MainActivity.this).asBitmap().load(R.drawable.btn_main_mute).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(resource);
                            btn_mute.setBackground(drawable);
                        }
                    });
                    isMute = false;
                } else {
                    mp.stop();
                    Glide.with(MainActivity.this).asBitmap().load(R.drawable.btn_main_muted).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(resource);
                            btn_mute.setBackground(drawable);
                        }
                    });
                    isMute = true;
                }
            }
        });

        GetFoodEntityAsyncTask gfe = new GetFoodEntityAsyncTask();
        gfe.execute();
        GetWasteEntityAsyncTask gwe = new GetWasteEntityAsyncTask();
        gwe.execute();

    }

    private class GetFoodEntityAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            foodInfos = RestClient.parseFoodJson(RestClient.getLunchboxFoodList());
            return null;
        }
    }

    private class GetWasteEntityAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            wasteInfos = RestClient.parseWasteJson(RestClient.getRandomWaste());
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mp != null) {
            mp.pause();
            media_length = mp.getCurrentPosition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mp != null) {
            mp.seekTo(media_length);
            mp.start();
        }
    }


    private static void setCustonDensity(@NonNull Activity activity, @NonNull final Application application){
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if(sNoncompatDensity == 0){
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks(){
                @Override
                public void onConfigurationChanged(Configuration newConfig){
                    if(newConfig != null && newConfig.fontScale >0){
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }
                @Override
                public void onLowMemory(){
                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / 412;
        final float targetScaledDensity = targetDensity*(sNoncompatScaledDensity/sNoncompatDensity);
        final int targetDensityDpi = (int)(160*targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

}
