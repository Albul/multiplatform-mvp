package com.olekdia.mvp.model

import com.olekdia.mvp.IComponentProvider

interface IModelHolder {
    val modelProvider: IComponentProvider<IModel>
}