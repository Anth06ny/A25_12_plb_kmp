package com.amonteiro.a25_12_plb_kmp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amonteiro.a25_12_plb_kmp.data.remote.DescriptionBean
import com.amonteiro.a25_12_plb_kmp.data.remote.KtorWeatherAPI
import com.amonteiro.a25_12_plb_kmp.data.remote.TempBean
import com.amonteiro.a25_12_plb_kmp.data.remote.WeatherBean
import com.amonteiro.a25_12_plb_kmp.data.remote.WindBean
import com.amonteiro.a25_12_plb_kmp.db.MyDatabase
import com.amonteiro.a25_12_plb_kmp.di.initKoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

suspend fun main() {

    val koin = initKoin()

    val viewModel = koin.get<MainViewModel>()
    viewModel.loadWeathers("")
    //viewModel.loadWeathers("Paris")

    while (viewModel.runInProgress.value) {
        delay(500)  //attente
    }
    //Affichage de la liste et du message d'erreur
    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}")

}

class MainViewModel(val ktorWeatherAPI: KtorWeatherAPI, myDatabase: MyDatabase) : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<WeatherBean>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")
    private val weatherStorageQueries = myDatabase.weatherStorageQueries

    private val jsonParser = Json { prettyPrint = true }

    init {
        loadWeathers("Paris")
    }

    fun toggleFavorite(id: Int) {
        dataList.update {
            //Crée une nouvelle liste Avec une nouvelle référence pour l'objet qui a changé
            it.map { w -> if (w.id == id) w.copy(favorite = !w.favorite) else w }
        }
    }

    fun loadWeathers(cityName: String) {
        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataList.value = ktorWeatherAPI.loadWeathers(cityName)

                weatherStorageQueries.transaction {

                    weatherStorageQueries.insertOrReplacePhotographer(
                        cityName,
                        jsonParser.encodeToString(dataList.value)
                    )
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"

                weatherStorageQueries.selectWeatherById(cityName).executeAsList().firstOrNull()?.let {
                    dataList.value = jsonParser.decodeFromString<List<WeatherBean>>(it.json)
                }
            }
            finally {
                runInProgress.value = false
            }
        }

    }

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
            WeatherBean(
                id = 1,
                name = "Paris",
                main = TempBean(temp = 18.5),
                weather = listOf(
                    DescriptionBean(description = "ciel dégagé", icon = "https://picsum.photos/200")
                ),
                wind = WindBean(speed = 5.0)
            ),
            WeatherBean(
                id = 2,
                name = "Toulouse",
                main = TempBean(temp = 22.3),
                weather = listOf(
                    DescriptionBean(description = "partiellement nuageux", icon = "https://picsum.photos/201")
                ),
                wind = WindBean(speed = 3.2)
            ),
            WeatherBean(
                id = 3,
                name = "Toulon",
                main = TempBean(temp = 25.1),
                weather = listOf(
                    DescriptionBean(description = "ensoleillé", icon = "https://picsum.photos/202")
                ),
                wind = WindBean(speed = 6.7)
            ),
            WeatherBean(
                id = 4,
                name = "Lyon",
                main = TempBean(temp = 19.8),
                weather = listOf(
                    DescriptionBean(description = "pluie légère", icon = "https://picsum.photos/203")
                ),
                wind = WindBean(speed = 4.5)
            )
        ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
    }
}