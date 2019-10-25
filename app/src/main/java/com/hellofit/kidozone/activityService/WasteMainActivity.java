package com.hellofit.kidozone.activityService;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellofit.kidozone.R;
import com.hellofit.kidozone.entity.WasteInfo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

/***
 *  This class is the waste game function class
 *
 *  Created by Mingzhe Liu on 08/30/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 3.6
 *
 *  Final modified date: 10/17/2019 by Mingzhe Liu
 */

public class WasteMainActivity extends AppCompatActivity {

    private float downX;
    private float downY;
    private int listIndex;
    private int testIndex;
    private int score;

    // The list to contain the waste entity which using in the game
    private ArrayList<WasteInfo> wasteInfos;

    ImageView iv_wasteScore;
    TextView tv_userScore;
    LottieAnimationView lottieAnimationView;
    LottieAnimationView yellowBin;
    LottieAnimationView greenBin;
    LottieAnimationView redBin;
    ImageView iv_endPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waste);
        listIndex = 0;

        // Initialize view component
        Button backButton = (Button) findViewById(R.id.backButton);
        TextView tv_wasteName = (TextView) findViewById(R.id.rubbishName);
        ImageView iv_wasteImge = (ImageView) findViewById(R.id.rubbish);
        tv_userScore = (TextView) findViewById(R.id.wasteScore);
        iv_wasteScore = (ImageView) findViewById(R.id.iv_waste_score);
        iv_endPic = (ImageView) findViewById(R.id.iv_waste_end);

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");

        // Load WasteMainActivity data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("SystemSP", MODE_PRIVATE);
        String json = sp.getString("wasteList", null);
        if (json != null) {
            Gson gson = new Gson();
            Type transType = new TypeToken<ArrayList<WasteInfo>>() {}.getType();
            wasteInfos = gson.fromJson(json, transType);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
                AlertDialog builder = new AlertDialog.Builder(WasteMainActivity.this)
                        .setTitle("Oops...")
                        .setIcon(R.drawable.icon_dialog)
                        .setMessage("You really want to quit the game?")
                        .setPositiveButton("Yes, I'm leaving!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(WasteMainActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No, Wrong button.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setCancelable(false)
                        .show();

                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                builder.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(20);
                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(type);

                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.design_default_color_primary));
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(20);
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(type);

                try {
                    // Get mAlert Object
                    Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
                    mAlert.setAccessible(true);
                    Object mAlertController = mAlert.get(builder);

                    // Obtain mTitle object
                    // Set size and color
                    Field mTitle = mAlertController.getClass().getDeclaredField("mTitleView");
                    mTitle.setAccessible(true);
                    TextView mTitleView = (TextView) mTitle.get(mAlertController);
                    mTitleView.setTextSize(25);
                    mTitleView.setTypeface(type);
                    mTitleView.setTextColor(Color.BLACK);

                    // Obtain mMessageView object
                    // Set size and color
                    Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
                    mMessage.setAccessible(true);
                    TextView mMessageView = (TextView) mMessage.get(mAlertController);
                    mMessageView.setTextColor(Color.BLACK);
                    mMessageView.setTextSize(22);
                    mMessageView.setTypeface(type);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });

        iv_wasteScore.setVisibility(View.GONE);
        tv_userScore.setVisibility(View.GONE);
        iv_endPic.setVisibility(View.GONE);
        tv_wasteName.setText(wasteInfos.get(0).getWasteName());
        tv_wasteName.setTypeface(type);
        Glide.with(this).load(wasteInfos.get(0).getWasteImage()).into(iv_wasteImge);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        iv_wasteImge.startAnimation(shake);

