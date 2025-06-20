package com.example.todo_list.ui.screens

import TopBarPopUp
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme

@Composable
fun TaskDialog(dismiss: () -> Unit, categoryList: List<Category>) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }
    var notification by remember { mutableStateOf(false) }

    var attachments = remember { mutableStateListOf<Uri>() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            attachments.add(it)
        }
    }

    Dialog(onDismissRequest = { dismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                TopBarPopUp(
                    text = "Add Task",
                    id = R.drawable.close,
                    action = { dismiss() }
                )

                Spacer(Modifier.size(Dimens.smallPadding))

                Text(
                    "Title: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
                BasicTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .fillMaxWidth()
                        .padding(vertical = Dimens.smallPadding, horizontal = Dimens.tinyPadding),
                    value = title,
                    onValueChange = { title = it },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = OswaldFontFamily,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(Modifier.size(Dimens.smallPadding))

                Text(
                    "Description",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )

                BasicTextField(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(vertical = Dimens.smallPadding, horizontal = Dimens.tinyPadding),
                    value = description,
                    onValueChange = { description = it },
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = OswaldFontFamily,
                        fontWeight = FontWeight.Light
                    )
                )
                Spacer(Modifier.size(Dimens.smallPadding))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Execution time:",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(Modifier.size(Dimens.smallPadding))
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
                        DateTimePickerSample()
                    }
                }
                Spacer(Modifier.size(Dimens.smallPadding))



                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Category:",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(modifier = Modifier.size(Dimens.smallPadding))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(TODOListTheme.colors.controlBackground)
                            .fillMaxWidth()
                            .padding(
                                vertical = Dimens.smallPadding,
                                horizontal = Dimens.smallPadding
                            )
                            .clickable { expanded = !expanded }
                    ) {
                        Column {
                            Text(
                                text = if (selectedOptionText == "") "<None>" else selectedOptionText
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("<None>") },
                                    onClick = {
                                        selectedOptionText = ""
                                        expanded = false
                                    }
                                )

                                categoryList.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(text = category.Title) },
                                        onClick = {
                                            selectedOptionText = category.Title
                                            expanded = false
                                        },
                                        modifier = Modifier.background(color = category.Color)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.size(Dimens.smallPadding))



                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Notification:",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(Modifier.size(Dimens.smallPadding))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(TODOListTheme.colors.controlBackground)
                            .size(30.dp)
                            .clickable { notification = !notification },
                        contentAlignment = Alignment.Center
                    ) {
                        if (notification) Image(
                            painter = painterResource(id = R.drawable.ok),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.size(Dimens.smallPadding))




                Row {
                    Text(
                        "Attachments: ",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                    Spacer(modifier = Modifier.size(Dimens.tinyPadding))
                    Text(
                        "(${attachments.size}/3)",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily,
                        color = TODOListTheme.colors.attachment
                    )
                }
                Spacer(modifier = Modifier.size(Dimens.tinyPadding))

                attachments.forEach { uri ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    attachments.remove(uri)
                                },
                            painter = painterResource(id = R.drawable.delete_red),
                            contentDescription = null
                        )

                        Text(uri.path!!)
                        Image(
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, context.contentResolver.getType(uri))
                                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    }

                                    try {
                                        context.startActivity(intent)
                                    } catch (e: ActivityNotFoundException) {
                                        Toast
                                            .makeText(
                                                context,
                                                "Brak aplikacji do otwarcia tego pliku",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                },
                            painter = painterResource(id = R.drawable.open),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.size(Dimens.smallPadding))

                }
                if (attachments.size < 3) Row(
                    modifier = Modifier.clickable {
                        launcher.launch("*/*")
                    },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.plus_attachment),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(Dimens.smallPadding))
                    Text(
                        "Add attachment ",
                        fontSize = Dimens.fontTiny,
                        fontFamily = OswaldFontFamily
                    )
                }

                Spacer(modifier = Modifier.size(Dimens.smallPadding))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = TODOListTheme.colors.saveButton)
                            .padding(
                                horizontal = Dimens.largePadding,
                                vertical = Dimens.tinyPadding
                            )
                    ) {
                        Text(
                            "SAVE",
                            fontSize = Dimens.fontSmall,
                            fontFamily = OswaldFontFamily,
                            color = TODOListTheme.colors.onButtons
                        )
                    }
                }

            }
        }
    }
}