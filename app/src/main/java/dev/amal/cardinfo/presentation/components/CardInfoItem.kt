package dev.amal.cardinfo.presentation.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import dev.amal.cardinfo.R
import dev.amal.cardinfo.presentation.Option

@Composable
fun CardInfoItem(
    title: String,
    value: String? = null,
    titleFontSize: TextUnit = 12.sp,
    isOptionValue: Boolean = false,
    option: Option? = null,
    firstOptionText: String = stringResource(id = R.string.yes),
    secondOptionText: String = stringResource(id = R.string.no)
) {
    Text(text = title, color = Color.Gray, fontSize = titleFontSize)

    if (isOptionValue && option != null) Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = if (option == Option.FIRST) Color.Black else Color.Gray)) {
                append(firstOptionText)
            }
            append(" / ")
            withStyle(SpanStyle(color = if (option == Option.SECOND) Color.Black else Color.Gray)) {
                append(secondOptionText)
            }
        },
        color = Color.Gray,
        fontSize = 16.sp
    )
    else Text(
        text = value?.replaceFirstChar { it.uppercaseChar() } ?: "-",
        color = Color.Black,
        fontSize = 16.sp
    )
}