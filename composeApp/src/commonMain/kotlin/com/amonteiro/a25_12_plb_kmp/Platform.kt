package com.amonteiro.a25_12_plb_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform