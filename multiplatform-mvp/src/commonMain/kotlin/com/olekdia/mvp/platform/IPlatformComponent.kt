package com.olekdia.mvp.platform

import com.olekdia.mvp.IComponent

/**
 *           Lifecycle:
 *          ------------
 *         | onCreate() |
 *          ------------
 *               ↓
 *          -------------
 *         | onDestroy() |
 *          -------------
 */
interface IPlatformComponent : IComponent