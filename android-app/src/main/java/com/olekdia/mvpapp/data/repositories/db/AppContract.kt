package com.olekdia.mvpapp.data.repositories.db

object AppContract {
    abstract class BaseColumns {
        companion object {
            const val _ID = "_id"
        }
    }


    object Task : BaseColumns() {
        const val TABLE = "task"

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
        const val POS_ = 1
        const val NAME_ = 2
        const val PRIORITY_ = 3
        const val COMPLETED_ = 4
        const val CREATION_DATE_TIME_ = 5

        // Projections
        const val BASE_PROJECTION =
            "$TABLE.$_ID,$_POS,$TABLE.$_NAME,$_PRIORITY,$_COMPLETED,$TABLE.$_CREATION_DATE_TIME"
    }
}