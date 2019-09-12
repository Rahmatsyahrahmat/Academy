package com.rahmatsyah.academy.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.rahmatsyah.academy.R;

public class MyButton extends AppCompatButton {

    private Drawable enabledBackground, disabledBackground;
    private int textColor;

    public MyButton(Context context){
        super(context);
        init();
    }
    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Resources resources = getResources();
        enabledBackground = resources.getDrawable(R.drawable.bg_button);
        disabledBackground = resources.getDrawable(R.drawable.bg_button_disable);
        textColor = ContextCompat.getColor(getContext(),android.R.color.background_light);
    }

}
