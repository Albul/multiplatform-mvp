package com.olekdia.mvpapp

import com.olekdia.mvp.Facade
import com.olekdia.mvp.IComponentProvider
import com.olekdia.mvp.presenter.IPresenter
import com.olekdia.mvpapp.data.repositories.TaskDbRepository
import com.olekdia.mvpapp.presentation.PlatformComponentFactory
import com.olekdia.mvpcore.domain.ModelFactory
import com.olekdia.mvpcore.domain.entries.TaskEntry
import com.olekdia.mvpcore.domain.repositories.ITaskDbRepository
import com.olekdia.mvpcore.presentation.PresenterFactory
import com.olekdia.mvpcore.presentation.presenters.IMainAppPresenter
import com.olekdia.mvpcore.presentation.views.IMainApp
import io.mockk.every
import io.mockk.slot
import io.mockk.spyk

class TestApplication : MvpApplication(), IMainApp {

    private val facade: Facade by lazy {
        Facade(
            ModelFactory(),
            PresenterFactory(),
            PlatformComponentFactory(baseContext)
        ).also {
            it.load(
                platformFactories = arrayOf(
                    ITaskDbRepository.COMPONENT_ID to {
                        spyk<TaskDbRepository>() {
                            val slot = slot<(list: List<TaskEntry>) -> Unit>()
                            every { loadAsync(capture(slot)) } answers {
                                slot.captured.invoke(
                                    listOf()
                                )
                            }
                        }
                    }
                )
            )
        }
    }

    override fun initApp() {
    }

    override fun updateConfiguration() {
    }

    override val componentId: String
        get() = IMainApp.COMPONENT_ID

    override val presenterProvider: IComponentProvider<IPresenter>
        get() = facade.presenterProvider

    private val mainAppPresenter: IMainAppPresenter
        get() = presenterProvider
            .get(IMainAppPresenter.COMPONENT_ID)!!

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppTheme)

        mainAppPresenter.let {
            it.onAttach(this)
            it.onAppInit()
        }
    }
}