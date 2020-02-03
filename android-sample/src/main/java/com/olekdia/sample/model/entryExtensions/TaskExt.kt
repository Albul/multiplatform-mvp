package com.olekdia.sample.model.entryExtensions

import com.olekdia.sample.common.AppColors
import com.olekdia.sample.model.entries.TaskEntry

val TaskEntry.priorityColor: Int
    get() = AppColors.priorityColors[priority]