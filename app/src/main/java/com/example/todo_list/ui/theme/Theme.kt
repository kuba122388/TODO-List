package com.example.todo_list.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class MyColors(
    val inputFields : Color,
    val acceptButton : Color,

    val background : Color,
    val onBackground : Color,

    val controlBackground : Color,
    val onSearchBar : Color,

    val taskWOCategory : Color,
    val taskButtons : Color,

    val attachment : Color,
    val onTaskText : Color
)

val DefaultTODOListColors = MyColors(
    inputFields = InputFieldInactive,
    acceptButton = AcceptButton,

    background = Background,
    onBackground = OnBackground,

    controlBackground = ControlBackground,
    onSearchBar = OnSearchBar,

    taskWOCategory = TaskWOCategory,
    taskButtons = TaskButtons,

    attachment = Attachment,
    onTaskText = OnTaskText
)

val LocalTODOListColors = staticCompositionLocalOf{ DefaultTODOListColors }

@Composable
fun Theme(
    colors: MyColors = DefaultTODOListColors,
    content : @Composable () -> Unit
) {
    CompositionLocalProvider(LocalTODOListColors provides colors) {
        content()
    }
}

object TODOListTheme {
    val colors: MyColors
        @Composable get() = LocalTODOListColors.current
}