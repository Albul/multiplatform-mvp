package com.olekdia.mvp.platform

import com.olekdia.mvp.IComponentProvider

interface IPlatformHolder {
    val platformProvider: IComponentProvider<IPlatformComponent>
}