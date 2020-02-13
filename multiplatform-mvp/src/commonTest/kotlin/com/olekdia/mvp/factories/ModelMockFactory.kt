package com.olekdia.mvp.factories

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.mocks.IMockModel
import com.olekdia.mvp.mocks.MockModel
import com.olekdia.mvp.model.IModel

class ModelMockFactory : ComponentFactory<IModel>(

    mutableMapOf(
        IMockModel.COMPONENT_ID to { MockModel() }
    )
)