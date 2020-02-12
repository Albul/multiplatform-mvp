package com.olekdia.mvpapp.presentation

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.olekdia.fam.FloatingActionButton
import com.olekdia.fam.FloatingActionsMenu
import com.olekdia.mvpapp.R
import com.olekdia.mvpapp.common.extensions.presenterProvider
import com.olekdia.mvpapp.common.extensions.parcelize
import com.olekdia.mvpapp.presentation.dialogs.DiscardDialog
import com.olekdia.mvpcore.presentation.views.IInputTaskView
import com.olekdia.mvpcore.presentation.views.IMainView
import com.olekdia.mvpcore.presentation.presenters.IMainViewPresenter
import com.olekdia.mvpcore.presentation.views.ITaskListView
import com.olekdia.mvpapp.presentation.fragments.IStatefulFragment
import com.olekdia.mvpapp.presentation.fragments.IStatefulFragment.StackState
import com.olekdia.mvpapp.presentation.fragments.InputTaskFragment
import com.olekdia.mvpapp.presentation.fragments.TaskListFragment
import com.olekdia.mvpcore.ViewType
import com.olekdia.mvpcore.presentation.views.IDiscardDialogView

class MainActivity : AppCompatActivity(),
    IMainView {

    override val componentId: String
        get() = IMainView.COMPONENT_ID

    val presenter: IMainViewPresenter?
        get() = presenterProvider?.get(IMainViewPresenter.COMPONENT_ID)

    var actionBar: ActionBar? = null
    var fabMenu: FloatingActionsMenu? = null
    var formContainer: ViewGroup? = null
    var snackbarContainer: ViewGroup? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T> getPlatformView(): T = this as T

    override fun showView(
        componentId: String,
        viewType: ViewType,
        params: Array<Pair<String, Any?>>
    ): Boolean =
        showFragment(
            createFragment(componentId),
            componentId,
            viewType,
            bundleOf(*params.parcelize())
        )

//--------------------------------------------------------------------------------------------------
//  Activity lifecycle
//--------------------------------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)

        actionBar = supportActionBar?.also {
            it.setDisplayShowTitleEnabled(true)
        }

        fabMenu = findViewById<FloatingActionsMenu>(R.id.fab_menu)
            .also {
                it.setIsExpandable(false)
                it.setOnFabClickListener { btnId ->
                    when (btnId) {
                        R.id.fab_expand_menu_button -> onShowTaskFrag()
                    }
                }
            }

        formContainer = findViewById(R.id.form_content_container)
        snackbarContainer = findViewById(R.id.snackbar_container)

        if (savedInstanceState == null) {
            showFragment(
                frag = TaskListFragment(),
                fragTag = ITaskListView.COMPONENT_ID,
                viewType = ViewType.MAIN
            )
        }
    }
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter?.onAttach(this)

        /**
         * This is fix, because of very strange behaviour of recreated fragments, their order is not saved,
         * and their views can be shuffled. So we need to bring to Front the view of the top fragment
         */
        val fragCount: Int = supportFragmentManager.backStackEntryCount
        val lastInd: Int = fragCount - 1
        if (fragCount > 1) {
            for (i in 0..lastInd) {
                val frag: Fragment? = getFragmentAt(i)

                if (i > 0) frag?.view?.bringToFront()
                if (i == lastInd) {
                    sendFragmentTo(frag, StackState.FOREGROUND) // Last fragment to the top
                } else {
                    sendFragmentTo(frag, StackState.BACKGROUND)
                }
            }
        }
    }

    override fun onDestroy() {
        presenter?.onDetach(this)
        super.onDestroy()
    }

    fun onShowTaskFrag() {
        showFragment(
            InputTaskFragment(),
            IInputTaskView.COMPONENT_ID,
            ViewType.FORM
        )
    }

    fun prepareActionbar(componentId: String) {
        when (componentId) {
            IInputTaskView.COMPONENT_ID -> {
                actionBar?.run {
                    setDisplayHomeAsUpEnabled(true)
                    setDisplayShowCustomEnabled(false)
                }
            }

            ITaskListView.COMPONENT_ID -> {
                actionBar?.run {
                    setDisplayHomeAsUpEnabled(false)
                    setDisplayShowCustomEnabled(true)
                }
            }

            else -> {
                actionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    fun setActionBarTitle(title: CharSequence?) {
        actionBar?.title = title
    }

    val isFabShown: Boolean
        get() = fabMenu?.isShown == true

//--------------------------------------------------------------------------------------------------
//  Fragment management
//--------------------------------------------------------------------------------------------------

    override fun onBackPressed() {
        val stackCount: Int = supportFragmentManager.backStackEntryCount

        if (stackCount == 1) {
            moveTaskToBack(true)
            supportFinishAfterTransition()
        } else {
            if (stackCount == 2) {
                fabMenu?.show()
            }
            val topFrag: Fragment? = this.topFragment
            if (!keepFragment(topFrag)) {
                sendFragmentTo(topFrag, StackState.ETERNITY)
                if (supportFragmentManager.popBackStackImmediate()) {
                    sendFragmentTo(this.topFragment, StackState.FOREGROUND)
                }
            }
        }
    }

    private fun createFragment(componentId: String): Fragment? =
        when (componentId) {
            IInputTaskView.COMPONENT_ID -> InputTaskFragment()
            ITaskListView.COMPONENT_ID -> TaskListFragment()
            IDiscardDialogView.COMPONENT_ID -> DiscardDialog()
            else -> null
        }

    protected fun showFragment(
        frag: Fragment?,
        fragTag: String,
        viewType: ViewType,
        data: Bundle? = null
    ): Boolean {
        return if (frag == null) {
            false
        } else {
            if (viewType != ViewType.DIALOG
                && supportFragmentManager.backStackEntryCount != 0
            ) {
                sendFragmentTo(topFragment, StackState.BACKGROUND)
            }

            frag.arguments = data
            supportFragmentManager
                .beginTransaction()
                .also { trans ->
                    when (viewType) {
                        ViewType.MAIN ->
                            trans.add(R.id.content_container, frag, fragTag)
                                .addToBackStack(fragTag)
                        ViewType.FORM -> {
                            trans.setCustomAnimations(
                                R.anim.frag_enter,
                                R.anim.frag_exit,
                                R.anim.frag_enter,
                                R.anim.frag_exit
                            )
                                .add(R.id.form_content_container, frag, fragTag)
                                .addToBackStack(fragTag)
                        }
                        ViewType.DIALOG ->
                            trans.add(frag, fragTag)

                    }
                }.commit()

            true
        }
    }

    /**
     * Returns current top fragment
     *
     * @return fragment from top of the stack
     */
    protected val topFragment: Fragment?
        get() = supportFragmentManager.run {
            if (backStackEntryCount > 0) {
                findFragmentByTag(
                    getBackStackEntryAt(backStackEntryCount - 1).name
                )
            } else {
                null
            }
        }

    /**
     * Returns fragment from back stack
     * @param index of fragment
     * @return fragment
     */
    protected fun getFragmentAt(index: Int): Fragment? =
        supportFragmentManager.findFragmentByTag(
            supportFragmentManager.getBackStackEntryAt(
                index
            ).name
        )

    /**
     * If fragment is implemented IStackFragment,
     * onForeground or onBackground methods will be called corespondently
     * @param frag
     * @param state
     */
    protected fun sendFragmentTo(frag: Fragment?, state: StackState) {
        if (frag is IStatefulFragment) {
            when (state) {
                StackState.FOREGROUND ->
                    frag.onForeground()
                StackState.BACKGROUND ->
                    frag.onBackground()
                StackState.ETERNITY -> {
                    frag.onBackground()
                    frag.onEternity()
                }
            }
        }
    }

    /**
     * Keep fragment when back button is pressed
     * @param frag Fragment
     * @return true keep it, false - can be removed from backstack
     */
    protected fun keepFragment(frag: Fragment?): Boolean =
        frag !is IStatefulFragment || (frag as IStatefulFragment).isKeepFragment()
}