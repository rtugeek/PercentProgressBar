package com.rtugeek.percentprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Jack Fu <rtugeek@gmail.com>
 * @date 2020/10/10
 */
public class PercentProgressBar extends RelativeLayout implements ValueAnimator.AnimatorUpdateListener {
    private android.widget.ProgressBar progressBar;
    private TextView mLabel;
    private int barHeight;
    private int borderSize;
    private int borderColor;
    private int radius;
    private int max;
    private int progress;
    private int progressColor;
    private int backgroundColor;
    private boolean remainMode;
    private ValueAnimator animator = new ValueAnimator();

    public PercentProgressBar(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public PercentProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public PercentProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public PercentProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        //get attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PercentProgressBar, defStyleAttr, defStyleRes);
        barHeight = a.getDimensionPixelSize(R.styleable.PercentProgressBar_barHeight, 0);
        borderSize = a.getDimensionPixelSize(R.styleable.PercentProgressBar_borderSize, 1);
        radius = a.getDimensionPixelSize(R.styleable.PercentProgressBar_radius, 0);
        borderColor = a.getInteger(R.styleable.PercentProgressBar_borderColor, Color.WHITE);
        backgroundColor = a.getInteger(R.styleable.PercentProgressBar_backgroundColor, Color.WHITE);
        progressColor = a.getColor(R.styleable.PercentProgressBar_progressColor, 0xff649FF7);
        progress = a.getInteger(R.styleable.PercentProgressBar_progress, 0);
        max = a.getInteger(R.styleable.PercentProgressBar_max, 100);
        remainMode = a.getBoolean(R.styleable.PercentProgressBar_remainMode, true);

        int textColor = a.getInteger(R.styleable.PercentProgressBar_textColor, Color.BLACK);
        float textSize = a.getDimensionPixelSize(R.styleable.PercentProgressBar_textSize, 0);

        a.recycle();

        View inflate = LayoutInflater.from(context).inflate(R.layout.progress_bar, this);
        progressBar = inflate.findViewById(R.id.progress);
        mLabel = inflate.findViewById(R.id.tv_label);


        updateProgressBarDrawable();
        setProgress(progress);
        setRemainMode(remainMode);

        //init label
        if (textSize == 0) {
            textSize = mLabel.getTextSize();
        }
        setTextSize(textSize);
        setTextColor(textColor);
    }

    private void updateProgressBarDrawable() {

        //init bg
        GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(radius);
        bg.setColor(backgroundColor);
        bg.setStroke(borderSize, borderColor);

        //
        GradientDrawable fg = new GradientDrawable();
        fg.setCornerRadius(radius);
        fg.setColor(progressColor);
        ClipDrawable clipDrawable = new ClipDrawable(fg, Gravity.START, ClipDrawable.HORIZONTAL);

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{bg, clipDrawable});

        if (borderSize > 0) {
            layerDrawable.setLayerInset(1, borderSize, borderSize, borderSize, borderSize);
        }

        if (barHeight > 0) {
            setBarHeight(barHeight);
        }

        progressBar.setProgressDrawable(layerDrawable);
        setProgress(progress,true);
    }

    public void setRadius(int radiusInPx) {
        this.radius = radiusInPx;
        updateProgressBarDrawable();
    }

    public void setRemainMode(boolean remainMode) {
        this.remainMode = remainMode;
        if (this.remainMode) {
            progressBar.setScaleX(-1);
        } else {
            progressBar.setScaleX(1);
        }

        setProgress(progress);
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        updateProgressBarDrawable();
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        updateProgressBarDrawable();
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setBarHeight(int heightInPx) {
        barHeight = heightInPx;
        ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
        layoutParams.height = barHeight;
        progressBar.setLayoutParams(layoutParams);
    }

    public int getTextColor() {
        return mLabel.getCurrentTextColor();
    }

    public void setTextColor(int color) {
        mLabel.setTextColor(color);
    }

    public float getTextSize() {
        return mLabel.getTextSize();
    }

    public void setTextSize(float size) {
        mLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTextSizeInSp(float size) {
        mLabel.setTextSize(size);
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
        updateProgressBarDrawable();
    }


    public void setProgress(int progress, boolean animation) {
        this.progress = progress;
        this.progress = Math.max(this.progress, 0);
        this.progress = Math.min(this.progress, max);
        if (animation) {
            animator.setIntValues(remainMode ? max : 0, progress);
            animator.setDuration(1000);
            animator.addUpdateListener(this);
            animator.start();
        } else {
            animator.cancel();
            progressBar.setProgress(this.progress);
        }
    }



    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        updateProgressBarDrawable();
    }

    public android.widget.ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getLabel() {
        return mLabel;
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue();
        mLabel.setText(value + "%");
        progressBar.setProgress(value);
    }




}
