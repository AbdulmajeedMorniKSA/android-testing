package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.runBlocking

/**
 * Created by Abdulmajeed Alyafey on 4/29/20.
 */
class FakeTestRepository : TasksRepository {

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private val observableTasks = MutableLiveData<Result<List<Task>>>()

    /**
     * Helper method specifically for tests that lets you add tasks.
     */
    fun addTasks(vararg tasks: Task) {
        tasks.forEach { tasksServiceData[it.id] = it }
        runBlocking { refreshTasks() }
    }

    override suspend fun getTasks(forceUpdate: Boolean): Result<List<Task>> {
        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return observableTasks
    }

    override suspend fun refreshTask(taskId: String) {
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return MutableLiveData<Result<Task>>()

    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        return Result.Success(Task())

    }

    override suspend fun saveTask(task: Task) {
    }

    override suspend fun completeTask(task: Task) {
    }

    override suspend fun completeTask(taskId: String) {
    }

    override suspend fun activateTask(task: Task) {
    }

    override suspend fun activateTask(taskId: String) {
    }

    override suspend fun clearCompletedTasks() {
    }

    override suspend fun deleteAllTasks() {
    }

    override suspend fun deleteTask(taskId: String) {
    }

}