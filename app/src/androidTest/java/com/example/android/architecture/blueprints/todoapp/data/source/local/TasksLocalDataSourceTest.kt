package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Abdulmajeed Alyafey on 5/2/20.
 *
 * Integration test for TasksLocalDataSource
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Real classes under test..
    private lateinit var localDataSource: TasksLocalDataSource
    private lateinit var database: ToDoDatabase

    @Before
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
                getApplicationContext(),
                ToDoDatabase::class.java
        )
                .allowMainThreadQueries() // Normally, Room doesn't allow database queries to be run on the main thread. Calling allowMainThreadQueries turns off this check. Don't do this in production code!
                .build()

        localDataSource =
                TasksLocalDataSource(
                        database.taskDao(),
                        Dispatchers.Main
                )
    }

    @After
    fun cleanUp() { database.close() }

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - A new task saved in the database.
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN  - Task retrieved by ID.
        val result = localDataSource.getTask(newTask.id) as Success

        // THEN - Same task is returned.
        assertThat(result.succeeded, `is`(true))
        assertThat(result.data.title, `is`("title"))
        assertThat(result.data.description, `is`("description"))
        assertThat(result.data.isCompleted, `is`(false))
    }

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun completeTask_retrievedTaskIsComplete() = runBlocking {
        // GIVEN - A new task saved in the database.
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN - Completing the task and retrieving it by ID.
        localDataSource.completeTask(newTask)
        val result = localDataSource.getTask(newTask.id) as Success

        // THEN - the task should be completed.
        assertThat(result.succeeded, `is`(true))
        assertThat(result.data.isCompleted, `is`(true))
    }
}