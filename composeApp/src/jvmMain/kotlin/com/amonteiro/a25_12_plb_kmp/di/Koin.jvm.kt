package com.amonteiro.a25_12_plb_kmp.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.amonteiro.a25_12_plb_kmp.db.MyDatabase
import org.koin.dsl.module

actual fun databaseModule() = module {
    single {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
            MyDatabase.Schema.create(it)
        }
        MyDatabase(driver)
    }
}