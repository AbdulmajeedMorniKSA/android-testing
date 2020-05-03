package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.DataBindingIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.monitorActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.RunWith

/**
 * Created by Abdulmajeed Alyafey on 5/3/20.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
/**
 * https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-survey/#7
 */
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    // An idling resource that waits for Data Binding to have no pending bindings.
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        repository = ServiceLocator.provideRepository()
        runBlocking {
            repository.deleteAllTasks()
        }
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     *
     * Idling resource is a great way to tell Espresso when your app is in an
     * idle state. This helps Espresso to synchronize your test actions, which makes tests
     * significantly more reliable.
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun editTask() = runBlocking {
        // Set initial state.
        repository.saveTask(Task("TITLE1", "DESCRIPTION"))

        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario) // Important to make idling resource know about the layout.

        // Click on the task on the list and verify that all the data is correct.
        onView(withText("TITLE1")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("TITLE1")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("DESCRIPTION")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        // Click on the edit button, edit, and save.
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withText("NEW TITLE")).check(matches(isDisplayed()))

        // Verify previous task is not displayed.
        onView(withText("TITLE1")).check(doesNotExist())

        // Make sure the activity is closed before resetting the db.
        activityScenario.close()
    }

    @Test
    fun createOneTask_deleteTask() {
        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario) // Important to make idling resource know about the layout.

        // Click add button and add new info and save it.
        createTask("New Task Test Title", "New Task Test Description")

        // Verify task is displayed and open its details.
        onView(withText("New Task Test Title")).check(matches(isDisplayed()))
        onView(withText("New Task Test Title")).perform(click())

        // Verify details is shown.
        onView(withText("New Task Test Title")).check(matches(isDisplayed()))

        // Verify it was deleted.
        onView(withId(R.id.menu_delete)).perform(click())
        onView(withText("New Task Test Title")).check(doesNotExist())

        // Make sure the activity is closed before resetting the db.
        activityScenario.close()
    }


    @Test
    fun createTaskMarkItAsComplete_taskIsCompleteInList() {
        // start up Tasks screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario) // Important to make idling resource know about the layout.

        // Create new task.
        createTask("TASK!", "DESC!")

        // Verify it is shown in the list.
        onView(withText("TASK!")).check(matches(isDisplayed()))

        // Open the new created one.
        onView(withText("TASK!")).perform(click())

        // Mark it as checked..
        onView(withId(R.id.task_detail_complete_checkbox)).perform(click())

        // Click back button
        onView(isRoot()).perform(ViewActions.pressBack())

        // Check that the task is marked as completed
        onView(allOf(withId(R.id.complete_checkbox), hasSibling(withText("TASK!"))))
                .check(matches(isChecked()))
    }

    /**
     * You can create function and pass to it any thing to do your test on.
     */
    private fun createTask(title: String, description: String) {
        // Click on the add task button
        onView(withId(R.id.add_task_fab)).perform(click())

        // Add task title and description
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText(title))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText(description))

        // Save it ..
        onView(withId(R.id.save_task_fab)).perform(click())
    }
}