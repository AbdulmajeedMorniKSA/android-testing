package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
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

class TasksViewModelTest {

    private lateinit var tasksViewModel: TasksViewModel

    // Use a fake repository to be injected into the viewModel
    private lateinit var tasksRepository: FakeTestRepository

    // Executes each task synchronously using Architecture Components.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUpViewModel() {
        // Prepare repository
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(tasksRepository)
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