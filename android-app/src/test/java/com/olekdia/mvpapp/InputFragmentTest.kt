package com.olekdia.mvpapp

import android.os.Build
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.rule.ActivityTestRule
import com.olekdia.mvpapp.common.extensions.presenterProvider
import com.olekdia.mvpapp.presentation.MainActivity
import com.olekdia.mvpapp.presentation.fragments.InputTaskFragment
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpcore.presentation.presenters.IInputTaskPresenter
import com.olekdia.mvpcore.presentation.presenters.ITaskListPresenter
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboMenuItem

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, sdk = [Build.VERSION_CODES.O_MR1])
class InputFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    fun getInputTaskPresenter(fragment: Fragment): IInputTaskPresenter {
        return fragment.presenterProvider
            ?.getOrCreate<IInputTaskPresenter>(IInputTaskPresenter.COMPONENT_ID)!!
    }

    @Test
    fun `fragment and presenterProvider should not be null`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()


        scenario.onFragment { fragment: InputTaskFragment ->
            assertNotNull(fragment)
            assertNotNull(fragment.presenterProvider)
        }
    }

    @Test
    fun `launch fragment - presenter have view reference`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()

        scenario.moveToState(Lifecycle.State.RESUMED).onFragment { fragment: InputTaskFragment ->
            assertNotNull(getInputTaskPresenter(fragment).view)
        }
    }

    @Test
    fun `input text - presenter state is correct`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()
        val newTodoName = "New todo"

        scenario.onFragment { fragment: InputTaskFragment ->
            fragment.view!!
                .findViewById<EditText>(R.id.task_name)
                .setText(newTodoName)
            fragment.onEditorAction(null, EditorInfo.IME_ACTION_DONE, null)

            assertEquals(
                newTodoName,
                getInputTaskPresenter(fragment).state.currTask.name
            )
        }
    }

    @Test
    fun `click priority - presenter state is correct`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()

        scenario.onFragment { fragment: InputTaskFragment ->
            fragment.view!!
                .findViewById<View>(R.id.task_priority_low)
                .performClick()

            assertEquals(
                TaskPriority.LOW,
                getInputTaskPresenter(fragment).state.currTask.priority
            )
        }
    }

    @Test
    fun `change state, click apply - task is saved, fragment is destroyed`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()
        val newTodoName = "New todo1"

        scenario.onFragment { fragment: InputTaskFragment ->
            val listPresenter = fragment.presenterProvider!!
                .getOrCreate<ITaskListPresenter>(ITaskListPresenter.COMPONENT_ID)

            fragment.view!!
                .findViewById<EditText>(R.id.task_name)
                .setText(newTodoName)
            fragment.view!!
                .findViewById<View>(R.id.task_priority_low)
                .performClick()

            fragment.onOptionsItemSelected(RoboMenuItem(R.id.apply_button))

            assertTrue(
                listPresenter!!.state.list!!.any { it.name == newTodoName }
            )
            assertTrue(fragment.lifecycle.currentState.isAtLeast(Lifecycle.State.DESTROYED))
        }
    }

    @Test
    fun `change state, click exit - task is not saved, fragment is not destroyed`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()
        val newTodoName = "New todo2"

        scenario.onFragment { fragment: InputTaskFragment ->
            val listPresenter = fragment.presenterProvider!!
                .getOrCreate<ITaskListPresenter>(ITaskListPresenter.COMPONENT_ID)!!
            assertNotNull(listPresenter.state.list)

            fragment.view!!
                .findViewById<EditText>(R.id.task_name)
                .setText(newTodoName)

            fragment.onOptionsItemSelected(RoboMenuItem(R.id.home))

            assertFalse(
                listPresenter.state.list!!.any { it.name == newTodoName }
            )
            assertTrue(fragment.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
        }
    }

    @Test
    fun `change state, recreate - state is properly restored`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()
        val newTodoName = "New todo1"

        scenario.onFragment { fragment: InputTaskFragment ->
            fragment.view!!
                .findViewById<EditText>(R.id.task_name)
                .setText(newTodoName)
            fragment.onEditorAction(null, EditorInfo.IME_ACTION_DONE, null)

            fragment.view!!
                .findViewById<View>(R.id.task_priority_medium)
                .performClick()
        }

        scenario.recreate()

        scenario.onFragment { fragment: InputTaskFragment ->
            val inputPresenter = getInputTaskPresenter(fragment)

            assertEquals(
                TaskPriority.MEDIUM,
                inputPresenter.state.currTask.priority
            )

            assertEquals(
                newTodoName,
                inputPresenter.state.currTask.name
            )
        }
    }
}