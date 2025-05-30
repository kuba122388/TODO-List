package com.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.todo_list.ui.theme.TODOListTheme
import com.example.todo_list.ui.theme.Theme
import com.example.todo_list.navigation.NavGraph
import com.example.todo_list.ui.theme.Dimens

@Composable
fun App() {
    Theme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = TODOListTheme.colors.background
        ) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(
                        top = Dimens.mediumPadding,
                        start = Dimens.largePadding,
                        end = Dimens.mediumPadding,
                        bottom = Dimens.mediumPadding
                    )
            ) {
                NavGraph(navController = navController)
            }
        }
    }
}
