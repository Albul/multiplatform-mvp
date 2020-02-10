package com.olekdia.mvpapp.platform

import android.content.Context
import com.olekdia.androidcommon.extensions.defaultSharedPreferences
import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvpapp.model.repositories.PrefRepository
import com.olekdia.mvpapp.model.repositories.TaskDbRepository
import com.olekdia.mvpapp.model.repositories.db.DbRepository
import com.olekdia.mvpcore.platform.repositories.IDbRepository
import com.olekdia.mvpapp.platform.managers.SnackManager
import com.olekdia.mvpapp.platform.managers.TextManager
import com.olekdia.mvpapp.platform.managers.ToastManager
import com.olekdia.mvpcore.platform.repositories.IPrefRepository
import com.olekdia.mvpcore.platform.repositories.ITaskDbRepository
import com.olekdia.mvpcore.platform.managers.ISnackManager
import com.olekdia.mvpcore.platform.managers.ITextManager
import com.olekdia.mvpcore.platform.managers.IToastManager

class PlatformComponentFactory(private val context: Context) :
    ComponentFactory<IPlatformComponent>(

        mutableMapOf(
            IDbRepository.COMPONENT_ID to { DbRepository(context) },
            IPrefRepository.COMPONENT_ID to { PrefRepository(context.defaultSharedPreferences) },
            IToastManager.COMPONENT_ID to { ToastManager(context) },
            ISnackManager.COMPONENT_ID to { SnackManager() },
            ITextManager.COMPONENT_ID to { TextManager(context) },

            ITaskDbRepository.COMPONENT_ID to TaskDbRepository.Companion
        )
    )