package com.example.android.architecture.blueprints.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import java.lang.Exception


/**
 * Created by Abdulmajeed Alyafey on 4/28/20.
 */

open class FakeDataSource(var tasks: MutableList<Task>? = mutableListOf())
    : TasksDataSource {

    override suspend fun getTasks(): Result<List<Task>> {
        tasks?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(Exception("Tasks not fount"))
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }

    override suspend fun saveTask(task: Task) {
        tasks?.add(task)
    }

    override suspend fun refreshTasks() {

    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        return MutableLiveData<Result<List<Task>>>()
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return MutableLiveData<Result<Task>>()
    }

    override suspend fun getTask(taskId: String): Result<Task> {
        return Result.Success(Task())
    }

    override suspend fun refreshTask(taskId: String) {
    }

    override suspend fun completeTask(task: Task) {
        tasks?.find { it.id == task.id }?.isCompleted = true
    }

    override suspend fun completeTask(taskId: String) {
    }

    override suspend fun activateTask(task: Task) {
    }

    override suspend fun activateTask(taskId: String) {
    }

    override suspend fun clearCompletedTasks() {
        tasks?.removeAll { it.isCompleted }
    }


    override suspend fun deleteTask(taskId: String) {
    }

}