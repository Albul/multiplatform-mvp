package com.olekdia.mvpcore

enum class TaskPriority {
    NONE, LOW, MEDIUM, HIGH
}

enum class TaskFilter {
    ALL, ACTIVE, COMPLETED
}

enum class NumEndingFormat {
    ONE, X1, X4, X5
}

enum class ViewType {
    MAIN, FORM, DIALOG
}

annotation class Key {
    companion object {
        const val NAME = "NAME"
        const val PRIORITY = "PRIORITY"
        const val INITIAL_ENTRY = "INIT_ENTRY"
        const val CURRENT_ENTRY = "CURR_ENTRY"
        const val POS = "POS"
        const val ID = "ID"
        const val CONTENT = "CONTENT"
    }
}

annotation class Param {
    companion object {
        const val SAVE = "SAVE"
        const val DISCARD = "DISCARD"
    }
}