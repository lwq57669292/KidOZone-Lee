package com.hellofit.kidozone.activityService;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.hellofit.kidozone.R;

/***
 *  This class is the introduction video for puzzle game
 *
 *  Created by Weiqiang Li on 10/05/19.
 *  Copyright @ 2019 Weiqiang Li. All right reserved
 *
 *  @author Weiqiang Li
 *  @version 3.5
 *
 *  Final modified date: 10/16/2019 by Weiqiang Li
 */

public class PuzzleIntroActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private int media_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_intro);

        initView();

        Button skip = (Button) findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PuzzleIntroActivity.this, PuzzleMainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * initial the video view and set the complete action: jump to game page
     */
    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoView);
        init();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(PuzzleIntroActivity.this, PuzzleMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.puzzle_introduction_video;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);
        mediaController.setVisibility(View.INVISIBLE);
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *  Pause the video
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mediaController != null && videoView != null) {
            videoView.pause();
            media_length = videoView.getCurrentPosition();
        }
    }

    /**
     *  Resume the video
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (mediaController != null && videoView != null) {
            videoView.seekTo(media_length);
            videoView.start();
        }
    }
}
