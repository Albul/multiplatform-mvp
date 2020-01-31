package com.olekdia.sample.model.repositories.db

import com.olekdia.mvp.model.IBaseModel
import com.olekdia.sample.ModelId

interface IDbRepository : IBaseModel {

    companion object {
        const val COMPONENT_ID: String = ModelId.DB_REPOSITORY
    }
}