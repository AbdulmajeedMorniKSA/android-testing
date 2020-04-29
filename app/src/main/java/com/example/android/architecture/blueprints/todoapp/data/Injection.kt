package com.example.android.architecture.blueprints.todoapp.data

import com.example.android.architecture.blueprints.todoapp.TodoApplication
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository

object Injection {

    fun provideRepository() = DefaultTasksRepository.getInstance(
            TodoApplication.getInstance()
    )

}