package com.amonteiro.a25_12_plb_kmp.data.remote

import com.amonteiro.a25_12_plb_kmp.BuildConfig
import com.amonteiro.a25_12_plb_kmp.di.initKoin
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

suspend fun main() {

    val koin = initKoin()
    val ktorWeatherAPI = koin.get<KtorWeatherAPI>()

    for (weather in ktorWeatherAPI.loadWeathers("nice")) {
        println(weather.getResume())
    }

    ktorWeatherAPI.close()
}

class KtorWeatherAPI(val httpClient: HttpClient) {

    suspend fun loadWeathers(cityName: String): List<WeatherBean> {

        val response = httpClient.get("https://api.openweathermap.org/data/2.5/find?q=$cityName&appid=${BuildConfig.WEATHER_API_KEY}&units=metric&lang=fr")

        delay(2000)

        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }


        return response.body<WeatherAPIResult>().list.onEach {w->
            w.weather.forEach {
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }
    }

    fun close() {
        httpClient.close()
    }
}

/* -------------------------------- */
// WEATHER
/* -------------------------------- */
//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable
data class WeatherAPIResult(val list: List<WeatherBean>)

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable
data class WeatherBean(
    val id: Int, val name: String, var main: TempBean,
    var weather: List<DescriptionBean>,
    var wind: WindBean,
    val favorite : Boolean = false
) {

    fun getResume() = """
            Il fait ${main.temp}° à $name (id=$id) avec un vent de ${wind.speed} m/s
            -Description : ${weather.firstOrNull()?.description ?: "-"}
            -Icône : ${weather.firstOrNull()?.icon ?: "-"}
        """.trimIndent()

}

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable
data class TempBean(var temp: Double)

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable
data class DescriptionBean(var description: String, var icon: String)

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable
data class WindBean(var speed: Double)