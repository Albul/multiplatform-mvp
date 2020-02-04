package com.olekdia.mvpapp.platform

import android.content.Context
import com.olekdia.androidcommon.extensions.defaultSharedPreferences
import com.olekdia.mvp.IComponent
import com.olekdia.mvp.IComponentFactory
import com.olekdia.mvpapp.model.repositories.PrefRepository
import com.olekdia.mvpcore.platform.managers.ITextManager
import com.olekdia.mvpcore.model.repositories.ITaskDbRepository
import com.olekdia.mvpapp.model.repositories.TaskDbRepository
import com.olekdia.mvpapp.model.repositories.db.DbRepository
import com.olekdia.mvpapp.model.repositories.db.IDbRepository
import com.olekdia.mvpcore.platform.managers.ISnackManager
import com.olekdia.mvpcore.platform.managers.IToastManager
import com.olekdia.mvpapp.platform.managers.SnackManager
import com.olekdia.mvpapp.platform.managers.TextManager
import com.olekdia.mvpapp.platform.managers.ToastManager
import com.olekdia.mvpcore.model.repositories.IPrefRepository

class PlatformComponentFactory(private val context: Context) : IComponentFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : IComponent> construct(componentId: String): T =
        when (componentId) {
            IDbRepository.COMPONENT_ID -> DbRepository(context)
            IPrefRepository.COMPONENT_ID -> PrefRepository(context.defaultSharedPreferences)
            IToastManager.COMPONENT_ID -> ToastManager(context)
            ISnackManager.COMPONENT_ID -> SnackManager()
            ITextManager.COMPONENT_ID -> TextManager(context)

            ITaskDbRepository.COMPONENT_ID -> TaskDbRepository()

            else -> throw RuntimeException("Platform component class not found")
        }.let { it as T }
}