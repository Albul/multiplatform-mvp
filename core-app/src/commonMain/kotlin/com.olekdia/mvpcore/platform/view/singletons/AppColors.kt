package com.olekdia.mvpcore.platform.view.singletons

import kotlin.jvm.JvmField

object AppColors {

    @JvmField
    val lowPriority: Int = 0xFF53856D.toInt()
    @JvmField
    val mediumPriority: Int = 0xFFBFA632.toInt()
    @JvmField
    val highPriority: Int = 0xFF831347.toInt()
    @JvmField
    val priorityColors: IntArray = intArrayOf(
        0xFF757575.toInt(),
        lowPriority,
        mediumPriority,
        highPriority
    )

    @JvmField
    val primText: Int = 0xFF212121.toInt()
    @JvmField
    val primLight: Int = 0xFFFFFFFF.toInt()
    @JvmField
    val secLight: Int = 0xFFF9F9FA.toInt()
}