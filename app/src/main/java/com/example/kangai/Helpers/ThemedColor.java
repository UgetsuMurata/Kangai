package com.example.kangai.Helpers;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;

public class ThemedColor {
    public static ColorStateList getColorStateList(Context context, int attr){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return ColorStateList.valueOf(context.getResources().getColor(typedValue.resourceId));
    }
}
