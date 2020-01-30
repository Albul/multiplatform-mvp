package com.olekdia.sample

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.olekdia.fam.FloatingActionsMenu
import com.olekdia.sample.view.fragments.IStatefulFragment
import com.olekdia.sample.view.fragments.IStatefulFragment.StackState
import com.olekdia.sample.view.fragments.InputTaskFragment
import com.olekdia.sample.view.fragments.TaskListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)

        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.content_container,
                TaskListFragment(),
                TaskListFragment.TAG
            )
            .also {
                it.commit()
            }

        findViewById<FloatingActionsMenu>(R.id.fab_menu).setOnFabClickListener { btnId ->
            when (btnId) {
                R.id.fab_expand_menu_button -> onShowTaskFrag()
                R.id.fab_category -> onShowCategoryFrag()
            }
        }
    }

    fun onShowTaskFrag() {
        showFragment(InputTaskFragment(), InputTaskFragment.TAG)
    }

    fun onShowCategoryFrag() {
        // todo
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
}