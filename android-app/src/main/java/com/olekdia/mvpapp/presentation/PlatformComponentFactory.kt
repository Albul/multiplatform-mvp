package com.olekdia.mvpapp.presentation

import android.content.Context
import com.olekdia.androidcommon.extensions.defaultSharedPreferences
import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.platform.IPlatformComponent
import com.olekdia.mvpapp.data.repositories.PrefsRepository
import com.olekdia.mvpapp.data.repositories.TaskDbRepository
import com.olekdia.mvpapp.data.repositories.db.DbRepository
import com.olekdia.mvpcore.domain.repositories.IDbRepository
import com.olekdia.mvpapp.presentation.managers.SnackManager
import com.olekdia.mvpapp.presentation.managers.TextManager
import com.olekdia.mvpapp.presentation.managers.ToastManager
import com.olekdia.mvpcore.domain.repositories.IPrefsRepository
import com.olekdia.mvpcore.domain.repositories.ITaskDbRepository
import com.olekdia.mvpcore.presentation.managers.ISnackManager
import com.olekdia.mvpcore.presentation.managers.ITextManager
import com.olekdia.mvpcore.presentation.managers.IToastManager

class PlatformComponentFactory(private val context: Context) :
    ComponentFactory<IPlatformComponent>(

        mutableMapOf(
            IDbRepository.COMPONENT_ID to { DbRepository(context) },
            IPrefsRepository.COMPONENT_ID to { PrefsRepository(context.defaultSharedPreferences) },
            IToastManager.COMPONENT_ID to { ToastManager(context) },
            ISnackManager.COMPONENT_ID to { SnackManager() },
            ITextManager.COMPONENT_ID to { TextManager(context) },

            ITaskDbRepository.COMPONENT_ID to TaskDbRepository.FACTORY
        )
    )