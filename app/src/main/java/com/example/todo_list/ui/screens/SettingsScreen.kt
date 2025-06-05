package com.example.todo_list.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todo_list.R
import com.example.todo_list.ui.components.TopBar
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme

@Composable
fun SettingsScreen(navController: NavController) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        TopBar("Settings", R.drawable.back, navController, "BACK")

        Column {
            Spacer(modifier = Modifier.size(Dimens.mediumPadding))
            Text(
                text = "General",
                fontSize = Dimens.fontSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = OswaldFontFamily
            )
            Spacer(Modifier.size(Dimens.smallPadding))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Your name: ",
                    fontSize = Dimens.fontSmall,
                    fontFamily = OswaldFontFamily,
                    fontWeight = FontWeight.Light
                )

            }

            Spacer(modifier = Modifier.size(Dimens.largePadding))

            Text(
                text = "Tasks",
                fontSize = Dimens.fontSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = OswaldFontFamily
            )
            Spacer(Modifier.size(Dimens.smallPadding))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)

                )
                Spacer(modifier = Modifier.size(Dimens.smallPadding))
                Text(
                    text = "Hide done tasks",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
            }
            Spacer(Modifier.size(Dimens.mediumPadding))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)

                )
                Spacer(modifier = Modifier.size(Dimens.smallPadding))
                Text(
                    text = "24-hour format",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
            }

            Spacer(modifier = Modifier.size(Dimens.largePadding))

            Text(
                text = "Notifications",
                fontSize = Dimens.fontSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = OswaldFontFamily
            )
            Spacer(Modifier.size(Dimens.smallPadding))
            Text(
                text = "Send notification XXXXX min before",
                fontSize = Dimens.fontTiny,
                fontFamily = OswaldFontFamily
            )

            Spacer(modifier = Modifier.size(Dimens.largePadding))

            Text(
                text = "Categories",
                fontSize = Dimens.fontSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = OswaldFontFamily
            )
            Spacer(Modifier.size(Dimens.smallPadding))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Delete category: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )

                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.delete_red),
                    contentDescription = null,
                )
            }
        }


    }
}