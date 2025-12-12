package com.amonteiro.a25_12_plb_kmp.di

import com.amonteiro.a25_12_plb_kmp.data.remote.KtorWeatherAPI
import com.amonteiro.a25_12_plb_kmp.presentation.viewmodel.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

//Si besoin du contexte, pour le passer en paramètre au lancement de Koin
fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(apiModule, viewModelModule)
    }.koin

// Version pour iOS et Desktop
fun initKoin() = initKoin {}

//------------------------
//DECLARATION DES MODULES
//------------------------
val apiModule = module {
    //Création d'un singleton pour le client HTTP
    single {
        HttpClient {
            install(Logging) {
                //(import io.ktor.client.plugins.logging.Logger)
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.INFO  // TRACE, HEADERS, BODY, etc.
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }
    }

    //Création d'un singleton pour les repository.
    //Get() injectera les objets déjà connues par koin, ici le HttpClient
    //single { KtorWeatherAPI(get()) }

    //Version avec injection automatique des objets connues
    singleOf(::KtorWeatherAPI)
}

//Version spécifique au ViewModel
val viewModelModule = module {

    viewModelOf(::MainViewModel)
}