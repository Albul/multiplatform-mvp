package com.olekdia.mvpapp.platform.extensions

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.widget.CheckBox
import androidx.core.graphics.drawable.DrawableCompat
import com.olekdia.androidcommon.extensions.getDrawableCompat
import com.olekdia.common.extensions.toBoolean
import com.olekdia.common.extensions.toInt
import com.olekdia.mvpapp.NumEndingFormat
import com.olekdia.mvpapp.R

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

fun ContentValues.put(key: String, enum: Enum<*>) {
    put(key, enum.ordinal)
}

inline fun <reified T : Enum<T>> Cursor.getEnum(columnIndex: Int): T =
    getInt(columnIndex).let { enumValues<T>()[it] }

fun Parcel.writeEnum(enum: Enum<*>) {
    writeString(enum.name)
}

inline fun <reified T : Enum<T>> Parcel.readEnum(): T? =
    readString()?.let { enumValueOf<T>(it) }

fun Parcel.writeBooleanCompat(value: Boolean) {
    writeInt(value.toInt())
}

fun Parcel.readBooleanCompat(): Boolean =
    readInt().toBoolean()

// move todo
fun Int.getNumEndingFormat(): NumEndingFormat =
    when {
        this == 1 -> NumEndingFormat.ONE
        this.hasLast1Digit() -> NumEndingFormat.X1
        this.hasLast234Digit() -> NumEndingFormat.X4
        else -> NumEndingFormat.X5
    }

fun Int.hasLast1Digit(): Boolean =
    this > 20 && this % 10 == 1

fun Int.hasLast234Digit(): Boolean =
    (this % 10).let { rest10 ->
        this in 2..4 || rest10 in 2..4 && this > 20
    }