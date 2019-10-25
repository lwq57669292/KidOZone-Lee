package com.hellofit.kidozone.activityService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hellofit.kidozone.R;
import com.hellofit.kidozone.entity.FoodInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

/***
 *  This class is show the lunchbox result
 *
 *  Created by Mingzhe Liu on 10/02/19.
 *  Copyright @ 2019 Mingzhe Liu. All right reserved
 *
 *  @author Mingzhe Liu
 *  @version 3.6
 *
 *  Final modified date: 10/17/2019 by Mingzhe Liu
 */

public class LunchBoxResultActivity extends AppCompatActivity {

    private int score;

    private ArrayList<FoodInfo> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_result);

        Button backButton = (Button) findViewById(R.id.backButton);

        resultList = new ArrayList<FoodInfo>();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(LunchBoxResultActivity.this, MainActivity.class);
            startActivityForResult(intent, 1);
            }
        });

        ImageView imageView1 = (ImageView) findViewById(R.id.imageFood1);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageFood2);
        ImageView imageView3 = (ImageView) findViewById(R.id.imageFood3);
        ImageView imageView4 = (ImageView) findViewById(R.id.imageFood4);
        ImageView imageView5 = (ImageView) findViewById(R.id.imageFood5);
        ImageView imageView6 = (ImageView) findViewById(R.id.imageFood6);

        // Load Food data from SharedPreferences
        SharedPreferences sp = getSharedPreferences("SystemSP", MODE_PRIVATE);
        String json = sp.getString("pickedList", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FoodInfo>>() {}.getType();
            resultList = gson.fromJson(json, type);
        }

        switch (resultList.size()) {
            case 1:
                score = 19;
                Glide.with(this).load(resultList.get(0).getFoodImage()).into(imageView1);
                if (resultList.get(0).getCategoryName().equals("junks")) {
                    score = score - 10;
                }
                break;
            case 2:
                score = 39;
                Glide.with(this).load(resultList.get(0).getFoodImage()).into(imageView1);
                Glide.with(this).load(resultList.get(1).getFoodImage()).into(imageView4);
                for (int i = 0; i < resultList.size(); i++) {
                    if (resultList.get(i).getCategoryName().equals("junks")) {
                        score = score - 10;
                    }
                    for (int j = i + 1; j < resultList.size(); j++) {
                        if (resultList.get(i).getCategoryName().equals(resultList.get(j).getCategoryName())) {
                            score = score - 10;
                        }
                    }
                }
                break;
            case 3:
                score = 59;
                Glide.with(this).load(resultList.get(0).getFoodImage()).into(imageView1);
                Glide.with(this).load(resultList.get(1).getFoodImage()).into(imageView4);
                Glide.with(this).load(resultList.get(2).getFoodImage()).into(imageView2);
                for (int i = 0; i < resultList.size(); i++) {
                    if (resultList.get(i).getCategoryName().equals("junks")) {
                        score = score - 10;
                    }
                    for (int j = i + 1; j < resultList.size(); j++) {
                        if (resultList.get(i).getCategoryName().equals(resultList.get(j).getCategoryName())) {
                            score = score - 10;
                        }
                    }
                }
                break;
            case 4:
                score = 79;
                Glide.with(this).load(resultList.get(0).getFoodImage()).into(imageView1);
                Glide.with(this).load(resultList.get(1).getFoodImage()).into(imageView4);
                Glide.with(this).load(resultList.get(2).getFoodImage()).into(imageView2);
                Glide.with(this).load(resultList.get(3).getFoodImage()).into(imageView5);
                for (int i = 0; i < resultList.size(); i++) {
                    if (resultList.get(i).getCategoryName().equals("junks")) {
                        score = score - 10;
                    }
                    for (int j = i + 1; j < resultList.size(); j++) {
                        if (resultList.get(i).getCategoryName().equals(resultList.get(j).getCategoryName())) {
                            score = score - 10;
                        }
                    }
                }
                break;
            case 5:
                score = 80;
                Glide.with(this).load(resultList.get(0).getFoodImage()).into(imageView1);
                Glide.with(this).load(resultList.get(1).getFoodImage()).into(imageView4);
                Glide.with(this).load(resultList.get(2).getFoodImage()).into(imageView2);
                Glide.with(this).load(resultList.get(3).getFoodImage()).into(imageView5);
                Glide.with(this).load(resultList.get(4).getFoodImage()).into(imageView3);
                for (int i = 0; i < resultList.size(); i++) {
                    if (resultList.get(i).getCategoryName().equals("junks")) {
                        score = score - 10;
                    }
                    for (int j = i + 1; j < resultList.size(); j++) {
                        if (resultList.get(i).getCategoryName().equals(resultList.get(j).getCategoryName())) {
                            score = score - 10;
                        }
                    }
                }
                break;
            case 6:
                score = 100;
                Glide.with(this).load(resultList.get(0).getFoodImage()).into(imageView1);
                Glide.with(this).load(resultList.get(1).getFoodImage()).into(imageView4);
                Glide.with(this).load(resultList.get(2).getFoodImage()).into(imageView2);
                Glide.with(this).load(resultList.get(3).getFoodImage()).into(imageView5);
                Glide.with(this).load(resultList.get(4).getFoodImage()).into(imageView3);
                Glide.with(this).load(resultList.get(5).getFoodImage()).into(imageView6);
                List<String> newList3 = new ArrayList<String>();
                for (int i = 0; i < resultList.size(); i++) {
                    if (resultList.get(i).getCategoryName().equals("junks")) {
                        score = score - 10;
                    }
                    for (int j = i + 1; j < resultList.size(); j++) {
                        if (resultList.get(i).getCategoryName().equals(resultList.get(j).getCategoryName())) {
                            score = score - 10;
                        }
                    }
                }
                break;
        }

        ImageView iv_result = (ImageView) findViewById(R.id.imageResult);
        TextView textView = (TextView) findViewById(R.id.textResultExplain);
        TextView tv = (TextView) findViewById(R.id.textView5);
        Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), "Monaco.ttf");
        textView.setTextSize(25);
        tv.setTextSize(25);
        tv.setTypeface(type);

        if (score >= 80) {
            Glide.with(this).load(R.drawable.fivestar).into(iv_result);
            textView.setText(("Great lunchbox!"));
            textView.setTypeface(type);
        }
        if ((score < 80) && (score >= 60)) {
            Glide.with(this).load(R.drawable.fourstar).into(iv_result);
            textView.setText(("Good, maybe better next time."));
            textView.setTypeface(type);
        }
        if (score < 60 && score >= 40) {
            Glide.with(this).load(R.drawable.threestar).into(iv_result);
            textView.setText(("Good, maybe better next time."));
            textView.setTypeface(type);
        }
        if (score < 40 && score >= 20) {
            Glide.with(this).load(R.drawable.twostar).into(iv_result);
            textView.setText(("You can have a more healthy lunchbox."));
            textView.setTypeface(type);
        }
        if (score < 20) {
            Glide.with(this).load(R.drawable.onestar).into(iv_result);
            textView.setText(("You can have a more healthy lunchbox."));
            textView.setTypeface(type);
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

}