//        score = 100;
//        tv_userScore.setTypeface(type);
//        tv_userScore.setText("Score: "+ score + " / 100");

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view4);
        yellowBin = (LottieAnimationView) findViewById(R.id.animation_yellow_bin);
        redBin = (LottieAnimationView) findViewById(R.id.animation_red_bin);
        greenBin = (LottieAnimationView) findViewById(R.id.animation_green_bin);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
        ImageView iv_wasteImge = (ImageView) findViewById(R.id.rubbish);
        TextView tv_wasteName = (TextView) findViewById(R.id.rubbishName);
        tv_wasteName.setTypeface(type);

        if (testIndex == 0) {
            score = 100;
            lottieAnimationView.setVisibility(View.GONE);
            iv_wasteScore.setVisibility(View.VISIBLE);
            tv_userScore.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.fivestar).into(iv_wasteScore);
            tv_userScore.setTypeface(type);
            tv_userScore.setText("Score: 100 / 100");
            testIndex++;
        }

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = x;
                downY = y;
                Log.e("Tag", "=======PressX：" + x);
                Log.e("Tag", "=======PressY：" + y);
                break;

            case MotionEvent.ACTION_UP:
                Log.e("Tag", "=======PressX：" + x);
                Log.e("Tag", "=======PressY：" + y);

                float dx = x - downX;
                float dy = y - downY;

                Log.e("Tag", "========X axis Distance：" + dx);
                Log.e("Tag", "========Y axis Distance：" + dy);
                if (Math.abs(dx) > 300 || Math.abs(dy) > 300) {

                    MediaPlayer mp = MediaPlayer.create(WasteMainActivity.this, R.raw.shoop);
                    mp.start();

                    int orientation = getOrientation(dx, dy);
                    // From 1 - 10 times of playing
                    if(listIndex < wasteInfos.size()) {
                        switch (orientation) {
                            // To right -> Yellow Bin
                            case 'r':
                                if (wasteInfos.get(listIndex).getCategoryName().equals("yellow")) {
                                    MediaPlayer mp1 = MediaPlayer.create(WasteMainActivity.this, R.raw.great);
                                    mp1.start();
                                    sendCorrectMsg(wasteInfos.get(listIndex).getWasteName(),  "Yellow Bin");
                                    if (listIndex < wasteInfos.size() - 1) {
                                        Glide.with(this).load(wasteInfos.get(listIndex + 1).getWasteImage()).into(iv_wasteImge);
                                        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                                        iv_wasteImge.startAnimation(shake);
                                        tv_wasteName.setText(wasteInfos.get(listIndex + 1).getWasteName());
                                        listIndex++;
                                    } else {
                                        listIndex++;
                                        iv_endPic.setVisibility(View.VISIBLE);
                                        Glide.with(this).load(R.drawable.waste_end_pic).into(iv_endPic);
                                        Glide.with(this).load(R.drawable.waste_end_help_pic).into(iv_wasteImge);
                                        ImageView iv_imageDecs = (ImageView) findViewById(R.id.imageDes);
                                        iv_imageDecs.setVisibility(View.INVISIBLE);
                                        iv_wasteImge.setVisibility(View.INVISIBLE);
                                        greenBin.setVisibility(View.INVISIBLE);
                                        redBin.setVisibility(View.INVISIBLE);
                                        yellowBin.setVisibility(View.INVISIBLE);
                                        tv_wasteName.setText("");
                                        break;
                                    }
                                } else {
                                    MediaPlayer mp1 = MediaPlayer.create(WasteMainActivity.this, R.raw.wrong);
                                    mp1.start();
                                    sendWrongMsg();
                                    if (score > 0) {
                                        score = score - 5;
                                        tv_userScore.setText("Score: " + score + " / 100");
                                        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_text);
                                        tv_userScore.startAnimation(shake);
                                        setStarPic(score);
                                    }
                                }
                                break;
                            // To Left -> Red Bin
                            case 'l':
                                if (wasteInfos.get(listIndex).getCategoryName().equals("red")) {
                                    MediaPlayer mp1 = MediaPlayer.create(WasteMainActivity.this, R.raw.great);
                                    mp1.start();
                                    sendCorrectMsg(wasteInfos.get(listIndex).getWasteName(), "Red bin");
                                    if (listIndex < wasteInfos.size() - 1) {
                                        Glide.with(this).load(wasteInfos.get(listIndex + 1).getWasteImage()).into(iv_wasteImge);
                                        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                                        iv_wasteImge.startAnimation(shake);
                                        tv_wasteName.setText(wasteInfos.get(listIndex + 1).getWasteName());
                                        listIndex++;
                                    } else {
                                        listIndex++;
                                        iv_endPic.setVisibility(View.VISIBLE);
                                        Glide.with(this).load(R.drawable.waste_end_pic).into(iv_endPic);
                                        Glide.with(this).load(R.drawable.waste_end_help_pic).into(iv_wasteImge);
                                        ImageView iv_imageDecs = (ImageView) findViewById(R.id.imageDes);
                                        iv_imageDecs.setVisibility(View.INVISIBLE);
                                        iv_wasteImge.setVisibility(View.INVISIBLE);
                                        greenBin.setVisibility(View.INVISIBLE);
                                        redBin.setVisibility(View.INVISIBLE);
                                        yellowBin.setVisibility(View.INVISIBLE);
                                        tv_wasteName.setText("");
                                        break;
                                    }
                                } else {
                                    MediaPlayer mp1 = MediaPlayer.create(WasteMainActivity.this, R.raw.wrong);
                                    mp1.start();
                                    sendWrongMsg();
                                    if (score > 0) {
                                        score = score - 5;
                                        tv_userScore.setText("Score: " + score + " / 100");
                                        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_text);
                                        tv_userScore.startAnimation(shake);
                                        setStarPic(score);
                                    }
                                }
                                break;
                            // To Top -> Green Bin
                            case 't':
                                if (wasteInfos.get(listIndex).getCategoryName().equals("green")) {
                                    MediaPlayer mp1 = MediaPlayer.create(WasteMainActivity.this, R.raw.great);
                                    mp1.start();
                                    sendCorrectMsg(wasteInfos.get(listIndex).getWasteName(), "Green bin");
                                    if (listIndex < wasteInfos.size() - 1) {
                                        Glide.with(this).load(wasteInfos.get(listIndex + 1).getWasteImage()).into(iv_wasteImge);
                                        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                                        iv_wasteImge.startAnimation(shake);
                                        tv_wasteName.setText(wasteInfos.get(listIndex + 1).getWasteName());
                                        listIndex++;
                                    } else {
                                        listIndex++;
                                        iv_endPic.setVisibility(View.VISIBLE);
                                        ImageView iv_imageDecs = (ImageView) findViewById(R.id.imageDes);
                                        Glide.with(this).load(R.drawable.waste_end_pic).into(iv_endPic);
                                        Glide.with(this).load(R.drawable.waste_end_help_pic).into(iv_wasteImge);
                                        iv_imageDecs.setVisibility(View.INVISIBLE);
                                        iv_wasteImge.setVisibility(View.INVISIBLE);
                                        greenBin.setVisibility(View.INVISIBLE);
                                        redBin.setVisibility(View.INVISIBLE);
                                        yellowBin.setVisibility(View.INVISIBLE);
                                        tv_wasteName.setText("");
                                        break;
                                    }
                                } else {
                                    MediaPlayer mp1 = MediaPlayer.create(WasteMainActivity.this, R.raw.wrong);
                                    mp1.start();
                                    sendWrongMsg();
                                    if (score > 0) {
                                        score = score - 5;
                                        tv_userScore.setText("Score: " + score + " / 100");
                                        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake_text);
                                        tv_userScore.startAnimation(shake);
                                        setStarPic(score);
                                    }
                                }
                                break;
                        }
                    } else {
                        iv_endPic.setVisibility(View.VISIBLE);
                        Glide.with(this).load(R.drawable.waste_end_pic).into(iv_endPic);
                        Glide.with(this).load(R.drawable.waste_end_help_pic).into(iv_wasteImge);
                        ImageView iv_imageDecs = (ImageView) findViewById(R.id.imageDes);
                        iv_imageDecs.setVisibility(View.INVISIBLE);
                        iv_wasteImge.setVisibility(View.INVISIBLE);
                        greenBin.setVisibility(View.INVISIBLE);
                        redBin.setVisibility(View.INVISIBLE);
                        yellowBin.setVisibility(View.INVISIBLE);
                        tv_wasteName.setText("");
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    private int getOrientation(float dx, float dy) {

        if (Math.abs(dx) > Math.abs(dy)) {
            //X axis move
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y axis move
            return dy > 0 ? 'b' : 't';
        }
    }

    private void setStarPic(int score) {
        if (score >= 80) {
            Glide.with(this).load(R.drawable.fivestar).into(iv_wasteScore);
        }
        if ((score < 80) && (score >= 60)) {
            Glide.with(this).load(R.drawable.fourstar).into(iv_wasteScore);
        }
        if (score < 60 && score >= 40) {
            Glide.with(this).load(R.drawable.threestar).into(iv_wasteScore);
        }
        if (score < 40 && score >= 20) {
            Glide.with(this).load(R.drawable.twostar).into(iv_wasteScore);
        }
        if (score < 20) {
            Glide.with(this).load(R.drawable.onestar).into(iv_wasteScore);
        }
    }

    /**
     * Clear the WasteMainActivity List
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("SystemSP", MODE_PRIVATE).edit();
        editor.remove("wasteList");
        editor.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendCorrectMsg(String wasteName, String binColor) {
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
        AlertDialog builder = new AlertDialog.Builder(WasteMainActivity.this)
                .setTitle("Great!")
                .setIcon(R.drawable.icon_correct)
                .setMessage("The " + wasteName + " should go to " + binColor + "!")
                .setPositiveButton("Give me next one!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .show();

        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(20);
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(type);

        try {
            // Get mAlert Object
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(builder);

            // Obtain mMessageView object
            // Set size and color
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
            mMessageView.setTextColor(Color.BLACK);
            mMessageView.setTextSize(22);
            mMessageView.setTypeface(type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void sendWrongMsg() {
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
        AlertDialog builder = new AlertDialog.Builder(WasteMainActivity.this)
                .setTitle("Think it carefully ~")
                .setIcon(R.drawable.icon_cross)
                .setPositiveButton("Let me try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setCancelable(false)
                .show();

        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(20);
        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(type);

        try {
            // Get mAlert Object
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(builder);

            // Obtain mMessageView object
            // Set size and color
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
            mMessageView.setTextColor(Color.BLACK);
            mMessageView.setTextSize(22);
            mMessageView.setTypeface(type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
