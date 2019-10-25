package com.hellofit.kidozone.puzzleGame.game;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hellofit.kidozone.R;
import com.hellofit.kidozone.activityService.WasteMainActivity;
import com.hellofit.kidozone.puzzleGame.ui.PuzzleLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

/***
 *  This class is the puzzle game function class
 *
 *  Created by Weiqiang Li on 09/13/19.
 *  Copyright @ 2019 Weiqiang Li. All right reserved
 *
 *  @author Weiqiang Li
 *  @version 3.5
 *
 *  Final modified date: 10/15/2019 by Mingzhe Liu
 */

public class PuzzleGame implements Game, PuzzleLayout.SuccessListener {

    private PuzzleLayout puzzleLayout;
    private GameStateListener stateListener;
    private Context context;

    public void addGameStateListener(GameStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public PuzzleGame(@NonNull Context context, @NonNull PuzzleLayout puzzleLayout) {
        this.context = context.getApplicationContext();
        this.puzzleLayout = puzzleLayout;
        puzzleLayout.addSuccessListener(this);
    }

    private boolean checkNull() {
        return puzzleLayout == null;
    }

    /**
     * Add level
     */
    @Override
    public void addLevel() {
        if (checkNull()) {
            return;
        }
        if (!puzzleLayout.addCount()) {
            Toast.makeText(context, "Is the highest level.", Toast.LENGTH_SHORT).show();
        }
        if (stateListener != null) {
            stateListener.setLevel(getLevel());
        }
    }

    /**
     * Reduce level
     */
    @Override
    public void reduceLevel() {
        if (checkNull()) {
            return;
        }
        if (!puzzleLayout.reduceCount()) {
            Toast.makeText(context,"Is the lowest level.", Toast.LENGTH_SHORT).show();
        }
        if (stateListener != null) {
            stateListener.setLevel(getLevel());
        }
    }

    /**
     * Change image
     *
     * @param res
     */
    @Override
    public void changeImage(int res) {
        puzzleLayout.changeRes(res);
    }

    public int getLevel() {
        if (checkNull()) {
            return 0;
        }
        int count = puzzleLayout.getCount();
        return count - 3 + 1;
    }


    @Override
    public void success() {
        if (stateListener != null) {
            stateListener.gameSuccess(getLevel());
        }
    }

    public interface GameStateListener {
        public void setLevel(int level);

        public void gameSuccess(int level);
    }
}
