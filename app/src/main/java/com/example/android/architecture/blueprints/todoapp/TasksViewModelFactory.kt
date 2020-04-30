package com.example.android.architecture.blueprints.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailFragment
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailViewModel
import com.example.android.architecture.blueprints.todoapp.tasks.TasksViewModel

/**
 * Created by Abdulmajeed Alyafey on 4/29/20.
 */
class TasksViewModelFactory(
        private val tasksRepository: TasksRepository // Inject repository in constructor because it may be test double.
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            with (modelClass) {
                when {
                    isAssignableFrom(TasksViewModel::class.java) ->
                        TasksViewModel(tasksRepository)
                    isAssignableFrom(TaskDetailViewModel::class.java) ->
                        TaskDetailViewModel(tasksRepository)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }  as T
}