package com.dev.hirelink.components

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

@BindingAdapter("selectableButton")
fun setBackgroundTint(button: MaterialButton, colorString: String) {
    val color = Color.parseColor(colorString)
    val colorStateList = ColorStateList.valueOf(color)
    button.backgroundTintList = colorStateList
}

@BindingAdapter("selectedTextColor")
fun changeSelectedButtonTextColor(button: MaterialButton, colorString: String) {
    val color = Color.parseColor(colorString)
    button.setTextColor(color)
}

@BindingAdapter("selectedIconColor")
fun changeSelectedIconColor(button: MaterialButton, colorString: String) {
    val color = Color.parseColor(colorString)
    val icon: Drawable = DrawableCompat.wrap(button.icon)
    DrawableCompat.setTint(icon, color)
}