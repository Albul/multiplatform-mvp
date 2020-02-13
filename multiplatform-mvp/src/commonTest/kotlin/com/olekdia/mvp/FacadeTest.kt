package com.olekdia.mvp

import com.olekdia.mvp.mocks.IMockModel
import com.olekdia.mvp.mocks.IMockPresenter
import com.olekdia.mvp.mocks.MockModelFake
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull

class FacadeTest : BaseTest() {

    @Test
    fun `load() new model factory - old model destroyed, new model available`() {
        val modelBefore: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID)!!
        assertEquals("Real model string", modelBefore.getModelString())
        facade.load(modelFactories = arrayOf(IMockModel.COMPONENT_ID to { MockModelFake() }))
        assertEquals(1, modelBefore.onDestroyCalled)

        val modelAfter: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID)!!
        assertNotSame(modelBefore, modelAfter)
        assertEquals("Fake model string", modelAfter.getModelString())
    }

    @Test
    fun `unload() presenter - presenters with any params not available any more`() {
        val presenter: IMockPresenter = presenterProvider.get(IMockPresenter.COMPONENT_ID)!!
        val presenter1: IMockPresenter = presenterProvider.get(IMockPresenter.COMPONENT_ID, "1")!!
        val presenter2: IMockPresenter = presenterProvider.get(IMockPresenter.COMPONENT_ID, "2")!!

        facade.unload(presenterIds = arrayOf(IMockPresenter.COMPONENT_ID))

        assertNull(presenterProvider.get(IMockPresenter.COMPONENT_ID))
        assertNull(presenterProvider.get(IMockPresenter.COMPONENT_ID, "1"))
        assertNull(presenterProvider.get(IMockPresenter.COMPONENT_ID, "2"))

        assertEquals(1, presenter.onDestroyCalled)
        assertEquals(1, presenter1.onDestroyCalled)
        assertEquals(1, presenter2.onDestroyCalled)
    }
}