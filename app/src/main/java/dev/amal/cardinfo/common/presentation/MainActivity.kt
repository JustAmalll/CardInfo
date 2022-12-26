package dev.amal.cardinfo.common.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.amal.cardinfo.common.presentation.ui.theme.CardInfoTheme
import dev.amal.cardinfo.presentation.CardInfoScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardInfoTheme {
                CardInfoScreen()
            }
        }
    }
}
