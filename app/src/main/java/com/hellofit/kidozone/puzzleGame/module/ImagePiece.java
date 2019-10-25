package com.hellofit.kidozone.puzzleGame.module;

import android.graphics.Bitmap;
import android.widget.ImageView;

/***
 *  This class include some methods to deal the image piece
 *
 *  Created by Weiqiang Li on 09/13/19.
 *  Copyright @ 2019 Weiqiang Li. All right reserved
 *
 *  @author Weiqiang Li
 *  @version 3.1
 *
 *  Final modified date: 09/13/2019 by Weiqiang Li
 */

public class ImagePiece {

    private int index;
    private Bitmap bitmap;
    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
