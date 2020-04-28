package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Abdulmajeed Alyafey on 4/28/20.
 */

@ExperimentalCoroutinesApi
class DefaultTasksRepositoryTest {

    private val task1 = Task("Title1", "Description1")
    private val task2 = Task("Title2", "Description2")
    private val task3 = Task("Title3", "Description3")

    private val localTasks = listOf(task3).sortedBy { it.id }
    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }

    private val newTasks = listOf(task3).sortedBy { it.id }

    private lateinit var tasksLocalDataSource: FakeDataSource
    private lateinit var tasksRemoteDataSource: FakeDataSource

    // Class under test
    private lateinit var tasksRepository: DefaultTasksRepository

    @Before
    fun createRepository() {
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksRepository = DefaultTasksRepository(
                tasksLocalDataSource,
                tasksRemoteDataSource,
                Dispatchers.Unconfined
        )
    }


    @Test
    fun getTasks_requestAllTasksFromRemoteDataSource()= runBlockingTest {
        // When tasks are requested from the repository with force update equals to true.
        val tasks = tasksRepository.getTasks(forceUpdate = true) as Result.Success

        // Then tasks are loaded from the remote data source
        assertThat(tasks.data, IsEqual(remoteTasks))
    }
}