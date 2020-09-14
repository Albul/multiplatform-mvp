package com.olekdia.mvp.platform

import com.olekdia.mvp.IMutableComponentProvider

interface IPlatformHolder {
    val platformProvider: IMutableComponentProvider<IPlatformComponent>
}