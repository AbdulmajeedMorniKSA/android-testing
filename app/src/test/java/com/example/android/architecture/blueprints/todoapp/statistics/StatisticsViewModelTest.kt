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
    private lateinit var tasksRepository: FakeTestRepository

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

    /**
     * Testing error handling...
     *
     * Try to remove [mainCoroutineRule] and see what happen when you run bellow test case!
     *
     * You will get exception because view model generates Coroutine on Dispathers.Main!!
     */

    @Test fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay() {
        // When error happens + and tries to get tasks...
        tasksRepository.setReturnError(true)
        statisticsViewModel.refresh()

        // Then verify that empty and error are true (which triggers an error message to be shown).
        assertThat(statisticsViewModel.empty.getOrAwaitNextValue(), `is`(true))
        assertThat(statisticsViewModel.error.getOrAwaitNextValue(), `is`(true))
    }
    
}