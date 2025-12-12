package com.amonteiro.a25_12_plb_kmp

import android.app.Application
import com.amonteiro.a25_12_plb_kmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@MyApplication)
        }
    }
}