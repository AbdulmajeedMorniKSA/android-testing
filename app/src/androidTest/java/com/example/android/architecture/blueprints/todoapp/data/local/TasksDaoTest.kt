package com.example.android.architecture.blueprints.todoapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Abdulmajeed Alyafey on 5/2/20.
 * https://codelabs.developers.google.com/codelabs/advanced-android-kotlin-training-testing-survey/#6
 */

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDB() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
                getApplicationContext(),
                ToDoDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // GIVEN - Insert a task
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // WHEN - Get the task by id from the database.
        val value = database.taskDao().getTaskById(task.id)

        // THEN - The loaded data contains the expected values.
        assertThat(value as Task, notNullValue())
        assertThat(value.id, `is`(task.id))
        assertThat(value.title, `is`(task.title))
        assertThat(value.description, `is`(task.description))
        assertThat(value.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        // GIVEN - inserting task in db.
        val oldTask = Task("OldTitle", "OldDescription", isCompleted = true)
        database.taskDao().insertTask(oldTask)

        // WHEN - updating the old task with new one.
        val newTask = Task("NewTitle", "NewDescription", isCompleted = false, id = oldTask.id)
        database.taskDao().updateTask(newTask)

        // THEN - the updated task should be with the new values!
        val updatedTask = database.taskDao().getTaskById(newTask.id)
        assertThat(updatedTask as Task, notNullValue())
        assertThat(updatedTask.id, `is`(oldTask.id))
        assertThat(updatedTask.title, `is`("NewTitle"))
        assertThat(updatedTask.description, `is`("NewDescription"))
        assertThat(updatedTask.isCompleted, `is`(false))
    }
}