package com.olekdia.mvpapp

import android.os.Build
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.olekdia.mvpapp.common.extensions.presenterProvider
import com.olekdia.mvpapp.presentation.fragments.InputTaskFragment
import com.olekdia.mvpcore.TaskPriority
import com.olekdia.mvpcore.presentation.presenters.IInputTaskPresenter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, sdk = [Build.VERSION_CODES.O_MR1])
class InputFragmentTest {

    fun getInputTaskPresenter(fragment: Fragment): IInputTaskPresenter {
        return fragment.presenterProvider
            ?.get<IInputTaskPresenter>(IInputTaskPresenter.COMPONENT_ID)!!
    }

    @Before
    fun setup() {
    }

    @Test
    fun `InputTaskFragment and presenterProvider should not be null`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()
        scenario.onFragment { fragment: InputTaskFragment ->
            assertNotNull(fragment)
            assertNotNull(fragment.presenterProvider)
        }
    }

    @Test
    fun `InputTaskFragment - presenter have view reference`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()

        scenario.onFragment { fragment: InputTaskFragment ->
            assertNotNull(getInputTaskPresenter(fragment).view)
        }
    }

    @Test
    fun `Input text - presenter state is correct`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()
        val newTodoName = "New todo"
        onView(withId(R.id.task_name))
            .perform(click())
            .perform(typeText(newTodoName))
            .perform(pressKey(KeyEvent.KEYCODE_ENTER))

        scenario.onFragment { fragment: InputTaskFragment ->

            assertEquals(
                newTodoName,
                getInputTaskPresenter(fragment).state.currTask.name
            )
        }
    }

    @Test
    fun `Click priority - presenter state is correct`() {
        val scenario = launchFragmentInContainer<InputTaskFragment>()
        onView(withId(R.id.task_priority_low))
            .perform(click())

        scenario.onFragment { fragment: InputTaskFragment ->
            assertEquals(
                TaskPriority.HIGH,
                getInputTaskPresenter(fragment).state.currTask.priority
            )
        }
    }
}