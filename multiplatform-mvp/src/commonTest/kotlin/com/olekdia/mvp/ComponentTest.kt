package com.olekdia.mvp

import com.olekdia.mvp.mocks.*
import kotlin.test.*

class ComponentTest : BaseTest() {

    @Test
    fun `onDestroy() - instance removed from provider`() {
        val model: IMockModel = retrieveMockModel()
        model.onDestroy()
        assertNotSame(model, retrieveMockModel())

        val presenter: IMockPresenter = presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID)
        presenter.onDestroy()
        assertNotSame(presenter, presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID))

        val manager: IMockPlatformManager = platformProvider.getOrCreate(IMockPlatformManager.COMPONENT_ID)
        manager.onDestroy()
        assertNotSame(manager, platformProvider.getOrCreate(IMockPlatformManager.COMPONENT_ID))
    }

    @Test
    fun `get() instance with param, onDestroy() - instance removed from provider only with that param`() {
        val model: IMockModel = retrieveMockModel()
        val model1: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")
        val model2: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "2")
        model1.onDestroy()
        assertSame(model, retrieveMockModel())
        assertNotSame(model1, modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1"))
        assertSame(model2, modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "2"))
    }

    @Test
    fun `get() - component providers are injected`() {
        val model: MockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID)
        assertSame(model.modelProvider, modelProvider)
        assertSame(model.platformProvider, platformProvider)

        val presenter: MockPresenter = presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID)
        assertSame(presenter.modelProvider, modelProvider)
        assertSame(presenter.presenterProvider, presenterProvider)
        assertSame(presenter.platformProvider, platformProvider)

        val manager: MockPlatformManager = platformProvider.getOrCreate(IMockPlatformManager.COMPONENT_ID)
        assertSame(manager.presenterProvider, presenterProvider)
        assertSame(manager.platformProvider, platformProvider)
    }


}