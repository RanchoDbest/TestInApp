package in.test.rachana.views;

import android.widget.ImageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import in.test.rachana.R;


public class RatioImageView extends android.support.v7.widget.AppCompatImageView {
    private float ratio = 1.0f;
    public RatioImageView(Context context) {
        super(context);
    }

    public RatioImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        if (typedArray != null){
            ratio = typedArray.getFloat(R.styleable.RatioImageView_ratio, 1.0f);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int ratioHeight = (int)(width * ratio);
        setMeasuredDimension(width, ratioHeight);
    }
}
