package com.amonteiro.a25_12_plb_kmp.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.amonteiro.a25_12_plb_kmp.data.remote.DescriptionBean
import com.amonteiro.a25_12_plb_kmp.data.remote.TempBean
import com.amonteiro.a25_12_plb_kmp.data.remote.WeatherBean
import com.amonteiro.a25_12_plb_kmp.data.remote.WindBean
import com.amonteiro.a25_12_plb_kmp.di.apiModule
import com.amonteiro.a25_12_plb_kmp.di.viewModelModule
import com.amonteiro.a25_12_plb_kmp.presentation.ui.screens.DetailScreen
import com.amonteiro.a25_12_plb_kmp.presentation.ui.screens.SearchScreen
import com.amonteiro.a25_12_plb_kmp.presentation.ui.theme.AppTheme
import com.amonteiro.a25_12_plb_kmp.presentation.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL, locale = "fr"
)
@Composable
fun SearchScreenWithFakeDataPreview() {
    //Il faut remplacer NomVotreAppliTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    val context = LocalContext.current

    KoinApplicationPreview(application = {
        androidContext(context)
        modules(viewModelModule, apiModule)
    }) {
        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                val mainViewModel: MainViewModel = koinViewModel<MainViewModel>()
                mainViewModel.loadFakeData(true, "un message d'erreur")
                SearchScreen(
                    modifier = Modifier.padding(innerPadding),
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
            or android.content.res.Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DetailScreenPreview() {
    val context = LocalContext.current

    KoinApplicationPreview(application = {
        androidContext(context)
        modules(viewModelModule, apiModule)
    }) {
        AppTheme {
            DetailScreen(
                //jeu de donnée pour la Preview
                data = WeatherBean(
                    id = 2,
                    name = "Toulouse",
                    main = TempBean(temp = 22.3),
                    weather = listOf(
                        DescriptionBean(description = "partiellement nuageux", icon = "https://picsum.photos/201")
                    ),
                    wind = WindBean(speed = 3.2),
                    favorite = true
                ),
                showBackIcon = true,
                mainViewModel = koinViewModel<MainViewModel>()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyErrorPreview() {

    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                //Je mets 2 versions pour tester avec et sans message d'erreur
                MyError(errorMessage = "Avec message d'erreur")
                Text("Sans erreur : ")
                MyError(errorMessage = "")
                Text("----------")
            }
        }
    }
}