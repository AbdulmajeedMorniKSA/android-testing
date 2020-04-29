package com.example.android.architecture.blueprints.todoapp.util

import androidx.fragment.app.Fragment
import com.example.android.architecture.blueprints.todoapp.data.Injection
import com.example.android.architecture.blueprints.todoapp.TasksViewModelFactory

/**
 * Created by Abdulmajeed Alyafey on 4/29/20.
 */

fun Fragment.getViewModelFactory(): TasksViewModelFactory =
        TasksViewModelFactory(Injection.provideRepository())