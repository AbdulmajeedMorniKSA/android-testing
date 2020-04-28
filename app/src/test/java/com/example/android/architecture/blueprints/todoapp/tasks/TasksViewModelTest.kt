package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.getOrAwaitNextValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.IsNot.not
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.hamcrest.core.Is.`is`
import org.junit.Before

/**
 * Created by Abdulmajeed Alyafey on 4/28/20.
 */

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    private lateinit var tasksViewModel: TasksViewModel

    // Executes each task synchronously using Architecture Components.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpViewModel() {
        /**** {Given} the ViewModel ****/
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_triggered_EventIsFired() {
        /**** {When} adding a new task ****/
        tasksViewModel.addNewTask()

        /**** {Then} the new task event is triggered ****/
        val value = tasksViewModel.newTaskEvent.getOrAwaitNextValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
    }

    @Test
    fun setFilter_allTasks_tasksAddViewVisible() {
        // When
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then
        val liveDataValue = tasksViewModel.tasksAddViewVisible.getOrAwaitNextValue()
        assertThat(liveDataValue, `is`(true))
    }
}