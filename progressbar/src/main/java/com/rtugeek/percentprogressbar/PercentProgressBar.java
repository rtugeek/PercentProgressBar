package com.rtugeek.percentprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
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
    private TextView label;
    private int barHeight;
    private int borderSize;
    private int borderColor;
    private int radius;
    private final int maxProgress = 1000;
    private float percent;
    private int progressColor;
    private int backgroundColor;
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
        percent = a.getFloat(R.styleable.PercentProgressBar_percent, 0);

        int textColor = a.getInteger(R.styleable.PercentProgressBar_textColor, Color.BLACK);
        float textSize = a.getDimensionPixelSize(R.styleable.PercentProgressBar_textSize, 0);

        a.recycle();

        View inflate = LayoutInflater.from(context).inflate(R.layout.progress_bar, this);
        progressBar = inflate.findViewById(R.id.progress);
        label = inflate.findViewById(R.id.tv_label);


        updateProgressBarDrawable();
        setPercent(percent, true);

        //init label
        if (textSize == 0) {
            textSize = label.getTextSize();
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
        setPercent(percent, true);
    }

    public void setRadius(int radiusInPx) {
        this.radius = radiusInPx;
        updateProgressBarDrawable();
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        updateProgressBarDrawable();
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
        updateProgressBarDrawable();
    }

    public void setBarHeight(int heightInPx) {
        barHeight = heightInPx;
        ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
        layoutParams.height = barHeight;
        progressBar.setLayoutParams(layoutParams);
    }

    public int getTextColor() {
        return label.getCurrentTextColor();
    }

    public void setTextColor(int color) {
        label.setTextColor(color);
    }

    public float getTextSize() {
        return label.getTextSize();
    }

    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTextSize(int unit, float size) {
        label.setTextSize(unit, size);
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(label.getTextSize());
        label.setMinWidth((int) textPaint.measureText("100%"));
    }

    public void setPercent(double percent) {
        setPercent(percent, true);
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
        updateProgressBarDrawable();
    }


    public void setPercent(double percent, boolean animation) {
        int progress = (int) Math.floor(percent * maxProgress);
        progress = Math.max(progress, 0);
        progress = Math.min(progress, maxProgress);
        if (animation) {
            animator.setIntValues(progressBar.getProgress(), progress);
            animator.setDuration(1000);
            animator.addUpdateListener(this);
            animator.start();
        } else {
            animator.cancel();
            progressBar.setProgress(progress);
            label.setText(String.format("%.0f%%", percent * 100));
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
        return label;
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int progressValue = (int) animation.getAnimatedValue();
        System.out.println("onAnimationUpdate:" + progressValue);
        int percentValue = progressValue / 10;
        label.setText(String.format("%d%%", percentValue));
        progressBar.setProgress(progressValue);
    }


}
