package com.olekdia.mvp.factories

import com.olekdia.mvp.ComponentFactory
import com.olekdia.mvp.mocks.IMockPresenter
import com.olekdia.mvp.mocks.MockPresenter
import com.olekdia.mvp.presenter.IPresenter

class PresenterMockFactory : ComponentFactory<IPresenter>(

    mutableMapOf(
        IMockPresenter.COMPONENT_ID to { MockPresenter() }
    )
)