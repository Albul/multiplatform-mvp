package com.olekdia.mvpcore.model

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.model.IModel
import com.olekdia.mvpcore.model.models.ITaskModel
import com.olekdia.mvpcore.model.models.TaskModel

class ModelFactory : ComponentFactory<IModel>(
    mutableMapOf(
        ITaskModel.COMPONENT_ID to { TaskModel() }
    )
)