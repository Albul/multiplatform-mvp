package com.olekdia.sample.model.repositories.db

object AppContract {
    abstract class BaseColumns {
        companion object {
            const val _ID = "_id"
        }
    }

    object Category : BaseColumns() {
        const val TABLE = "category"

        const val _POS = "pos"
        const val _NAME = "name"
        const val _COLOR = "color"
        const val _EXPANDED = "expanded"
        const val _CREATION_DATE_TIME = "creation_date_time"

        const val ID_ = 0
        const val POS_ = 1
        const val NAME_ = 2
        const val COLOR_ = 3
        const val EXPANDED_ = 4
        const val CREATION_DATE_TIME_ = 5

        // Projections
        const val BASE_PROJECTION =
            "$TABLE.$_ID,$_POS,$TABLE.$_NAME,$_COLOR,$_EXPANDED,$TABLE.$_CREATION_DATE_TIME"
    }


    object Task : BaseColumns() {
        const val TABLE = "task"

        const val _PID = "pid"
        const val _POS = "pos"
        const val _NAME = "name"
        /**
         * Priority of the task (1 - *, 2 - !, 3 - !!)
         * <P>Type: Integer (int)</P>
         */
        const val _PRIORITY = "priority"
        const val _COMPLETED = "completed"
        const val _CREATION_DATE_TIME = "creation_date_time"

        const val ID_ = 0
        const val PID_ = 1
        const val POS_ = 2
        const val NAME_ = 3
        const val PRIORITY_ = 4
        const val COMPLETED_ = 5
        const val CREATION_DATE_TIME_ = 6

        // Projections
        const val BASE_PROJECTION =
            "$TABLE.$_ID,$_PID,$_POS,$TABLE.$_NAME,$_PRIORITY,$_COMPLETED,$TABLE.$_CREATION_DATE_TIME"
    }
}