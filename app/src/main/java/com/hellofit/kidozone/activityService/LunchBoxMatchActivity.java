package com.hellofit.kidozone.activityService;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellofit.kidozone.R;
import com.hellofit.kidozone.entity.FoodInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/***
 *  This class include the function of match food and category correctly
 *
 *  Created by Mingzhe Liu on 10/02/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 3.6
 *
 *  Final modified date: 10/17/2019 by Mingzhe Liu
 */
public class LunchBoxMatchActivity extends AppCompatActivity {

    private ArrayList<FoodInfo> foodInfoList;
    private ArrayList<FoodInfo> pickedList;
    private int currentFoodIndex;
    // when question == 0, should be wrong question
    // when question == 1, should be right question
    private int question;
    // when answer == 'l', user make left choice
    // when answer == 'r', user make right choice
    private char answer;

    private MediaPlayer mp;
    private int media_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_box_match);

        // Initialize view component
        final Button btn_backButton = (Button) findViewById(R.id.backButton);
        final Button btn_yes = (Button) findViewById(R.id.btn_lb_match_yes);
        final Button btn_no = (Button) findViewById(R.id.btn_lb_match_no);
        final Button btn_start = (Button) findViewById(R.id.btn_lb_match_start);
        final ImageView iv_foodImage = (ImageView) findViewById(R.id.iv_lb_match_food_pic);
        final ImageView iv_userPickedSum = (ImageView) findViewById(R.id.iv_count_number);
        final ImageView iv_foodType = (ImageView) findViewById(R.id.iv_match_food_type);
        final TextView tv_foodName = (TextView) findViewById(R.id.foodName);

        // Set component invisible at beginning
        btn_yes.setVisibility(View.INVISIBLE);
        btn_no.setVisibility(View.INVISIBLE);
        iv_foodType.setVisibility(View.INVISIBLE);
        iv_foodImage.setVisibility(View.GONE);
        tv_foodName.setText("");

        foodInfoList = new ArrayList<FoodInfo>();
        pickedList = new ArrayList<FoodInfo>();

        mp = MediaPlayer.create(LunchBoxMatchActivity.this, R.raw.lunch_box_intro);
        mp.start();
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        // Load Food data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("SystemSP", MODE_PRIVATE);
        String json = sp.getString("foodList", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FoodInfo>>() {}.getType();
            foodInfoList = gson.fromJson(json, type);
        }

        // On click listener setting
        btn_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
                AlertDialog builder = new AlertDialog.Builder(LunchBoxMatchActivity.this)
                        .setTitle("Oops...")
                        .setIcon(R.drawable.icon_dialog)
                        .setMessage("You really want to quit the game?")
                        .setPositiveButton("Yes, I'm leaving!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(LunchBoxMatchActivity.this, MainActivity.class);
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

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_yes.setVisibility(View.VISIBLE);
                btn_no.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.GONE);
                iv_foodImage.setVisibility(View.VISIBLE);
                iv_foodType.setVisibility(View.VISIBLE);
                // display components
                currentFoodIndex = getRandomNum(0,29);
                setFoodNameAndImage(currentFoodIndex, iv_foodImage, tv_foodName);
                iv_foodImage.startAnimation(shake);
                // answers
                setQuestionImage(currentFoodIndex, iv_foodType);

            }
        });

        // Press the "Yes" button
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer = 'r';
                clickButton(iv_foodImage, tv_foodName, iv_foodType, iv_userPickedSum);
                answer = ' ';
            }
        });

        // Press the "No" button
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer = 'l';
                clickButton(iv_foodImage, tv_foodName, iv_foodType, iv_userPickedSum);
                answer = ' ';
            }
        });
    }

    private void clickButton(ImageView foodImage, TextView foodName, ImageView typeImage, ImageView userPicked) {
        if ((question == 0 && answer == 'l') || (question == 1 && answer == 'r')) {
            //
            if (pickedList.size() < 5) {
                pickedList.add(foodInfoList.get(currentFoodIndex));
                while (true) {
                    currentFoodIndex = getRandomNum(0, 29);
                    if (!pickedList.contains(foodInfoList.get(currentFoodIndex))) {
                        setFoodNameAndImage(currentFoodIndex, foodImage, foodName);
                        setQuestionImage(currentFoodIndex, typeImage);
                        break;
                    }
                }
                setPickedNumImage(userPicked, pickedList.size());
            }
            // Size of pickedList has reached 5, stop the game, going to next stage
            else {
                showFinalAlertDialog();
            }
        } else {
            // Size of pickedList is not enough 6, continue the game
            if (pickedList.size() < 6) {
                showWrongAlertDialog();
                while (true) {
                    currentFoodIndex = getRandomNum(0, 29);
                    if (!pickedList.contains(foodInfoList.get(currentFoodIndex))) {
                        setFoodNameAndImage(currentFoodIndex, foodImage, foodName);
                        setQuestionImage(currentFoodIndex, typeImage);
                        break;
                    }
                }
            }
        }
    }

    private void setPickedNumImage(ImageView userPicked, int size) {
        switch (size) {
            case 0:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_0).into(userPicked);
                break;
            case 1:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_1).into(userPicked);
                break;
            case 2:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_2).into(userPicked);
                break;
            case 3:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_3).into(userPicked);
                break;
            case 4:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_4).into(userPicked);
                break;
            case 5:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_5).into(userPicked);
                break;
            case 6:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_6).into(userPicked);
                break;
            default:
                Glide.with(LunchBoxMatchActivity.this).load(R.drawable.count_number_9).into(userPicked);
                break;
        }
    }

    private void setQuestionImage(int foodIndex, ImageView imageView) {
        if (!foodInfoList.get(foodIndex).getCategoryName().equals("junks")) {
            question = getRandomNum(0, 1);
        } else {
            question = 0;
        }
        if (question == 1) {
            setFoodTypeImage(indexOfFoodCategory(foodInfoList.get(foodIndex).getCategoryName()), imageView);
        } else {
            ArrayList<Integer> temp = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
            temp.remove(indexOfFoodCategory(foodInfoList.get(foodIndex).getCategoryName()));
            setFoodTypeImage(temp.get(getRandomNum(0, temp.size() - 1)), imageView);
        }
    }

    private void showFinalAlertDialog() {
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
        AlertDialog builder = new AlertDialog.Builder(LunchBoxMatchActivity.this)
                .setTitle("Great Work!")
                .setIcon(R.drawable.icon_correct)
                .setMessage("You have made 6 correct now!")
                .setPositiveButton("Go select foods", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LunchBoxMatchActivity.this, LunchBoxSelectActivity.class);
                        startActivity(intent);
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

    private void showWrongAlertDialog() {
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
        AlertDialog builder = new AlertDialog.Builder(LunchBoxMatchActivity.this)
                .setTitle("Oops...")
                .setIcon(R.drawable.icon_cross)
                .setMessage("Think it carefully next time!")
                .setPositiveButton("Try another one", new DialogInterface.OnClickListener() {
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

    private void setFoodTypeImage(int typeNum, ImageView imageView) {
        switch (typeNum) {
            case 0:
                Glide.with(this).load(R.drawable.lunchbox_question_fruit).into(imageView);
                break;
            case 1:
                Glide.with(this).load(R.drawable.lunchbox_question_vegetable).into(imageView);
                break;
            case 2:
                Glide.with(this).load(R.drawable.lunchbox_question_dairy_product).into(imageView);
                break;
            case 3:
                Glide.with(this).load(R.drawable.lunchbox_question_meat).into(imageView);
                break;
            case 4:
                Glide.with(this).load(R.drawable.lunchbox_question_grain).into(imageView);
                break;
            case 5:
                Glide.with(this).load(R.drawable.lunchbox_question_drink).into(imageView);
                break;
        }
    }

    private int indexOfFoodCategory(String categoryName) {
        switch (categoryName) {
            case "fruit":
                return 0;
            case "vegetable":
                return 1;
            case "diary product":
                return 2;
            case "meat":
                return 3;
            case "grain":
                return 4;
            case "drink":
                return 5;
        }
        return 0;
    }

    /**
     *
     * @param index
     */
    private void setFoodNameAndImage(int index, ImageView iv, TextView tv) {
        Glide.with(this).load(foodInfoList.get(index).getFoodImage()).into(iv);
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
        tv.setTypeface(type);
        tv.setText(foodInfoList.get(index).getFoodName());
    }

    /**
     * Create a random number between min and max
     * Both limitations are inclusive
     *
     * @param min low limitation
     * @param max high limitation
     * @return random number
     */
    private int getRandomNum(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
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

