package com.example.todo_list.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.InputFieldInactive
import com.example.todo_list.ui.theme.OnSearchBar
import com.example.todo_list.ui.theme.OswaldFontFamily

@Composable
fun SearchModule(searchText: String, onSearchChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Find your task",
            fontSize = Dimens.fontMedium,
            fontFamily = OswaldFontFamily,
            fontWeight = FontWeight.Light,
            maxLines = 1,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.size(Dimens.tinyPadding))
        CustomSearchBar(query = searchText, onQueryChange = onSearchChange)
    }
}

@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.roundedSize))
            .background(InputFieldInactive)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = OnSearchBar
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = OswaldFontFamily,
                    fontWeight = FontWeight.Light,
                    color = OnSearchBar
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            "What are we looking for? :)",
                            fontSize = Dimens.fontTiny,
                            fontFamily = OswaldFontFamily,
                            fontWeight = FontWeight.Light,
                            color = OnSearchBar,
                            letterSpacing = 0.5.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}
