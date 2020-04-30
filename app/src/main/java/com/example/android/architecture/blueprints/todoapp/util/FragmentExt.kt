package com.example.android.architecture.blueprints.todoapp.util

import androidx.fragment.app.Fragment
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.TasksViewModelFactory

/**
 * Created by Abdulmajeed Alyafey on 4/29/20.
 */

fun Fragment.getViewModelFactory(): TasksViewModelFactory =
        TasksViewModelFactory(ServiceLocator.provideRepository())