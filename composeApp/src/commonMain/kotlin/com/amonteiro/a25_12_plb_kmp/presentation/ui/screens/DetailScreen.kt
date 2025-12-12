package com.amonteiro.a25_12_plb_kmp.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.amonteiro.a25_12_plb_kmp.data.remote.WeatherBean
import com.amonteiro.a25_12_plb_kmp.presentation.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable //id du WeatherBean à afficher
fun DetailScreen(
    modifier: Modifier = Modifier,
    data: WeatherBean,
    onBackClick: () -> Unit = {},
    showBackIcon: Boolean = false,
    mainViewModel: MainViewModel
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Mon titre") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                //Icone à gauche
                navigationIcon = {
                    if (showBackIcon) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { mainViewModel.toggleFavorite(data.id) }) {
                        Icon(
                            imageVector =
                                if (data.favorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "favoris"
                        )
                    }
                }

            )
        }


    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            Text(
                text = data.name,
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            )

            AsyncImage(
                model = data.weather.firstOrNull()?.icon ?: "",
                contentDescription = data.weather.firstOrNull()?.description ?: "",
                contentScale = ContentScale.FillWidth,
                //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
                //Image d'échec de chargement qui sera utilisé par la preview
                //error = painterResource(R.drawable.toto),
                //Image d'attente.
                //placeholder = painterResource(R.drawable.toto),
                onError = { println(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Text(
                text = data.getResume(),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(16.dp))

            Button(
                onClick = onBackClick,
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                modifier = Modifier
                    .padding(8.dp)
                    .align(CenterHorizontally)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Retour")
            }
        }
    }
}