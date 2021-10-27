package com.olekdia.mvpcore.presentation

import com.olekdia.mvp.platform.IPlatformHolder
import com.olekdia.mvpcore.presentation.managers.ITextManager

interface IExtPlatformHolder : IPlatformHolder {

    val textManager: ITextManager
        get() = platformProvider.getOrCreate(ITextManager.COMPONENT_ID)
}