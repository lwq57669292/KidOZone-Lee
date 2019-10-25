package com.hellofit.kidozone.puzzleGame.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.hellofit.kidozone.R;
import com.hellofit.kidozone.activityService.LunchBoxMatchActivity;
import com.hellofit.kidozone.activityService.MainActivity;

import java.lang.reflect.Field;

/***
 *  This class is the dialog when the user success finish the puzzle
 *
 *  Created by Weiqiang Li on 09/13/19.
 *  Copyright @ 2019 Weiqiang Li. All right reserved
 *
 *  @author Weiqiang Li
 *  @version 3.1
 */

public class SuccessDialog extends DialogFragment {

    public OnButtonClickListener buttonClickListener;

    /**
     * Create the dialog and set the some attributes like message
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "Monaco.ttf");
        androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setTitle("You made it!")
                .setIcon(R.drawable.icon_correct)
                .setMessage("Wanna challenge next level?")
                .setPositiveButton("Go to next level!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (buttonClickListener != null) {
                            buttonClickListener.nextLevelClick();
                        }
                    }
                })
                .setNegativeButton("Let me stay here.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (buttonClickListener != null) {
                            buttonClickListener.cancelClick();
                        }
                    }
                })
                .setCancelable(false)
                .show();

        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextSize(20);
        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTypeface(type);

        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.design_default_color_primary));
        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextSize(20);
        builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTypeface(type);

        try {
            // Get mAlert Object
            Field mAlert = androidx.appcompat.app.AlertDialog.class.getDeclaredField("mAlert");
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

        return builder;

    }

    public void addButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public interface OnButtonClickListener {
        public void nextLevelClick();

        public void cancelClick();
    }
}
