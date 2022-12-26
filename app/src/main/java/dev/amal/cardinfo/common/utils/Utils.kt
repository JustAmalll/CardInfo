package dev.amal.cardinfo.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

fun String.removeWhitespaces() = replace(" ", "")

fun Context.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("https://$url")
    this.startActivity(intent)
}

fun Context.ring(number: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$number")
    this.startActivity(intent)
}

fun Context.openMap(latitude: Double, longitude: Double) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("geo:$latitude,$longitude")
    intent.setPackage("com.google.android.apps.maps")
    this.startActivity(intent)
}

fun formatCardNumber(string: String): TransformedText {

    var cardNumber = ""

    for (i in string.indices) {
        cardNumber += string[i]
        if (i % 4 == 3 && i != 15) cardNumber += " "
    }

    val creditCardOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int =
            if (offset <= 3) offset
            else if (offset <= 7) offset + 1
            else if (offset <= 11) offset + 2
            else if (offset <= 16) offset + 3
            else 19

        override fun transformedToOriginal(offset: Int): Int =
            if (offset <= 4) offset
            else if (offset <= 9) offset - 1
            else if (offset <= 14) offset - 2
            else if (offset <= 19) offset - 3
            else 16
    }

    return TransformedText(AnnotatedString(cardNumber), creditCardOffsetTranslator)
}