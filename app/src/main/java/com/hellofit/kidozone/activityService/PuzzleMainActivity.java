package com.hellofit.kidozone.activityService;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hellofit.kidozone.R;

/***
 *  This class is the main page for puzzle game
 *
 *  Created by Mingzhe Liu on 08/30/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 3.5
 *
 *  Final modified date: 10/15/2019 by Mingzhe Liu
 */

public class PuzzleMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle);

        Button imageAnimal = (Button) findViewById(R.id.imageAnimal);

        imageAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PuzzleMainActivity.this, PuzzleAnimalActivity.class);
                startActivity(intent);
            }
        });

        Button imageSport = (Button) findViewById(R.id.imageSport) ;

        imageSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PuzzleMainActivity.this, PuzzleSportActivity.class);
                startActivity(intent);
            }
        });

        Button imageView = (Button) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PuzzleMainActivity.this, PuzzleViewActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(PuzzleMainActivity.this, MainActivity.class);
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
}

