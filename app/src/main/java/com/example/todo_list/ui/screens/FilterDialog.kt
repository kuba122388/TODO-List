package com.example.todo_list.ui.screens

import TopBarPopUp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColor
import com.example.todo_list.R
import com.example.todo_list.data.model.Category
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily

@Composable
fun FilterDialog(
    categoryList: List<Category>,
    dismiss: () -> Unit,
    changeCategory: (Int) -> Unit
) {
    Dialog(onDismissRequest = { dismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopBarPopUp(
                    text = "Filter",
                    id = R.drawable.close,
                    action = { dismiss() }
                )
                Spacer(Modifier.size(Dimens.mediumPadding))
                if(categoryList.isEmpty()) Text(text = "No categories found", fontFamily = OswaldFontFamily, fontSize = Dimens.fontSmall)
                LazyColumn(
                    modifier = Modifier.sizeIn(maxHeight = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(categoryList) { category ->
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(40.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    Color(category.colorLong).copy(alpha = 0.8f)
                                )
                                .clickable {
                                    changeCategory(category.id)
                                    dismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = category.title)
                        }
                    }
                }
                Spacer(Modifier.size(Dimens.mediumPadding))
            }
        }
    }
}