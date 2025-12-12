package com.amonteiro.a25_12_plb_kmp.di

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.amonteiro.a25_12_plb_kmp.db.MyDatabase
import org.koin.dsl.module

//koin.iosmain.kt (dans iosMain)
actual fun databaseModule() = module {
    single {
        val driver = NativeSqliteDriver(MyDatabase.Schema, "test.db")
        MyDatabase(driver)
    }
}