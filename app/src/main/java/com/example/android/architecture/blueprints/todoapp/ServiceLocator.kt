package com.example.android.architecture.blueprints.todoapp

import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.TodoApplication
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private var database: ToDoDatabase? = null

    @Volatile
    var tasksRepository: TasksRepository? = null
        @VisibleForTesting set

    fun provideRepository(): TasksRepository = synchronized(this) {
        return tasksRepository ?: createTasksRepository()
    }

    private fun createTasksRepository(): TasksRepository {
        val newRepo = DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource())
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(): TasksDataSource {
        val database = database ?: createDatabase()
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDatabase(): ToDoDatabase {
        val result = Room.databaseBuilder(
                TodoApplication.getInstance(),
                ToDoDatabase::class.java,
                "Tasks.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() = synchronized(this) {
        runBlocking {
            TasksRemoteDataSource.deleteAllTasks()
        }
        // Clear all data to avoid test pollution.
        database?.apply {
            clearAllTables()
            close()
        }
        database = null
        tasksRepository = null
    }
}