/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.architecture.blueprints.todoapp.tasks

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.architecture.blueprints.todoapp.R
import com.google.android.material.navigation.NavigationView

/**
 * Main activity for the todoapp. Holds the Navigation Host Fragment and the Drawer, Toolbar, etc.
 */
class TasksActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_act)
        setupNavigationDrawer()
        setSupportActionBar(findViewById(R.id.toolbar))

/*        val navController: NavController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration.Builder(R.id.tasks_fragment_dest, R.id.statistics_fragment_dest)
                .setDrawerLayout(drawerLayout)
                .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)*/

        // Check
        // https://developer.android.com/guide/navigation/navigation-ui#Tie-navdrawer

        // 1- Get the nav controller
        val navController = findNavController(R.id.nav_host_fragment)

        // 2- Set up AppBarConfiguration that used to manage the behavior of the Navigation button
        // in the upper-left corner of your app's display area.

        // The Up button will not be displayed when on these destinations.(Top-level destinations)
        appBarConfiguration =
                AppBarConfiguration.Builder(R.id.tasks_fragment_dest)
                        .setDrawerLayout(drawerLayout) // connect the DrawerLayout to your navigation graph by passing it to AppBarConfiguration, //
                        .build()
         // You can use it directly without specifying destinations
         // val appBarConfiguration = AppBarConfiguration(navController.graph)

        // 3- To add navigation support to the default action bar
        setupActionBarWithNavController(navController, appBarConfiguration)

        // 4- Link nav view with nav controller.
        findViewById<NavigationView>(R.id.nav_view)
                .setupWithNavController(navController)

        showToast()
    }

    private fun showToast() {
        // Test showing custom toast
        val container = findViewById<ViewGroup>(R.id.toast_container)
        val customLayout = layoutInflater.inflate(R.layout.toast_view, container)
        val toastMsg = customLayout.findViewById<TextView>(R.id.toastMsg)
        //toastMsg.text = "This is a custom toast"
        with (Toast(this)) {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = Toast.LENGTH_LONG
            view = customLayout
            show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
            .apply {
                setStatusBarBackground(R.color.colorPrimaryDark)
            }
    }
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3
