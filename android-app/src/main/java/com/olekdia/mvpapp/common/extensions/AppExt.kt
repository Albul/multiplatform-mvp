package com.olekdia.mvpapp.common.extensions

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
import com.olekdia.mvpapp.R
import com.olekdia.mvpapp.data.entries.parcels.TaskParcel
import com.olekdia.mvpcore.domain.entries.TaskEntry

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

fun Array<Pair<String, Any?>>.parcelize(): Array<Pair<String, Any?>> =
    this.apply {
        for (i in this.lastIndex downTo 0) {
            val pair = this[i]
            val item = pair.second
            if (item is TaskEntry) {
                this[i] = pair.first to TaskParcel(item)
            }
        }
    }