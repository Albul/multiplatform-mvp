package com.olekdia.mvpapp

import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.olekdia.fam.FloatingActionsMenu
import com.olekdia.mvpapp.platform.extensions.presenterProvider
import com.olekdia.mvpapp.model.entries.parcels.TaskParcel
import com.olekdia.mvpcore.platform.views.IInputTaskView
import com.olekdia.mvpcore.platform.views.IMainView
import com.olekdia.mvpcore.presentation.presenters.IMainViewPresenter
import com.olekdia.mvpcore.platform.views.ITaskListView
import com.olekdia.mvpapp.platform.fragments.IStatefulFragment
import com.olekdia.mvpapp.platform.fragments.IStatefulFragment.StackState
import com.olekdia.mvpapp.platform.fragments.InputTaskFragment
import com.olekdia.mvpapp.platform.fragments.TaskListFragment
import com.olekdia.mvpcore.model.entries.TaskEntry
import java.io.Serializable

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

    override fun showView(componentId: String, vararg params: Pair<String, Any?>) {
        showFragment(
            createFragment(componentId),
            componentId,
            bundleOf(*params)
        )
    }

    fun onShowTaskFrag() {
        showFragment(InputTaskFragment(), IInputTaskView.COMPONENT_ID)
    }

    fun onShowCategoryFrag() {
        // todo
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
        frag !is IStatefulFragment || (frag as IStatefulFragment).isKeepFragment()
}

fun bundleOf(vararg pairs: Pair<String, Any?>) = Bundle(pairs.size).apply {
    for ((key, value) in pairs) {
        when (value) {
            null -> putString(key, null) // Any nullable type will suffice.

            // Scalars
            is Boolean -> putBoolean(key, value)
            is Byte -> putByte(key, value)
            is Char -> putChar(key, value)
            is Double -> putDouble(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Short -> putShort(key, value)

            // References
            is Bundle -> putBundle(key, value)
            is CharSequence -> putCharSequence(key, value)
            is Parcelable -> putParcelable(key, value)

            // Scalar arrays
            is BooleanArray -> putBooleanArray(key, value)
            is ByteArray -> putByteArray(key, value)
            is CharArray -> putCharArray(key, value)
            is DoubleArray -> putDoubleArray(key, value)
            is FloatArray -> putFloatArray(key, value)
            is IntArray -> putIntArray(key, value)
            is LongArray -> putLongArray(key, value)
            is ShortArray -> putShortArray(key, value)

            // Reference arrays
            is Array<*> -> {
                val componentType = value::class.java.componentType!!
                @Suppress("UNCHECKED_CAST") // Checked by reflection.
                when {
                    Parcelable::class.java.isAssignableFrom(componentType) -> {
                        putParcelableArray(key, value as Array<Parcelable>)
                    }
                    String::class.java.isAssignableFrom(componentType) -> {
                        putStringArray(key, value as Array<String>)
                    }
                    CharSequence::class.java.isAssignableFrom(componentType) -> {
                        putCharSequenceArray(key, value as Array<CharSequence>)
                    }
                    Serializable::class.java.isAssignableFrom(componentType) -> {
                        putSerializable(key, value)
                    }
                    else -> {
                        val valueType = componentType.canonicalName
                        throw IllegalArgumentException(
                            "Illegal value array type $valueType for key \"$key\"")
                    }
                }
            }

            // Last resort. Also we must check this after Array<*> as all arrays are serializable.
            is Serializable -> putSerializable(key, value)
            //
            is TaskEntry -> putParcelable(key,
                TaskParcel(value)
            )
        }
    }
}