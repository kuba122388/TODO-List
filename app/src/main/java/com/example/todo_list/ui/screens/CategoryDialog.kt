package com.example.todo_list.ui.screens

import TopBarPopUp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todo_list.R
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme

@Composable
fun CategoryDialog(dismiss: () -> Unit) {
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
                    text = "Add Category",
                    id = R.drawable.close,
                    action = { dismiss() }
                )

                Spacer(Modifier.size(Dimens.smallPadding))

                Text(
                    "Name: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
                Text(
                    "Uwuwwewew ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )

                Spacer(Modifier.size(Dimens.smallPadding))
                Text(
                    "Color: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
                Text(
                    "In the futre: ",
                    fontSize = Dimens.fontTiny,
                    fontFamily = OswaldFontFamily
                )
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