package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Created by Abdulmajeed Alyafey on 4/28/20.
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var tasksLocalDataSource: FakeDataSource
    private lateinit var tasksRemoteDataSource: FakeDataSource

    private lateinit var tasks : List<Task>

    // Class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    @Before
    fun setUpTasks() {
        tasks = listOf(
                Task("Title1", "Description1", isCompleted = false),
                Task("Title2", "Description2", isCompleted = true),
                Task("Title3", "Description3", isCompleted = true)
        )
        setUpRepository(
                FakeDataSource(tasks.toMutableList()),
                FakeDataSource(tasks.toMutableList())
        )
    }

    /**
     * Used to pass fakes or mocks [TasksDataSource].
     *
     * By default, fakes are used.
     */
    private fun setUpRepository(
            localDataSource: FakeDataSource,
            remoteDataSource: FakeDataSource
    ) {
        // Assign and get reference of the data sources.
        tasksLocalDataSource = localDataSource
        tasksRemoteDataSource = remoteDataSource
        tasksRepository = DefaultTasksRepository(
                localDataSource,
                remoteDataSource,
                Dispatchers.Main //Here  MainCoroutineRule swaps the Dispatcher.Main for a TestCoroutineDispatcher
        )
    }

    /**
     * Take a look at
     * https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-survey/#3
     *
     * Generally, only create one TestCoroutineDispatcher to run a test.
     *
     * Whenever you call runBlockingTest, it will create a new TestCoroutineDispatcher if you don't specify one.
     *
     * MainCoroutineRule includes a TestCoroutineDispatcher. So, to ensure that
     * you don't accidentally create multiple instances of TestCoroutineDispatcher,
     * use the mainCoroutineRule.runBlockingTest instead of just runBlockingTest.
     */
    @Test
    fun getTasks_requestAllTasksFromRemoteDataSource()= mainCoroutineRule.runBlockingTest {
        // When tasks are requested from the repository with force update equals to true.
        val tasks = tasksRepository.getTasks(forceUpdate = false) as Result.Success

        // Then tasks are loaded from the remote data source
        assertThat(tasks.data as  MutableList<Task> , IsEqual(tasksRemoteDataSource.tasks))
    }

    @Test fun completeTask_savesTaskAndBeCompleted() = runBlockingTest {
        with(tasksRepository) {
            // Given a stub active task with title and description added in the repository
            val newTask = Task("TaskNew", "66336")

            // When
            saveTask(newTask)
            completeTask(newTask)

            // Then check task will be completed.
            val task = tasksLocalDataSource.tasks?.find { it.id == newTask.id }
            assertThat(task?.isActive, `is`(false))
        }
    }

    @Test
    fun clearCompletedTasks_returnsOne() = runBlockingTest {
        // Given new tasks in addition to the ones added before.
        val task4 = Task("Title4", "Description5", isCompleted = true)
        val task5 = Task("Title5", "Description6", isCompleted = true)
        tasksRepository.saveTask(task4)
        tasksRepository.saveTask(task5)

        // When delete all completed tasks..
        tasksRepository.clearCompletedTasks()

        // Then remaining tasks is 1
        assertThat(tasksLocalDataSource.tasks?.size, `is`(1))
        assertThat(tasksRemoteDataSource.tasks?.size, `is`(1))
    }

    /** Using Mocks.. **/
    @Test
    fun saveTask_SavesTaskInRemoteAndLocal() = runBlockingTest {
        // Given mocks data sources and a task..
        setUpRepository(
                mock(FakeDataSource(tasks.toMutableList())::class.java),
                mock(FakeDataSource(tasks.toMutableList())::class.java)
        )
        val newTask = Task("Task222", "66336")

        // When
        tasksRepository.saveTask(newTask)

        // Then
        verify(tasksRemoteDataSource).saveTask(newTask)
        verify(tasksLocalDataSource).saveTask(newTask)
    }

    /** Using Mocks.. **/
    @Test
    fun clearCompletedTasks_deleteTasksFromLocalAndRemoteSource() = runBlockingTest {
        // Given mocks data sources and a task..
        setUpRepository(
                mock(FakeDataSource(tasks.toMutableList())::class.java),
                mock(FakeDataSource(tasks.toMutableList())::class.java)
        )
        // When delete all completed tasks..
        tasksRepository.clearCompletedTasks()

        // Verify both local and remote functions are called
        verify(tasksLocalDataSource).clearCompletedTasks()
        verify(tasksRemoteDataSource).clearCompletedTasks()
    }
}