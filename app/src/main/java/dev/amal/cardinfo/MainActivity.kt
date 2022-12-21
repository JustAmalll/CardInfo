package dev.amal.cardinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.amal.cardinfo.presentation.CardInfoScreen
import dev.amal.cardinfo.ui.theme.CardInfoTheme

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
