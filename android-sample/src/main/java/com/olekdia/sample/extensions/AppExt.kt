package com.olekdia.sample.extensions

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.CheckBox
import androidx.core.graphics.drawable.DrawableCompat
import com.olekdia.androidcommon.extensions.getDrawableCompat
import com.olekdia.sample.R

@SuppressLint("PrivateResource")
fun CheckBox.setTint(color: Int) {
    val scl = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        ), intArrayOf(
            color,
            color
        )
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.buttonTintList = scl
    } else {
        this.context.getDrawableCompat(R.drawable.abc_btn_check_material)
            ?.let { d ->
                val wd: Drawable = DrawableCompat.wrap(d)
                DrawableCompat.setTintList(wd, scl)
                this.buttonDrawable = wd
            }
    }
}