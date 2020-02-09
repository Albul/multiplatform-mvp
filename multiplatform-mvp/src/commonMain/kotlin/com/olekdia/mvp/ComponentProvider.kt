package com.olekdia.mvp

class ComponentProvider<T : IComponent>(
    private val facade: Facade,
    private val factory: IComponentFactory<T>
) : IComponentProvider<T> {

    private val instanceMap: MutableMap<String, T> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <C : T> get(componentId: String): C? =
        get(componentId, null)

    @Suppress("UNCHECKED_CAST")
    override fun <C : T> get(componentId: String, param: Any?): C? =
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
        remove(component.componentId)
    }

    override fun remove(componentId: String) {
        remove(componentId, null)
    }

    override fun remove(componentId: String, param: Any?) {
        instanceMap.remove(toInstanceId(componentId, param))
    }

    private fun create(componentId: String): T? =
        factory.create(componentId)
            ?.also {
                facade.inject(it)
            }
    
    private fun toInstanceId(componentId: String, param: Any?): String =
        if (param == null) {
            componentId
        } else {
            componentId + param.toString()
        }
}