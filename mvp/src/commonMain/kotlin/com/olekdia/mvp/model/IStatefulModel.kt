package com.olekdia.mvp.model

interface IStatefulModel<S> : IBaseModel {
    val state: S
}