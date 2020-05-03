package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Abdulmajeed Alyafey on 5/3/20.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    @Before
    fun init() {
        repository = ServiceLocator.provideRepository()
        runBlocking {
            repository.deleteAllTasks()
        }
    }

    @After
    fun reset() {
        ServiceLocator.resetRepository()
    }

    @Test
    fun editTask() = runBlocking {

        // Set initial state
        repository.saveTask(Task("Title", "Desc"))

        // Start up Tasks screen.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        // Click on the task on the list and verify that all the data is correct.
        onView(withText("Title")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("Title")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("Desc")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isNotChecked()))

        // Click on the edit button, edit, and save.
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW Title"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW Desc"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // Verify task is displayed on screen in the task list.
        onView(withText("NEW Title")).check(matches(isDisplayed()))

        // Verify previous task is not displayed.
        onView(withText("Title")).check(doesNotExist())

        // Make sure the activity is closed before resetting the db:
        activityScenario.close()
    }
}