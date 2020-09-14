package com.olekdia.mvp.model

import com.olekdia.mvp.IMutableComponentProvider

interface IModelHolder {
    val modelProvider: IMutableComponentProvider<IModel>
}