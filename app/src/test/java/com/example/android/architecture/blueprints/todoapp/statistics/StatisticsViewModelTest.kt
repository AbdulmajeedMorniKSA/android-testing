package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitNextValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Abdulmajeed Alyafey on 5/2/20.
 */
@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    // Executes each task synchronously using Architecture Components. (Testing Architecture Components).
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Testing coroutines and view models.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Class under test.
    private lateinit var statisticsViewModel: StatisticsViewModel

    // Fake...
    private lateinit var tasksRepository: TasksRepository

    @Before
    fun setUpViewModel() {
        tasksRepository = FakeTestRepository()
        statisticsViewModel = StatisticsViewModel(tasksRepository)
    }

    @Test
    fun loadTasks_loading() {
        // Pause dispatcher so you can verify initial values.
        mainCoroutineRule.pauseDispatcher()

        // Load the task in the view model.
        statisticsViewModel.refresh()

        // Then assert that the progress indicator is shown.
        assertThat(statisticsViewModel.dataLoading.getOrAwaitNextValue(), `is`(true))

        // Execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()

        // Then assert that the progress indicator is hidden.
        assertThat(statisticsViewModel.dataLoading.getOrAwaitNextValue(), `is`(false))
    }
}