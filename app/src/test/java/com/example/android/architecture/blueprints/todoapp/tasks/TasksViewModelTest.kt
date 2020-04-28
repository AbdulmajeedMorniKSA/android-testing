package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.IsNot.not
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.*

/**
 * Created by Abdulmajeed Alyafey on 4/28/20.
 */

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addNewTask_triggered_EventIsFired() {
        /**** {Given} a ViewModel ****/
        val viewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        /**** {When} adding a new task ****/
        viewModel.addNewTask()

        /**** {Then} the new task event is triggered ****/
        val value = viewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
    }
}