package dev.amal.cardinfo.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import dev.amal.cardinfo.R
import dev.amal.cardinfo.common.formatCardNumber
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.*

@Composable
fun HistoryCardItem(
    cardBIN: String,
    cardNetwork: String,
    onSwipeToDelete: (cardBin: String) -> Unit,
    onItemClicked: (cardBin: String) -> Unit
) {
    val swipeToDelete = SwipeAction(
        icon = {
            Icon(
                modifier = Modifier.padding(end = 20.dp),
                imageVector = Icons.Default.Delete,
                tint = MaterialTheme.colors.onError,
                contentDescription = stringResource(id = R.string.delete_icon)
            )
        },
        background = MaterialTheme.colors.error,
        onSwipe = { onSwipeToDelete(cardBIN) }
    )

    SwipeableActionsBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clickable { onItemClicked(cardBIN) },
        startActions = listOf(swipeToDelete),
        swipeThreshold = 90.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 12.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_credit_card),
                contentDescription = stringResource(id = R.string.credit_card_icon)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = buildAnnotatedString {
                    append(cardNetwork.uppercase(Locale.getDefault()))
                    append(" - ")
                    append(formatCardNumber(cardBIN).text)
                }
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 24.dp))
}