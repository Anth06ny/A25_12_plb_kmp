package com.amonteiro.a25_12_plb_kmp.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.amonteiro.a25_12_plb_kmp.db.MyDatabase
import org.koin.dsl.module

actual fun databaseModule() = module {
    single {
        //Penser à faire un Build -> "Compile all Sources" pour générer le MyDatabase
        val driver = AndroidSqliteDriver(MyDatabase.Schema, get(), "test.db")
        MyDatabase(driver)
    }
}