package com.olekdia.mvp

import com.olekdia.mvp.mocks.IMockModel
import com.olekdia.mvp.mocks.IMockPresenter
import com.olekdia.mvp.mocks.MockModel
import com.olekdia.mvp.mocks.MockPresenter
import kotlin.test.*

class ProviderTest : BaseTest() {

    @Test
    fun `get() - returns proper instance`() {
        assertTrue(modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID) is MockModel)
        assertTrue(presenterProvider.get<IMockPresenter>(IMockPresenter.COMPONENT_ID) is MockPresenter)
    }

    @Test
    fun `get() multiple times - onCreate() called once`() {
        val model1: IMockModel = retrieveMockModel()
        val model2: IMockModel = retrieveMockModel()

        assertEquals(1, model1.onCreateCalled)
        assertEquals(1, model2.onCreateCalled)
    }

    @Test
    fun `get() multiple times - returns same instance`() {
        val model1: IMockModel = retrieveMockModel()
        val model2: IMockModel = retrieveMockModel()

        assertSame(model1, model2)
    }

    @Test
    fun `get() with diff params - returns different instance`() {
        val model1: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!
        val model2: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "2")!!

        assertNotSame(model1, model2)
    }

    @Test
    fun `get() with identical params - returns same instance`() {
        val model1: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!
        val model2: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!

        assertSame(model1, model2)
    }

    @Test
    fun `get() unknown id - returns null`() {
        val model: IMockModel? = modelProvider.get("UNKNOWN_KEY")
        val presenter: IMockPresenter? = presenterProvider.get(IMockModel.COMPONENT_ID)

        assertNull(model)
        assertNull(presenter)
    }

    @Test
    fun `get(id) equals to get(id, null)`() {
        assertSame(
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID)!!,
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID, null)!!
        )
    }

    @Test
    fun `get(id) equals to get(id, "")`() {
        assertSame(
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID)!!,
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID, "")!!
        )
    }

    @Test
    fun `get(id) not equals to get(id, "null")`() {
        assertNotSame(
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID)!!,
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID, "null")!!
        )
    }

    @Test
    fun `remove(component) - removes instance`() {
        val model1: IMockModel = retrieveMockModel()

        modelProvider.remove(model1)
        val model2: IMockModel = retrieveMockModel()

        assertNotSame(model1, model2)
    }

    @Test
    fun `remove(id) - removes instance`() {
        val model1: IMockModel = retrieveMockModel()

        modelProvider.remove(IMockModel.COMPONENT_ID)
        val model2: IMockModel = retrieveMockModel()

        assertNotSame(model1, model2)
    }

    @Test
    fun `remove(id, param) - removes instance`() {
        val model1: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!

        modelProvider.remove(IMockModel.COMPONENT_ID, "1")
        val model2: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!

        assertNotSame(model1, model2)
    }

    @Test
    fun `remove(id, param) diff params - instance not removed`() {
        val modelBefore: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!

        modelProvider.remove(IMockModel.COMPONENT_ID, "2")
        val modelAfter: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!

        assertSame(modelBefore, modelAfter)
        assertEquals(0, modelAfter.onDestroyCalled)
    }
}