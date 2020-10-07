package com.olekdia.mvp

import com.olekdia.mvp.mocks.IMockModel
import com.olekdia.mvp.mocks.IMockPresenter
import com.olekdia.mvp.mocks.MockModel
import com.olekdia.mvp.mocks.MockPresenter
import kotlin.test.*

class ProviderTest : BaseTest() {

    @Test
    fun `getOrCreate() - returns proper instance`() {
        assertTrue(modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID) is MockModel)
        assertTrue(presenterProvider.getOrCreate<IMockPresenter>(IMockPresenter.COMPONENT_ID) is MockPresenter)
    }

    @Test
    fun `getOrCreate() multiple times - onCreate() called once`() {
        val model1: IMockModel = retrieveMockModel()
        val model2: IMockModel = retrieveMockModel()

        assertEquals(1, model1.onCreateCalled)
        assertEquals(1, model2.onCreateCalled)
    }

    @Test
    fun `getOrCreate() multiple times - returns same instance`() {
        val model1: IMockModel = retrieveMockModel()
        val model2: IMockModel = retrieveMockModel()

        assertSame(model1, model2)
    }

    @Test
    fun `getOrCreate() with diff params - returns different instance`() {
        val model1: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")
        val model2: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "2")

        assertNotSame(model1, model2)
    }

    @Test
    fun `getOrCreate() with identical params - returns same instance`() {
        val model1: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")
        val model2: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")

        assertSame(model1, model2)
    }

    @Test
    fun `getOrCreate() unknown id - throws NoSuchElementException`() {
        assertFailsWith(NoSuchElementException::class) {
            modelProvider.getOrCreate<IMockModel>("UNKNOWN_KEY")
        }
        assertFailsWith(NoSuchElementException::class) {
            presenterProvider.getOrCreate<IMockPresenter>(IMockModel.COMPONENT_ID)
        }
    }

    @Test
    fun `getOrCreate(id) equals to get(id, null)`() {
        assertSame(
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID),
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, null)
        )
    }

    @Test
    fun `getOrCreate(id) equals to get(id, "")`() {
        assertSame(
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID),
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, "")
        )
    }

    @Test
    fun `getOrCreate(id) not equals to get(id, "null")`() {
        assertNotSame(
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID),
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, "null")
        )
    }

    @Test
    fun `get() - returns proper instance`() {
        assertTrue(modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID) is MockModel)
        assertTrue(modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID) is MockModel)
        assertTrue(presenterProvider.getOrCreate<IMockPresenter>(IMockPresenter.COMPONENT_ID) is MockPresenter)
        assertTrue(presenterProvider.get<IMockPresenter>(IMockPresenter.COMPONENT_ID) is MockPresenter)
    }

    @Test
    fun `get() multiple times - returns same instance`() {
        val model1: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID)
        val model2: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID)!!
        val model3: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID)!!

        assertSame(model1, model2)
        assertSame(model1, model3)
    }

    @Test
    fun `get() with diff params - returns different instance`() {
        modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, "1")
        modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, "2")

        val model1: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "1")!!
        val model2: IMockModel = modelProvider.get(IMockModel.COMPONENT_ID, "2")!!

        assertNotSame(model1, model2)
    }

    @Test
    fun `get() with identical params - returns same instance`() {
        val model1: IMockModel? = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")
        val model2: IMockModel? = modelProvider.get(IMockModel.COMPONENT_ID, "1")

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
    fun `get() known id, but yet not created instances - returns null`() {
        val model1: IMockModel? = modelProvider.get(IMockModel.COMPONENT_ID)
        val model2: IMockModel? = modelProvider.get(IMockModel.COMPONENT_ID, "1")

        assertNull(model1)
        assertNull(model2)
    }

    @Test
    fun `get(id) equals to get(id, null)`() {
        assertSame(
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID),
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, null)
        )

        assertSame(
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID)!!,
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID, null)!!
        )
    }

    @Test
    fun `get(id) equals to get(id, "")`() {
        assertSame(
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID),
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, "")
        )

        assertSame(
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID)!!,
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID, "")!!
        )
    }

    @Test
    fun `get(id) not equals to get(id, "null")`() {
        assertNotSame(
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID),
            modelProvider.getOrCreate<IMockModel>(IMockModel.COMPONENT_ID, "null")
        )
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
        val model1: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID)

        assertEquals(0, model1.onDestroyCalled)

        modelProvider.remove(IMockModel.COMPONENT_ID)

        assertEquals(0, model1.onDestroyCalled) // by Design

        assertNull(
            modelProvider[IMockModel.COMPONENT_ID]
        )
        val model2: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID)
        assertNotSame(model1, model2)
        assertEquals(0, model2.onDestroyCalled)

        assertNotNull(
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID)
        )

        model2.onDestroy()
        assertEquals(1, model2.onDestroyCalled)
        assertNull(
            modelProvider.get<IMockModel>(IMockModel.COMPONENT_ID)
        )
    }

    @Test
    fun `remove(id, param) - removes instance`() {
        val model1: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")

        modelProvider.remove(IMockModel.COMPONENT_ID, "1")
        val model2: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")

        assertNotSame(model1, model2)
    }

    @Test
    fun `remove(id, param) diff params - instance not removed`() {
        val modelBefore: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")

        modelProvider.remove(IMockModel.COMPONENT_ID, "2")
        val modelAfter: IMockModel = modelProvider.getOrCreate(IMockModel.COMPONENT_ID, "1")

        assertSame(modelBefore, modelAfter)
        assertEquals(0, modelAfter.onDestroyCalled)
    }
}