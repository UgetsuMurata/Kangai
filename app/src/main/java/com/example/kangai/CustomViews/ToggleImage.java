package com.example.kangai.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.kangai.R;

public class ToggleImage extends LinearLayout {

    Context context;
    state currentState = state.open;
    Integer activeImage, inactiveImage;
    ImageView imageView;

    private enum state{
        open, close
    }

    public ToggleImage(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public ToggleImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public ToggleImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.customviews_toggle_image, this);
        this.imageView = findViewById(R.id.image);

        if (attrs != null) {

            int initialState = attrs.getAttributeIntValue(null, "initialState", 1);
            if (initialState == 0) currentState = state.close;
            else currentState = state.open;

            activeImage = attrs.getAttributeResourceValue(null, "activeImage", R.drawable.active_notif);
            inactiveImage = attrs.getAttributeResourceValue(null, "inactiveImage", R.drawable.inactive_notif);

            if (isActive())
                imageView.setImageDrawable(ContextCompat.getDrawable(context, activeImage));
            else imageView.setImageDrawable(ContextCompat.getDrawable(context, inactiveImage));
        } else {
            currentState = state.open;
            activeImage = R.drawable.active_notif;
            inactiveImage = R.drawable.inactive_notif;
            imageView.setImageDrawable(ContextCompat.getDrawable(context, activeImage));
        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isActive()){
                    currentState = state.close;
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, inactiveImage));
                } else {
                    currentState = state.open;
                    imageView.setImageDrawable(ContextCompat.getDrawable(context, activeImage));
                }
            }
        });
    }

    public Boolean isActive() { return currentState == state.open; }
    public void setActive(){
        currentState = state.open;
        imageView.setImageDrawable(ContextCompat.getDrawable(context, activeImage));
    }
    public void setInactive(){
        currentState = state.close;
        imageView.setImageDrawable(ContextCompat.getDrawable(context, inactiveImage));
    }
}
