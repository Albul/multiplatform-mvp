package com.olekdia.mvpcore.domain

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.model.IModel
import com.olekdia.mvpcore.domain.models.ITaskModel
import com.olekdia.mvpcore.domain.models.TaskModel

class ModelFactory : ComponentFactory<IModel>(
    mutableMapOf(
        ITaskModel.COMPONENT_ID to { TaskModel() }
    )
)