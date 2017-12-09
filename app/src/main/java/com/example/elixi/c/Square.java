package com.example.elixi.c;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

/**
 * Created by Shmulik on 12 נובמבר 2017.
 */

public class Square extends View {

    private static final int SQUARE_SIZE_DEF = 200;

    static Rect mRectSquare;
    private Paint mPaintSquare;

    private int mSquareColor;
    private int mSquareSize;

    public Square(Context context) {
        super(context);

        init(null);
    }

    public Square(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public Square(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public Square(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mRectSquare = new Rect();
        mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (set == null)
            return;


    }

    public void swapColor() {
        mPaintSquare.setColor(mPaintSquare.getColor() == mSquareColor ? Color.RED : mSquareColor);

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRectSquare = new Rect();

       /* mPaintSquare = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectSquare.left = getLeft()+(getRight()-getLeft())/3;
        mRectSquare.top =  getTop()+(getBottom()-getTop())/3;
        mRectSquare.right = getRight()-(getRight()-getLeft())/3;
        mRectSquare.bottom =  getBottom()-(getBottom()-getTop())/3;
        mPaintSquare.setColor(Color.BLACK);
        mPaintSquare.setStyle(Paint.Style.STROKE);
        mPaintSquare.setStrokeWidth(10);
        canvas.drawRect(mRectSquare, mPaintSquare);*/
    }
}
