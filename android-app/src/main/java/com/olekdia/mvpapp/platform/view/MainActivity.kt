package com.olekdia.mvpapp.platform.view

import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.olekdia.fam.FloatingActionsMenu
import com.olekdia.mvpapp.R
import com.olekdia.mvpapp.platform.extensions.presenterProvider
import com.olekdia.mvpapp.platform.extensions.parcelize
import com.olekdia.mvpcore.platform.view.views.IInputTaskView
import com.olekdia.mvpcore.platform.view.views.IMainView
import com.olekdia.mvpcore.presentation.presenters.IMainViewPresenter
import com.olekdia.mvpcore.platform.view.views.ITaskListView
import com.olekdia.mvpapp.platform.view.fragments.IStatefulFragment
import com.olekdia.mvpapp.platform.view.fragments.IStatefulFragment.StackState
import com.olekdia.mvpapp.platform.view.fragments.InputTaskFragment
import com.olekdia.mvpapp.platform.view.fragments.TaskListFragment

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

    override fun showView(componentId: String, params: Array<Pair<String, Any?>>) {
        showFragment(
            createFragment(componentId),
            componentId,
            bundleOf(*params.parcelize())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)

        actionBar = supportActionBar?.also {
            it.setDisplayShowTitleEnabled(true)
        }

        fabMenu = findViewById<FloatingActionsMenu>(R.id.fab_menu)
            .also {
                it.setOnFabClickListener { btnId ->
                    when (btnId) {
                        R.id.fab_expand_menu_button -> onShowTaskFrag()
                    }
                }
            }

        formContainer = findViewById(R.id.form_content_container)
        snackbarContainer = findViewById(R.id.snackbar_container)

        showFragment(
            frag = TaskListFragment(),
            fragTag = ITaskListView.COMPONENT_ID,
            data = null,
            containerViewId = R.id.content_container
        )
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        presenter?.onAttach(this)
    }

    override fun onDestroy() {
        presenter?.onDetach(this)
        super.onDestroy()
    }

    fun onShowTaskFrag() {
        showFragment(InputTaskFragment(), IInputTaskView.COMPONENT_ID)
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

        if (fabMenu?.isExpanded == true && stackCount == 1) {
            fabMenu?.collapse()
        } else if (stackCount == 1) {
            moveTaskToBack(true)
            supportFinishAfterTransition()
        } else {
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
            else -> null
        }

    protected fun showFragment(
        frag: Fragment?,
        fragTag: String,
        data: Bundle? = null,
        @IdRes containerViewId: Int = R.id.form_content_container
    ): Boolean {
        return if (frag == null) {
            false
        } else {
            if (supportFragmentManager.backStackEntryCount != 0) {
                sendFragmentTo(topFragment, StackState.BACKGROUND)
            }

            frag.arguments = data
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.frag_enter,
                    R.anim.frag_exit,
                    R.anim.frag_enter,
                    R.anim.frag_exit
                )
                .add(containerViewId, frag, fragTag)
                .addToBackStack(fragTag)
                .also {
                    it.commit()
                }

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