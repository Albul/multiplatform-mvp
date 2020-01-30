package com.olekdia.sample

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.olekdia.fam.FloatingActionsMenu
import com.olekdia.sample.view.fragments.IStatefulFragment
import com.olekdia.sample.view.fragments.IStatefulFragment.StackState
import com.olekdia.sample.view.fragments.InputTaskFragment
import com.olekdia.sample.view.fragments.TaskListFragment

class MainActivity : AppCompatActivity() {

    var actionBar: ActionBar? = null
    var fabMenu: FloatingActionsMenu? = null

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
                        R.id.fab_category -> onShowCategoryFrag()
                    }
                }
            }

        showFragment(
            frag = TaskListFragment(),
            fragTag = TaskListFragment.TAG,
            data = null,
            containerViewId = R.id.content_container
        )
    }

    fun onShowTaskFrag() {
        showFragment(InputTaskFragment(), InputTaskFragment.TAG)
    }

    fun onShowCategoryFrag() {
        // todo
    }

    fun prepareToolbar(@ViewId componentId: String) {
        when (componentId) {
            ViewId.INPUT_TASK ->
                actionBar?.setDisplayHomeAsUpEnabled(true)

            else -> {
                actionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    fun setActionBarTitle(title: CharSequence?) {
        actionBar?.title = title
    }

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
                    R.anim.frag_enter, R.anim.frag_exit,
                    R.anim.frag_enter, R.anim.frag_exit
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
        frag !is IStatefulFragment || (frag as IStatefulFragment).onBackPressed()
}