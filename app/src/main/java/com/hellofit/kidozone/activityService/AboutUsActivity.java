package com.hellofit.kidozone.activityService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hellofit.kidozone.R;

import org.w3c.dom.Text;

/***
 *  This class is the introduction of the applications
 *
 *  Created by Mingzhe Liu on 09/17/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 3.6
 *
 *  Final modified date: 10/17/2019 by Mingzhe Liu
 */

public class AboutUsActivity extends AppCompatActivity {

    private MediaPlayer mp;
    private int media_length;
    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Intent intent = getIntent();
        SharedPreferences sp = getSharedPreferences("SystemSP", MODE_PRIVATE);
        media_length = sp.getInt("mp_length", 0);
        isMute = sp.getBoolean("isMute", false);

        if (!isMute) {
            mp = MediaPlayer.create(AboutUsActivity.this, R.raw.background_music);
            mp.seekTo(media_length);
            mp.start();
        }

        final Button btn_mute_button = (Button) findViewById(R.id.btn_main_mute);
        if (!isMute) {
            btn_mute_button.setBackground(getApplicationContext().getDrawable(R.drawable.btn_main_mute));
        } else {
            btn_mute_button.setBackground(getApplicationContext().getDrawable(R.drawable.btn_main_muted));
        }
        btn_mute_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMute) {
                    mp = MediaPlayer.create(AboutUsActivity.this, R.raw.background_music);
                    mp.start();
                    Glide.with(AboutUsActivity.this).asBitmap().load(R.drawable.btn_main_mute).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(resource);
                            btn_mute_button.setBackground(drawable);
                        }
                    });
                    isMute = false;
                } else {
                    mp.stop();
                    Glide.with(AboutUsActivity.this).asBitmap().load(R.drawable.btn_main_muted).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(resource);
                            btn_mute_button.setBackground(drawable);
                        }
                    });
                    isMute = true;
                }
            }
        });

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");

        TextView tv_title = (TextView) findViewById(R.id.tv_title_about_us) ;
        TextView tv_product_info = (TextView) findViewById(R.id.tv_intro_product);

        tv_title.setTypeface(type);
        tv_product_info.setTypeface(type);

        Button btn_button = (Button) findViewById(R.id.backButton);

        btn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMute)
                    mp.stop();
                Intent intent = new Intent(AboutUsActivity.this, MainActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("SystemSP", MODE_PRIVATE).edit();
                editor.putBoolean("isMute", isMute);
                editor.commit();
                startActivityForResult(intent, 1);
            }
        });
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
}
