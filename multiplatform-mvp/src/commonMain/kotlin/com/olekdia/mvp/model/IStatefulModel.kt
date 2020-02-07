package com.olekdia.mvp.model

interface IStatefulModel<S> : IModel {
    val state: S
}