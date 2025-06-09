package com.example.todo_list.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todo_list.sharedPreferences.SharedPreferencesHelper
import com.example.todo_list.ui.screens.SettingsScreen
import com.example.todo_list.ui.screens.TaskListScreen
import com.example.todo_list.viewModel.TaskViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: TaskViewModel, startTaskId: Int) {
    val context = LocalContext.current
    val sharedPreferencesHelper = SharedPreferencesHelper(context);

    NavHost(
        navController = navController,
        startDestination = Routes.TaskList.route
    ) {
        composable(route = Routes.TaskList.route) {
            TaskListScreen(
                navController = navController,
                sharedPreferencesHelper = sharedPreferencesHelper,
                viewModel = viewModel,
                startTaskId = startTaskId
            )
        }
        composable(route = Routes.Settings.route) {
            SettingsScreen(navController = navController, sharedPreferencesHelper, viewModel)
        }
    }
}
