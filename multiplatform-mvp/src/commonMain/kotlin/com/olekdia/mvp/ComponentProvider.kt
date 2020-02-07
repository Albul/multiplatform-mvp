package com.olekdia.mvp

class ComponentProvider<T : IComponent>(
    private val facade: Facade,
    private val factory: IComponentFactory<T>
) : IComponentProvider<T> {

    private val instanceMap: MutableMap<String, in T> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    override fun <C : T> get(componentId: String): C? =
        run {
            instanceMap[componentId]
                ?: create(componentId)
                    ?.also {
                        instanceMap[componentId] = it
                        //it.onCreate() todo
                    }
        } as? C

    @Suppress("UNCHECKED_CAST")
    override fun <C : T> get(componentId: String, param: Any): C? =
        run {
            val composeId = componentId + param.toString()
            instanceMap[composeId]
                ?: create(componentId)
                    ?.also {
                        instanceMap[composeId] = it
                        //it.onCreate() todo
                    }
        } as? C

    private fun create(componentId: String): T? =
        factory.create(componentId)
            ?.also {
                facade.inject(it)
            }

    override fun remove(component: T) {
        remove(component.componentId)
    }

    override fun remove(componentId: String) {
        instanceMap.remove(componentId)
    }

    override fun remove(componentId: String, tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}