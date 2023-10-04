package com.example.kangai.CustomViews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.kangai.Helpers.ThemedColor;
import com.example.kangai.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeafLoadingBar extends FrameLayout {

    Integer leafs = 9;
    Integer max = 100;
    List<ImageView> leafsList;
    Context context;

    public LeafLoadingBar(@NonNull Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public LeafLoadingBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }

    public LeafLoadingBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
    }

    public LeafLoadingBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.customviews_leafloadingbar, this, true);
        leafsList = new ArrayList<>();
        leafsList.addAll(Arrays.asList(
                findViewById(R.id.leaf1),
                findViewById(R.id.leaf2),
                findViewById(R.id.leaf3),
                findViewById(R.id.leaf4),
                findViewById(R.id.leaf5),
                findViewById(R.id.leaf6),
                findViewById(R.id.leaf7),
                findViewById(R.id.leaf8),
                findViewById(R.id.leaf9)
        ));
        ColorStateList stateList = ThemedColor.getColorStateList(context, R.attr.greyBG);
        for (ImageView leaf : leafsList) leaf.setImageTintList(stateList);
    }

    public void setProgress(int progress) {
        int fill = (int) (leafs * ((float) progress / (float) max));
        ColorStateList stateList = ThemedColor.getColorStateList(context, R.attr.confirm);
        for (int leaf_number = 0; leaf_number < fill; leaf_number++){
            leafsList.get(leaf_number).setImageTintList(stateList);
        }
    }

    public void setMax(int max){
        this.max = max;
    }
}
