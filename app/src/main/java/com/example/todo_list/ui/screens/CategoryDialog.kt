package com.example.todo_list.ui.screens

import TopBarPopUp
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme
import com.example.todo_list.viewModel.TaskViewModel

@Composable
fun CategoryDialog(dismiss: () -> Unit, viewModel: TaskViewModel) {
    var name by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.Gray) }

    Dialog(onDismissRequest = { dismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White, shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                TopBarPopUp(text = "Add Category", id = R.drawable.close, action = { dismiss() })

                Spacer(Modifier.size(Dimens.smallPadding))

                Text(
                    "Name: ", fontSize = Dimens.fontTiny, fontFamily = OswaldFontFamily
                )
                Spacer(Modifier.size(Dimens.tinyPadding))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(TODOListTheme.colors.controlBackground)
                        .fillMaxWidth(0.7f)
                        .padding(
                            vertical = Dimens.smallPadding, horizontal = Dimens.smallPadding
                        )
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .clip(RoundedCornerShape(5.dp))
                            .background(TODOListTheme.colors.controlBackground)
                            .fillMaxWidth(),
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        textStyle = TextStyle(
                            fontSize = 14.sp, fontFamily = OswaldFontFamily, letterSpacing = 0.5.sp
                        ),
                        maxLines = 1
                    )
                }

                Spacer(Modifier.size(Dimens.smallPadding))

                Text(
                    "Color: ", fontSize = Dimens.fontTiny, fontFamily = OswaldFontFamily
                )
                Spacer(Modifier.size(Dimens.tinyPadding))

                Row {
                    ColorPickerBox(selectedColor) { selectedColor = it }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = TODOListTheme.colors.saveButton)
                            .padding(
                                horizontal = Dimens.largePadding, vertical = Dimens.tinyPadding
                            )
                            .clickable {
                                viewModel.addCategory(
                                    Category(
                                        title = name,
                                        colorLong = selectedColor.toArgb().toLong()
                                    )
                                )
                                dismiss()
                            }
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


@Composable
fun FullColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    var red by remember { mutableIntStateOf((selectedColor.red * 255).toInt()) }
    var green by remember { mutableIntStateOf((selectedColor.green * 255).toInt()) }
    var blue by remember { mutableIntStateOf((selectedColor.blue * 255).toInt()) }

    val currentColor = Color(red, green, blue)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Wybierz kolor") },
        text = {
            Column {
                ColorPreviewBox(currentColor)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Czerwony: $red")
                Slider(
                    value = red.toFloat(),
                    onValueChange = { red = it.toInt() },
                    valueRange = 0f..255f
                )
                Text("Zielony: $green")
                Slider(
                    value = green.toFloat(),
                    onValueChange = { green = it.toInt() },
                    valueRange = 0f..255f
                )
                Text("Niebieski: $blue")
                Slider(
                    value = blue.toFloat(),
                    onValueChange = { blue = it.toInt() },
                    valueRange = 0f..255f
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onColorSelected(currentColor)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}

@Composable
fun ColorPreviewBox(color: Color) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(color)
            .border(1.dp, Color.Black)
    )
}

@Composable
fun ColorPickerBox(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(selectedColor)
            .size(40.dp)
            .clickable { showPicker = true }
    )

    if (showPicker) {
        FullColorPicker(
            selectedColor = selectedColor,
            onColorSelected = {
                onColorSelected(it)
                showPicker = false
            },
            onDismiss = { showPicker = false }
        )
    }
}
