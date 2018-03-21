package com.starunion.progress_lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Discription:
 * Created by sz on 2018/3/21.
 */

public class HfMusicProgressBar extends View {
    private static final String TAG = HfMusicProgressBar.class.getSimpleName();
    private boolean isDebug = true;
    private Paint mPathPaint;
    private Bitmap mBackgroundBitmap;
    private int mProgressBarColor;
    int backgroundRes;
    private int mWidth;
    private int mHeight;
    private int mInitialMotionY;
    private float mInitialMotionX;
    private float mLastDistance;
    private float diffX;
    private Path mPath;
    /**
     * 100分之多少
     */
    private float mProgress;

    OnProgressBarScrollListener mOnProgressBarScrollListener;

    public HfMusicProgressBar(Context context) {
        this(context, null);
    }

    public HfMusicProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HfMusicProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    /**
     * 设置背景图片
     * @param progressBackgroud
     */
    public void setProgressBackgroud(int progressBackgroud){
        mBackgroundBitmap = BitmapFactory.decodeResource(getResources(),progressBackgroud);
    }

    /**
     * 设置进度颜色
     *
     * @param progressBarColor
     */
    public void setProgressBarColor(int progressBarColor){
        mProgressBarColor = progressBarColor;
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(float progress){
        this.mProgress = progress;
        invalidate();
    }

    /**
     * 初始化
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HfMusicProgressBar);
        backgroundRes = a.getResourceId(R.styleable.HfMusicProgressBar_progress_background, 0);
        mProgressBarColor = a.getColor(R.styleable.HfMusicProgressBar_progress_color, Color.BLUE);
        a.recycle();

//        mBackgroundBitmap = getBitmapFromDrawable(getBackground());

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPath = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(mProgressBarColor);
        mPathPaint.setStyle(Paint.Style.FILL);
        mPathPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int bgWidth = mBackgroundBitmap.getWidth();
        // TODO: 2018/3/21 添加左右的滑动距离限制
        //找到这次拖拽的距离和上次拖拽距离的和
        mLastDistance = mLastDistance + diffX;
        float resultWidht = mLastDistance;
        if (isDebug){
            Log.d(TAG, "onDraw: resultWidht="+ resultWidht);
        }

        int i = 0;
        while(resultWidht < mWidth && bgWidth > 0){
            canvas.drawBitmap(mBackgroundBitmap, resultWidht,0,paint);
            resultWidht += bgWidth;
            i++;
        }
        if (isDebug){
            Log.d(TAG, "onDraw: i="+ i);
        }

        //创建进度Path
        mPath.reset();
        mPath.moveTo(0,0);
        mPath.lineTo(mProgress/100*mWidth,0);
        mPath.lineTo(mProgress/100*mWidth, mBackgroundBitmap.getHeight());
        mPath.lineTo(0, mBackgroundBitmap.getHeight());
        mPath.close();

        canvas.drawPath(mPath, mPathPaint);

        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mInitialMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                diffX = event.getX() - mInitialMotionX;
                mInitialMotionX = event.getX();
                Log.d(TAG, "ACTION_MOVE mInitialMotionX="+mInitialMotionX+",diffX="+diffX);
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {

        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

}
