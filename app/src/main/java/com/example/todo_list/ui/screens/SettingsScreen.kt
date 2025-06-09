package com.example.todo_list.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo_list.R
import com.example.todo_list.sharedPreferences.SharedPreferencesHelper
import com.example.todo_list.ui.components.TopBar
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme
import com.example.todo_list.viewModel.TaskViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    sharedPreferencesHelper: SharedPreferencesHelper,
    viewModel: TaskViewModel
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var name by remember {
        mutableStateOf(
            sharedPreferencesHelper.getUserName()
        )
    }

    val categories by viewModel.categories.collectAsState()
    var selectedCategory by remember { mutableStateOf("<None>") }

    var hideDoneTasks by remember { mutableStateOf(sharedPreferencesHelper.getHideDoneTask()) }
    var fullHour by remember { mutableStateOf(sharedPreferencesHelper.getFullHourFormat()) }
    var notificationTime by remember { mutableStateOf(sharedPreferencesHelper.getNotificationTime()) }

    var expanded by remember { mutableStateOf(false) }


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
            ) {
                Text(
                    text = "Your name: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
                Spacer(Modifier.size(Dimens.tinyPadding))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .fillMaxWidth()
                        .padding(
                            vertical = Dimens.smallPadding,
                            horizontal = Dimens.smallPadding
                        )
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(TODOListTheme.colors.controlBackground)
                            .fillMaxWidth(),
                        value = name,
                        onValueChange = {
                            name = it;
                            if (name.isEmpty()) sharedPreferencesHelper.setUserName("Mysterious")
                            else sharedPreferencesHelper.setUserName(it)
                        },
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = OswaldFontFamily,
                            letterSpacing = 0.5.sp
                        ),
                        maxLines = 1
                    )
                }

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
                        .clickable {
                            hideDoneTasks = !hideDoneTasks;
                            sharedPreferencesHelper.setHideDoneTask(hideDoneTasks)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (hideDoneTasks) Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ok),
                        contentDescription = null
                    )
                }
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
                        .clickable {
                            fullHour = !fullHour;
                            sharedPreferencesHelper.getFullHourFormat(fullHour)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (fullHour) Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ok),
                        contentDescription = null
                    )
                }
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Send notification ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
                Spacer(Modifier.size(Dimens.tinyPadding))
                BasicTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .width(70.dp)
                        .padding(
                            vertical = Dimens.tinyPadding,
                            horizontal = Dimens.smallPadding
                        ),
                    value = notificationTime,
                    onValueChange = {
                        notificationTime = it.filter { c -> c.isDigit() }
                        if (notificationTime == "") {
                            notificationTime = "0"
                            mutableStateOf(
                                sharedPreferencesHelper.setNotificationTime(
                                    notificationTime
                                )
                            )
                        }
                        mutableStateOf(sharedPreferencesHelper.setNotificationTime(notificationTime))
                    },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = OswaldFontFamily,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(Modifier.size(Dimens.tinyPadding))
                Text(
                    text = " min before",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
            }
            Spacer(modifier = Modifier.size(Dimens.largePadding))

            Text(
                text = "Categories",
                fontSize = Dimens.fontSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = OswaldFontFamily
            )
            Spacer(Modifier.size(Dimens.smallPadding))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Delete category: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
                Spacer(Modifier.size(Dimens.smallPadding))
                Box(
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .weight(0.9f)
                        .padding(
                            vertical = Dimens.smallPadding,
                            horizontal = Dimens.smallPadding
                        )

                ) {
                    Text(
                        text = if (selectedCategory == "") "<None>" else selectedCategory
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("<None>") },
                            onClick = {
                                selectedCategory = ""
                                expanded = false
                            }
                        )

                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(text = category.title) },
                                onClick = {
                                    selectedCategory = category.title
                                    expanded = false
                                },
                                modifier = Modifier.background(color = Color(category.colorLong))
                            )
                        }
                    }
                }
                Spacer(Modifier.size(Dimens.smallPadding))
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            categories
                                .find { category -> category.title == selectedCategory }
                                ?.let { viewModel.deleteCategory(it) }
                            if (selectedCategory != "<None>") Toast
                                .makeText(
                                    context,
                                    "Category $selectedCategory deleted successfully",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            selectedCategory = "<None>"
                        },
                    painter = painterResource(id = R.drawable.delete_red),
                    contentDescription = null
                )
            }
        }


    }
}