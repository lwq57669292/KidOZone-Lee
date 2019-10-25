package com.hellofit.kidozone.puzzleGame.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hellofit.kidozone.R;
import com.hellofit.kidozone.puzzleGame.gameUtils.Utils;
import com.hellofit.kidozone.puzzleGame.module.ImagePiece;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/***
 *  This class set the layout for the puzzle game view
 *
 *  Created by Weiqiang Li on 09/13/19.
 *  Copyright @ 2019 Weiqiang Li. All right reserved
 *
 *  @author Weiqiang Li
 *  @version 3.1
 *
 *  Final modified date: 10/13/2019 by Weiqiang Li
 */

public class PuzzleLayout extends FrameLayout implements View.OnClickListener {

    private static final int DEFAULT_MARGIN = 3;

    private int mViewWidth = 0;

    //number of picture in one line
    private int mCount = 2;

    private int mItemWidth;

    private List<ImagePiece> mImagePieces;

    private FrameLayout.LayoutParams layoutParams;

    private Bitmap mBitmap;

    private RelativeLayout mAnimLayout;

    private int mMargin;

    private int mPadding;

    private ImageView mFirst;

    private ImageView mSecond;

    private boolean isAddAnimatorLayout = false;

    private boolean isAnimation = false;

    // Default picture of puzzle Game
    private int res = R.drawable.puzzle_default;

    public PuzzleLayout(Context context) {
        this(context, null);
    }

