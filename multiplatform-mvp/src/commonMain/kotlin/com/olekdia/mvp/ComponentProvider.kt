package com.olekdia.mvp

class ComponentProvider<T : ILifecycleComponent>(
    private val facade: Facade,
    private val factory: IComponentFactory<T>
) : IComponentProvider<T> {

    private val instanceMap: MutableMap<String, T> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <C : T> get(componentId: String): C? =
        get(componentId, null)

    @Suppress("UNCHECKED_CAST")
    override fun <C : T> get(componentId: String, param: String?): C? =
        run {
            val instanceId = toInstanceId(componentId, param)
            instanceMap[instanceId]
                ?: create(componentId)
                    ?.also {
                        instanceMap[instanceId] = it
                        it.onCreate()
                    }
        } as? C

    override fun remove(component: T) {
        with(instanceMap.iterator()) {
            forEach {
                if (it.value === component) remove()
            }
        }
    }

    override fun remove(componentId: String) {
        remove(componentId, null)
    }

    override fun remove(componentId: String, param: String?) {
        instanceMap.remove(toInstanceId(componentId, param))
    }

    /**
     * Remove all components with all parameters
     */
    internal fun removeAll(componentId: String) {
        instanceMap
            .filter { it.key.startsWith(componentId) }
            .forEach { it.value.onDestroy() }
    }

    private fun create(componentId: String): T? =
        factory.create(componentId)
            ?.also {
                facade.inject(it)
            }

    private fun toInstanceId(componentId: String, param: String?): String =
        if (param == null) {
            componentId
        } else {
            componentId + param
        }
}