package com.cso.habittracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cso.habittracker.ui.theme.Border
import com.cso.habittracker.ui.theme.Destructive
import com.cso.habittracker.ui.theme.InterFamily
import com.cso.habittracker.ui.theme.Surface
import com.cso.habittracker.ui.theme.TextPrimary
import com.cso.habittracker.ui.theme.TextTertiary

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    maxLength: Int = 50
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = TextTertiary,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        BasicTextField(
            value = value,
            onValueChange = { if (it.length <= maxLength) onValueChange(it) },
            textStyle = TextStyle(
                color = TextPrimary,
                fontFamily = InterFamily,
                fontSize = 15.sp
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Surface, RoundedCornerShape(12.dp))
                .border(
                    width = if (isError) 1.5.dp else 1.dp,
                    color = if (isError) Destructive else Border,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )
    }
}
