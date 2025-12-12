package com.amonteiro.a25_12_plb_kmp.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//Suspend sera expliqué dans le chapitre des coroutines
suspend fun main() {
    val list: List<MuseumDTO> = KtorMuseumApi.loadMuseums()
    println(list.joinToString(separator = "\n\n"))
    KtorMuseumApi.close()
}

object KtorMuseumApi {
    private const val API_URL =
        "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"

    //Création et réglage du client
    private val client = HttpClient {
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
        //engine { proxy = ProxyBuilder.http("monproxy:1234") }
    }

    //GET Le JSON reçu sera parser en List<MuseumDTO>,
    //Crash si le JSON ne correspond pas
    suspend fun loadMuseums(): List<MuseumDTO> {
        val response = client.get(API_URL) {
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
        //possibilité de typer le body
        //.body<List<MuseumDTO>>()
    }

    //POST
    suspend fun postData(newObject: MuseumDTO): MuseumDTO {
        val response = client.post(API_URL) {
            setBody(newObject)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }
        return response.body()
    }

    //Ferme le Client mais celui ci ne sera plus utilisable. Uniquement pour le main
    fun close() = client.close()

    //Avancés : Exemple avec Flow
    //fun loadMuseumsFlow() = flow<List<MuseumDTO>> {
    //    emit(client.get(API_URL).body())
    //}
}

//DATA CLASS

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
//@SuppressLint("UnsafeOptInUsageError")
@Serializable //KotlinX impose cette annotation
data class MuseumDTO(
    val objectID: Int,
    val title: String,
    val artistDisplayName: String,
    val primaryImage: String,
    //Si un attribut n'est pas toujours dans le JSON il faut lui donner une valeur par défaut
    val optionalField: String? = null
)