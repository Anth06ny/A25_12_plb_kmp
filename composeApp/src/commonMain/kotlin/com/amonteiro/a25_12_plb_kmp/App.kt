package com.amonteiro.a25_12_plb_kmp

import androidx.compose.runtime.Composable
import com.amonteiro.a25_12_plb_kmp.presentation.AppNavigation
import com.amonteiro.a25_12_plb_kmp.presentation.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        AppNavigation()
    }
}