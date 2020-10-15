package com.rtugeek.percentprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
    private double percent;
    private int progressColor;
    private int backgroundColor;
    private Direction direction = Direction.LEFT_TO_RIGHT;
    private ValueAnimator animator = new ValueAnimator();

    private final String KEY_BAR_HEIGHT = "bar_Height";
    private final String KEY_BORDER_SIZE = "border_Size";
    private final String KEY_BORDER_COLOR = "border_Color";
    private final String KEY_BACKGROUND_COLOR = "background_Color";
    private final String KEY_PROGRESS_COLOR = "progress_Color";
    private final String KEY_DIRECTION = "direction";
    private final String KEY_RADIUS = "radius";
    private final String KEY_PERCENT = "percent";
    private final String KEY_STATE = "key_state";

    public enum Direction {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
    }

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
        direction = Direction.values()[a.getInt(R.styleable.PercentProgressBar_direction, 0)];

        int textColor = a.getInteger(R.styleable.PercentProgressBar_textColor, Color.BLACK);
        float textSize = a.getDimensionPixelSize(R.styleable.PercentProgressBar_textSize, 0);

        a.recycle();

        View inflate = LayoutInflater.from(context).inflate(R.layout.progress_bar, this);
        progressBar = inflate.findViewById(R.id.progress);
        label = inflate.findViewById(R.id.tv_label);


        setDirection(direction);
        updateProgressBarDrawable();
        setPercent(percent, false);

        //init label
        if (textSize == 0) {
            textSize = label.getTextSize();
        }
        setTextSize(textSize);
        setTextColor(textColor);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        if (direction == Direction.RIGHT_TO_LEFT) {
            progressBar.setScaleX(-1);
        } else {
            progressBar.setScaleX(1);
        }
        setPercent(percent);
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
        setPercent(0, percent);
    }

    public Direction getDirection() {
        return direction;
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

    /**
     * Use this to show animation
     *
     * @param from start percent
     * @param to   end percent
     */
    public void setPercent(double from, double to) {
        progressBar.setProgress(percentToProgressInt(from));
        setPercent(to, true);
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
        updateProgressBarDrawable();
    }

    private int percentToProgressInt(double percent) {
        int progress = (int) Math.floor(percent * progressBar.getMax());
        progress = Math.max(progress, 0);
        progress = Math.min(progress, progressBar.getMax());
        return progress;
    }


    public void setPercent(double percent, boolean animation) {
        this.percent = percent;
        int progress = percentToProgressInt(percent);
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
        int percentValue = progressValue / 10;
        label.setText(String.format("%d%%", percentValue));
        progressBar.setProgress(progressValue);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_STATE, super.onSaveInstanceState());
        bundle.putInt(KEY_BAR_HEIGHT, barHeight);
        bundle.putInt(KEY_BORDER_SIZE, borderSize);
        bundle.putInt(KEY_RADIUS, radius);
        bundle.putInt(KEY_BORDER_COLOR, borderColor);
        bundle.putInt(KEY_BACKGROUND_COLOR, backgroundColor);
        bundle.putInt(KEY_PROGRESS_COLOR, progressColor);
        bundle.putDouble(KEY_PERCENT, percent);
        bundle.putInt(KEY_DIRECTION, direction.ordinal());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            barHeight = bundle.getInt(KEY_BAR_HEIGHT, barHeight);
            borderSize = bundle.getInt(KEY_BORDER_SIZE, borderSize);
            radius = bundle.getInt(KEY_RADIUS, radius);
            borderColor = bundle.getInt(KEY_BORDER_COLOR, borderColor);
            backgroundColor = bundle.getInt(KEY_BACKGROUND_COLOR, backgroundColor);
            progressColor = bundle.getInt(KEY_PROGRESS_COLOR, progressColor);
            percent = bundle.getDouble(KEY_PERCENT, percent);
            bundle.getInt(KEY_DIRECTION, direction.ordinal());
            super.onRestoreInstanceState(bundle.getParcelable(KEY_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.cancel();
    }

}
