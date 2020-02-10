package com.olekdia.mvpapp.data.entries.parcels

import android.os.Parcel
import android.os.Parcelable
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpapp.platform.extensions.readEnum
import com.olekdia.mvpapp.platform.extensions.writeEnum
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpapp.platform.extensions.readBooleanCompat
import com.olekdia.mvpapp.platform.extensions.writeBooleanCompat

class TaskParcel(val entry: TaskEntry) : Parcelable {

    constructor(parcel: Parcel) : this(
        TaskEntry(
            parcel.readLong(),
            parcel.readString() ?: "",
            parcel.readEnum<TaskPriority>()
                ?: TaskPriority.NONE,
            parcel.readBooleanCompat(),
            parcel.readLong()
        )
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(entry.id)
        parcel.writeString(entry.name)
        parcel.writeEnum(entry.priority)
        parcel.writeBooleanCompat(entry.isCompleted)
        parcel.writeLong(entry.creationLdt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskParcel> {
        override fun createFromParcel(parcel: Parcel): TaskParcel {
            return TaskParcel(parcel)
        }

        override fun newArray(size: Int): Array<TaskParcel?> {
            return arrayOfNulls(size)
        }
    }
}