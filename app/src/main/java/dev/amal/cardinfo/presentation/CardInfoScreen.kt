package dev.amal.cardinfo.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CardInfoScreen(
    viewModel: CardInfoViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(value = viewModel.cardNumber, onValueChange = { viewModel.cardNumber = it })
            Button(onClick = { viewModel.getCardInfo() }) {
                Text(text = "Get Card Info")
            }
            state.cardInfo?.let { cardInfo ->
                Text(text = "bank name ${cardInfo.bank.name}")
                Text(text = "card brand ${cardInfo.brand}")
                Text(text = "country ${cardInfo.country.name}")
                Text(text = "card type ${cardInfo.type}")
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
}