package com.olekdia.mvpcore.domain

import com.olekdia.mvp.model.IModelHolder
import com.olekdia.mvpcore.domain.models.ITaskModel

interface IExtModelHolder : IModelHolder {

    val taskModel: ITaskModel
        get() = modelProvider.getOrCreate(ITaskModel.COMPONENT_ID)!!
}