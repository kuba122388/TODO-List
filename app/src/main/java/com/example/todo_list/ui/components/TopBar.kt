package com.example.todo_list.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todo_list.R
import com.example.todo_list.ui.theme.Dimens
import com.example.todo_list.ui.theme.OswaldFontFamily
import com.example.todo_list.ui.theme.TODOListTheme

@Composable
fun TopBar() {
    val myColors = TODOListTheme.colors
    Row {
        Column(
            modifier = Modifier.wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(Modifier)
                    Text(
                        "Welcome, Jacob!",
                        fontSize = Dimens.fontBig,
                        fontFamily = OswaldFontFamily,
                        maxLines = 1
                    )
                    HorizontalDivider(color = Color.Black, thickness = 2.dp)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.roundedSize))
                        .background(myColors.controlBackground)
                        .padding(all = Dimens.insidePadding)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = null
                    )
                }
            }
        }
    }
}