    public PuzzleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initBitmaps();
        initBitmapsWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ImageView) {
                ImageView imageView = (ImageView) getChildAt(i);
                imageView.layout(imageView.getLeft(), imageView.getTop(), imageView.getRight(), imageView.getBottom());
            } else {
                RelativeLayout relativeLayout = (RelativeLayout) getChildAt(i);
                relativeLayout.layout(0, 0, mViewWidth, mViewWidth);
            }
        }
    }

    /*
     *
     * @param context
     */
    private void init(Context context) {
        mMargin = Utils.dp2px(context, DEFAULT_MARGIN);
        mViewWidth = Utils.getScreenWidth(context)[0];
        mPadding = Utils.getMinLength(getPaddingBottom(), getPaddingLeft(), getPaddingRight(), getPaddingTop());
        mItemWidth = (mViewWidth - mPadding * 2 - mMargin * (mCount - 1)) / mCount;
    }

    /**
     * spilt the image
     */
    private void initBitmaps() {
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), res);
        }
        mImagePieces = Utils.splitImage(getContext(), mBitmap, mCount);
        sortImagePieces();
    }

    /**
     *Sort the ImagePieces
     */
    private void sortImagePieces() {
        try {
            Collections.sort(mImagePieces, new Comparator<ImagePiece>() {
                @Override
                public int compare(ImagePiece lhs, ImagePiece rhs) {
                    return Math.random() > 0.5 ? 1 : -1;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set size of image and the attribute of layout
     */
    private void initBitmapsWidth() {
        int line = 0;
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < mImagePieces.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(mImagePieces.get(i).getBitmap());
            layoutParams = new LayoutParams(mItemWidth, mItemWidth);
            imageView.setLayoutParams(layoutParams);
            if (i != 0 && i % mCount == 0) {
                line++;
            }
            if (i % mCount == 0) {
                left = i % mCount * mItemWidth;
            } else {
                left = i % mCount * mItemWidth + (i % mCount) * mMargin;
            }
            top = mItemWidth * line + line * mMargin;
            right = left + mItemWidth;
            bottom = top + mItemWidth;
            imageView.setRight(right);
            imageView.setLeft(left);
            imageView.setBottom(bottom);
            imageView.setTop(top);
            imageView.setId(i);
            imageView.setOnClickListener(this);
            mImagePieces.get(i).setImageView(imageView);
            addView(imageView);
        }
    }


    public void reset() {
        mItemWidth = (mViewWidth - mPadding * 2 - mMargin * (mCount - 1)) / mCount;
        if (mImagePieces != null) {
            mImagePieces.clear();
        }
        isAddAnimatorLayout = false;
        mBitmap = null;
        removeAllViews();
        initBitmaps();
        initBitmapsWidth();
    }

    /**
     * Add count maximum is 4
     */
    public boolean addCount() {
        mCount++;
        if (mCount > 4) {
            mCount--;
            return false;
        }
        reset();
        return true;
    }

    /**
     * change image
     */
    public void changeRes(int res) {
        this.res = res;
        reset();
    }

    /**
     * reduce count at least 2 in line
     */
    public boolean reduceCount() {
        mCount--;
        if (mCount < 2) {
            mCount++;
            return false;
        }
        reset();
        return true;
    }


    @Override
    public void onClick(View v) {
        if (isAnimation) {

            return;
        }
        if (!(v instanceof ImageView)) {
            return;
        }

        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        if (mFirst == null) {
            mFirst = (ImageView) v;

            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
        } else {
            mSecond = (ImageView) v;
            exChangeView();
        }

    }

    private ImageView addAnimationImageView(ImageView imageView) {
        ImageView getImage = new ImageView(getContext());
        RelativeLayout.LayoutParams firstParams = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        firstParams.leftMargin = imageView.getLeft() - mPadding;
        firstParams.topMargin = imageView.getTop() - mPadding;
        Bitmap firstBitmap = mImagePieces.get(imageView.getId()).getBitmap();
        getImage.setImageBitmap(firstBitmap);
        getImage.setLayoutParams(firstParams);
        mAnimLayout.addView(getImage);
        return getImage;
    }

    /**
     * animation view add
     */
    private void exChangeView() {


        setUpAnimLayout();

        ImageView first = addAnimationImageView(mFirst);

        ImageView second = addAnimationImageView(mSecond);

        ObjectAnimator secondXAnimator = ObjectAnimator.ofFloat(second, "TranslationX", 0f, -(mSecond.getLeft() - mFirst.getLeft()));
        ObjectAnimator secondYAnimator = ObjectAnimator.ofFloat(second, "TranslationY", 0f, -(mSecond.getTop() - mFirst.getTop()));
        ObjectAnimator firstXAnimator = ObjectAnimator.ofFloat(first, "TranslationX", 0f, mSecond.getLeft() - mFirst.getLeft());
        ObjectAnimator firstYAnimator = ObjectAnimator.ofFloat(first, "TranslationY", 0f, mSecond.getTop() - mFirst.getTop());
        AnimatorSet secondAnimator = new AnimatorSet();
        secondAnimator.play(secondXAnimator).with(secondYAnimator).with(firstXAnimator).with(firstYAnimator);
        secondAnimator.setDuration(300);

        final ImagePiece firstPiece = mImagePieces.get(mFirst.getId());
        final ImagePiece secondPiece = mImagePieces.get(mSecond.getId());
        final Bitmap firstBitmap = mImagePieces.get(mFirst.getId()).getBitmap();
        final Bitmap secondBitmap = mImagePieces.get(mSecond.getId()).getBitmap();
        secondAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                int fristIndex = firstPiece.getIndex();
                int secondeIndex = secondPiece.getIndex();
                if (mFirst != null) {
                    mFirst.setColorFilter(null);
                    mFirst.setVisibility(VISIBLE);
                    mFirst.setImageBitmap(secondBitmap);
                    firstPiece.setBitmap(secondBitmap);
                    firstPiece.setIndex(secondeIndex);
                }
                if (mSecond != null) {
                    mSecond.setVisibility(VISIBLE);
                    mSecond.setImageBitmap(firstBitmap);
                    secondPiece.setBitmap(firstBitmap);
                    secondPiece.setIndex(fristIndex);
                }

                mAnimLayout.removeAllViews();
                mAnimLayout.setVisibility(GONE);
                mFirst = null;
                mSecond = null;
                isAnimation = false;
                invalidate();
                if (checkSuccess()) {
                    Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                    if (mSuccessListener != null) {
                        mSuccessListener.success();
                    }
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimation = true;
                mAnimLayout.setVisibility(VISIBLE);
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }
        });
        secondAnimator.start();
    }

    /**
     * set animation click
     */
    private void setUpAnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
        }
        if (!isAddAnimatorLayout) {
            isAddAnimatorLayout = true;
            addView(mAnimLayout);
        }
    }

    /**
     * check success or not
     */
    private boolean checkSuccess() {

        boolean isSuccess = true;
        for (int i = 0; i < mImagePieces.size(); i++) {
            ImagePiece imagePiece = mImagePieces.get(i);
            if (i != imagePiece.getIndex()) {
                isSuccess = false;
            }
        }
        return isSuccess;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getRes() {
        return res;
    }

    public int getCount() {
        return mCount;
    }

    private SuccessListener mSuccessListener;

    public void addSuccessListener(SuccessListener successListener) {
        this.mSuccessListener = successListener;
    }

    public interface SuccessListener {
        public void success();
    }
}
