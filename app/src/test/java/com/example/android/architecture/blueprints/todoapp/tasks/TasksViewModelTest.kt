package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitNextValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Abdulmajeed Alyafey on 4/28/20.
 */

class TasksViewModelTest {

    /**
     * Testing Coroutines and ViewModels.
     *
     * Coroutine dispatcher meant for testing. It executes tasks immediately and gives you control over the timing of coroutine
     * execution in tests, such as allowing you to pause and restart coroutine execution.
     *
     * Set the main coroutines dispatcher for unit testing.
     */
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components. (Testing Architecture Components).
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Class under test.
    private lateinit var tasksViewModel: TasksViewModel

    // Use a fake repository to be injected into the viewModel
    private lateinit var testRepository: FakeTestRepository

    @Before
    fun setUpViewModel() {
        // Prepare repository
        testRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        testRepository.addTasks(task1, task2, task3)
        tasksViewModel = TasksViewModel(testRepository)
    }

    @Test
    fun addNewTask_triggered_EventIsFired() {
        /**** {When} adding a new task ****/
        tasksViewModel.addNewTask()

        /**** {Then} the new task event is triggered ****/
        val value = tasksViewModel.newTaskEvent.getOrAwaitNextValue()
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFilter_allTasks_tasksAddViewVisible() {
        // When
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then
        val liveDataValue = tasksViewModel.tasksAddViewVisible.getOrAwaitNextValue()
        assertThat(liveDataValue, `is`(true))
    }

    /**
     * For Testing Coroutines and ViewModels.
     */
    @Test
    fun completeTask_shows_correctSnackbarMessage() {
        // Given active task to be added to the repository.
        val task = Task("T4", "D4")
        testRepository.addTasks(task)

        // When marking the task as completed..
        tasksViewModel.completeTask(task, true)

        // Then verify the task is completed. + the snackbar message is the correct one.
        assertThat(testRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        val snackbarTextRsrId: Event<Int> = tasksViewModel.snackbarText.getOrAwaitNextValue ()
        assertThat(snackbarTextRsrId.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}