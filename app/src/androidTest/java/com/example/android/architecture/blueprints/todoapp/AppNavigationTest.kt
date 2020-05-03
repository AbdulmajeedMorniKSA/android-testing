package com.example.android.architecture.blueprints.todoapp

import android.app.Activity
import android.view.Gravity
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import com.example.android.architecture.blueprints.todoapp.util.DataBindingIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Abdulmajeed Alyafey on 5/3/20.
 *
 * https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-survey/#8
 */

@RunWith(AndroidJUnit4::class)
@LargeTest

class AppNavigationTest {

    private lateinit var tasksRepository: TasksRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        tasksRepository = ServiceLocator.provideRepository()
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your idling resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun tasksScreen_clickOnDrawer_opensNavigation() {
        // Start the Tasks screen.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // 1. Check that left drawer is closed at startup.
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(Gravity.START)))

        // 2. Open drawer by clicking drawer icon.
        onView(
                withContentDescription(
                   activityScenario.getToolbarNavigationContentDescription()
                )
        ).perform(click())

        // 3. Check if drawer is open.
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.START)))

        // When using ActivityScenario.launch(), always call close()
        activityScenario.close()
    }

    @Test
    fun taskDetailScreen_doubleUpButton() = runBlocking {
        val task = Task("Up button", "Description of Up button")
        tasksRepository.saveTask(task)

        // Start the Tasks screen.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list.
        onView(withText("Up button")).perform(click())

        // Check it is displayed.
        onView(withText("Up button")).check(matches(isDisplayed()))
        onView(withText("Description of Up button")).check(matches(isDisplayed()))

        // Click on the edit task button.
        onView(withId(R.id.edit_task_fab)).perform(click())

        // Confirm that if we click Up button once, we end up back at the task details page.
        onView(
                withContentDescription(
                        activityScenario.getToolbarNavigationContentDescription()
                )
        ).perform(click())
        onView(withId(R.id.edit_task_fab)).check(matches(isDisplayed()))

        // Confirm that if we click Up button a second time, we end up back at the home screen.
        onView(
                withContentDescription(
                        activityScenario.getToolbarNavigationContentDescription()
                )
        ).perform(click())
        onView(withId(R.id.add_task_fab)).check(matches(isDisplayed()))

        // When using ActivityScenario.launch(), always call close().
        activityScenario.close()
    }

    @Test
    fun taskDetailScreen_doubleBackButton() = runBlocking {
        val task = Task("Back Button", "Description of Back Button")
        tasksRepository.saveTask(task)

        // Start the Tasks screen.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task on the list.
        onView(withText("Back Button")).perform(click())

        // Check it is displayed.
        onView(withText("Back Button")).check(matches(isDisplayed()))
        onView(withText("Description of Back Button")).check(matches(isDisplayed()))

        // Click on the edit task button.
        onView(withId(R.id.edit_task_fab)).perform(click())

        // Confirm that if we click Up button once, we end up back at the task details page.
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.edit_task_fab)).check(matches(isDisplayed()))

        // Confirm that if we click Up button a second time, we end up back at the home screen.
        onView(isRoot()).perform(ViewActions.pressBack())
        onView(withId(R.id.add_task_fab)).check(matches(isDisplayed()))

        // When using ActivityScenario.launch(), always call close().
        activityScenario.close()
    }


    /**
     * This is used to get referece of the toolbar of up button.
     * Lock at [taskDetailScreen_doubleUpButton]
     */
    fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription()
            : String {
        var description = ""
        onActivity {
            description =
                    it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as String
        }
        return description
    }
}