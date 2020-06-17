package com.olekdia.mvp

import com.olekdia.mvp.mocks.*
import kotlin.test.*

class FacadeTest : BaseTest() {

    @Test
    fun `load() new model factory - old model destroyed, new model available`() {
        val modelBefore: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID)!!
        assertEquals("Real model string", modelBefore.getModelString())

        facade.load(modelFactories = arrayOf(IMockModel.COMPONENT_ID to { MockModelFake() }))
        assertEquals(1, modelBefore.onDestroyCalled)

        val modelAfter: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID)!!
        assertNotSame(modelBefore, modelAfter)
        assertEquals("Fake model string", modelAfter.getModelString())
    }

    @Test
    fun `load() new components factory - old components destroyed, new components available`() {
        val compBefore: MockPlatformManager = platformProvider.getOrCreate(IMockPlatformManager.COMPONENT_ID)!!

        val mockOfMock = MockPlatformManager("Deb")
        facade.load(platformFactories = arrayOf(IMockPlatformManager.COMPONENT_ID to { mockOfMock }))
        assertEquals(1, compBefore.onDestroyCalled)

        val compAfter: MockPlatformManager = platformProvider.getOrCreate(IMockPlatformManager.COMPONENT_ID)!!
        assertNotSame(compBefore, compAfter)
        assertEquals("Lin", compBefore.param)
        assertEquals("Deb", compAfter.param)
        assertSame(mockOfMock, compAfter)

        val compAfter2: MockPlatformManager = platformProvider.getOrCreate(IMockPlatformManager.COMPONENT_ID)!!
        assertSame(mockOfMock, compAfter2)
    }

    @Test
    fun `unload() presenter - presenters with any params not available any more`() {
        val presenter: IMockPresenter = presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID)!!
        val presenter1: IMockPresenter = presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID, "1")!!
        val presenter2: IMockPresenter = presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID, "2")!!

        facade.unload(presenterIds = arrayOf(IMockPresenter.COMPONENT_ID))

        assertNull(presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID))
        assertNull(presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID, "1"))
        assertNull(presenterProvider.getOrCreate(IMockPresenter.COMPONENT_ID, "2"))

        assertEquals(1, presenter.onDestroyCalled)
        assertEquals(1, presenter1.onDestroyCalled)
        assertEquals(1, presenter2.onDestroyCalled)
    }
}