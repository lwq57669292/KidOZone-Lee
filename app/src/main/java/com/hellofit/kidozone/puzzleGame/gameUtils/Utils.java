package com.hellofit.kidozone.puzzleGame.gameUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.hellofit.kidozone.puzzleGame.module.ImagePiece;

import java.util.ArrayList;
import java.util.List;

/***
 *  This class include some function method like get the screen width and split the image
 *
 *  Created by Weiqiang Li on 09/13/19.
 *  Copyright @ 2019 Weiqiang Li. All right reserved
 *
 *  @author Weiqiang Li
 *  @version 3.1
 *
 *  Final modified date: 09/13/2019 by Weiqiang Li
 */

public class Utils {

    /**
     * return the screen width and height
     * 0.width  1.height
     *
     * @param context
     * @return
     */
    public static int[] getScreenWidth(Context context) {
        context = context.getApplicationContext();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        int[] size = new int[2];
        size[0] = width;
        size[1] = height;
        return size;
    }

    /**
     * trans a bitmap return a imagePiece
     *
     * @param bitmap
     * @param count
     * @return
     */
    public static List<ImagePiece> splitImage(Context context, Bitmap bitmap, int count) {

        List<ImagePiece> imagePieces = new ArrayList<>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int picWidth = Math.min(width, height) / count;

        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * count);
                int x = j * picWidth;
                int y = i * picWidth;
                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y, picWidth, picWidth));
                imagePieces.add(imagePiece);
            }
        }
        return imagePieces;
    }

    /**
     * read the image and return bitmap object
     * <p>
     *
     * @param scale
     * @return Bitmap
     */
    public synchronized static Bitmap readBitmap(Context context, int res, int scale) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeResource(context.getResources(), res, options);
        } catch (Exception e) {
            return null;
        }
    }

    public static int getMinLength(int... params) {
        int min = params[0];
        for (int para : params) {
            if (para < min) {
                min = para;
            }
        }
        return min;
    }

    //dp px
    public static int dp2px(Context context, int dpval) {
        context = context.getApplicationContext();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpval, context.getResources().getDisplayMetrics());
    }
}
