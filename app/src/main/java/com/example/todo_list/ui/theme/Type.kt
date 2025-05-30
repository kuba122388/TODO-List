package com.example.todo_list.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.todo_list.R

object OswaldFontFamily {
    val Oswald = FontFamily(
        Font(R.font.oswald_light, FontWeight.Light),
        Font(R.font.oswald_regular, FontWeight.Normal),
        Font(R.font.oswald_bold, FontWeight.Bold)
    )
}
