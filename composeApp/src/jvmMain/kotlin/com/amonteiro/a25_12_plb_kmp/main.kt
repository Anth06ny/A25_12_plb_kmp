package com.amonteiro.a25_12_plb_kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.amonteiro.a25_12_plb_kmp.di.initKoin

fun main() = application {

    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "A25_12_plb_kmp",
    ) {
        App()
    }
}
