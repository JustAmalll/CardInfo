package dev.amal.cardinfo.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.amal.cardinfo.R
import dev.amal.cardinfo.common.formatCardNumber
import dev.amal.cardinfo.common.openMap
import dev.amal.cardinfo.common.openUrl
import dev.amal.cardinfo.common.ring
import dev.amal.cardinfo.domain.model.CardInfo
import dev.amal.cardinfo.presentation.components.CardInfoItem
import dev.amal.cardinfo.presentation.components.ConfirmationDialog
import dev.amal.cardinfo.presentation.components.EmptySearchHistory
import dev.amal.cardinfo.presentation.components.HistoryCardItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CardInfoScreen(
    viewModel: CardInfoViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.snackBarEvent.collectLatest { message ->
            snackBarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 18.dp)) {
                    GetCardInfoSection(viewModel = viewModel)
                    Spacer(Modifier.height(18.dp))
                    state.cardInfo?.let { cardInfo -> SearchResultSection(cardInfo) }
                    Spacer(Modifier.height(18.dp))
                    SearchHistorySection(
                        isSearchHistoryEmpty = state.searchHistory.isEmpty(),
                        onDeleteAllSearchHistory = { viewModel.deleteAllSearchHistory() }
                    )
                }
            }
            items(state.searchHistory) { searchHistory ->
                HistoryCardItem(cardBIN = searchHistory.cardBIN!!,
                    cardNetwork = searchHistory.scheme!!,
                    onItemClicked = { cardBin -> viewModel.getItemFromSearchHistory(cardBin) },
                    onSwipeToDelete = { cardBin -> viewModel.deleteSearchHistoryItem(cardBin) }
                )
            }
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun GetCardInfoSection(
    viewModel: CardInfoViewModel
) {
    val focusManager = LocalFocusManager.current

    Column {
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.get_card_info),
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.height(12.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.cardNumber,
            onValueChange = { if (it.length <= 16) viewModel.cardNumber = it },
            label = { Text(text = stringResource(id = R.string.bin_or_iin)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = { number -> formatCardNumber(number.text) },
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.getCardInfo()
                    focusManager.clearFocus()
                }
            )
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = stringResource(id = R.string.enter_first_digits),
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(Modifier.height(6.dp))
        OutlinedButton(modifier = Modifier.align(Alignment.End), onClick = {
            viewModel.getCardInfo()
            focusManager.clearFocus()
        }) {
            Text(text = stringResource(id = R.string.get_card_info))
        }
    }
}

@Composable
fun SearchResultSection(
    cardInfo: CardInfo
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // SCHEME / NETWORK
            CardInfoItem(
                title = stringResource(id = R.string.scheme_or_network),
                value = cardInfo.scheme
            )
            Spacer(modifier = Modifier.height(12.dp))
            // BRAND
            CardInfoItem(
                title = stringResource(id = R.string.brand),
                value = cardInfo.brand
            )
            Spacer(modifier = Modifier.height(12.dp))
            // CARD NUMBER
            Text(
                text = stringResource(id = R.string.card_number),
                color = Color.Gray,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Column {
                    CardInfoItem(
                        title = stringResource(id = R.string.length),
                        value = if (cardInfo.number?.length != null)
                            cardInfo.number.length.toString() else null,
                        titleFontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    CardInfoItem(
                        title = stringResource(id = R.string.luhn),
                        titleFontSize = 10.sp,
                        isOptionValue = true,
                        option = if (cardInfo.number?.luhn == null) null
                        else if (cardInfo.number.luhn == true) Option.FIRST else Option.SECOND,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // TYPE
            CardInfoItem(
                title = stringResource(id = R.string.type),
                isOptionValue = true,
                option = when (cardInfo.type) {
                    "debit" -> Option.FIRST
                    "credit" -> Option.SECOND
                    else -> null
                },
                firstOptionText = stringResource(id = R.string.debit),
                secondOptionText = stringResource(id = R.string.credit)
            )
        }
        Column(modifier = Modifier.padding(horizontal = 30.dp)) {
            // PREPAID
            CardInfoItem(
                title = stringResource(id = R.string.prepaid),
                isOptionValue = true,
                option = when (cardInfo.prepaid) {
                    true -> Option.FIRST
                    false -> Option.SECOND
                    else -> null
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            // COUNTRY
            CardInfoItem(
                title = stringResource(id = R.string.country),
                value = if (cardInfo.country != null)
                    "${cardInfo.country.emoji} ${cardInfo.country.name}" else null
            )
            if (cardInfo.country?.latitude != null && cardInfo.country.longitude != null) {
                Text(
                    modifier = Modifier.clickable {
                        context.openMap(
                            latitude = cardInfo.country.latitude,
                            longitude = cardInfo.country.longitude
                        )
                    }, text = buildAnnotatedString {
                        append(stringResource(id = R.string.latitude))
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append(cardInfo.country.latitude.toString())
                        }
                        append(", ")
                        append(stringResource(id = R.string.longitude))
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append(cardInfo.country.longitude.toString())
                        }
                        append(")")
                    },
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // BANK
            CardInfoItem(
                title = stringResource(id = R.string.bank),
                value = cardInfo.bank?.name
            )
            if (cardInfo.bank?.url != null) Text(
                modifier = Modifier.clickable { context.openUrl(cardInfo.bank.url) },
                text = cardInfo.bank.url,
                color = Color.Blue,
                fontSize = 14.sp
            )
            if (cardInfo.bank?.phone != null) Text(
                modifier = Modifier.clickable { context.ring(cardInfo.bank.phone) },
                text = cardInfo.bank.phone,
                color = Color.Black,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun SearchHistorySection(
    isSearchHistoryEmpty: Boolean, onDeleteAllSearchHistory: () -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) ConfirmationDialog(onDeleteClicked = onDeleteAllSearchHistory,
        onDialogDismiss = { openDialog = false })
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.history),
            style = MaterialTheme.typography.h5
        )
        TextButton(onClick = { openDialog = true }) {
            Text(text = stringResource(id = R.string.clear_all))
        }
    }
    if (isSearchHistoryEmpty) EmptySearchHistory()
}