package com.amonteiro.a25_12_plb_kmp.presentation.ui.screens

import a25_12_plb_kmp.composeapp.generated.resources.Res
import a25_12_plb_kmp.composeapp.generated.resources.bt_filter
import a25_12_plb_kmp.composeapp.generated.resources.error
import a25_12_plb_kmp.composeapp.generated.resources.load_data
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.amonteiro.a25_12_plb_kmp.data.remote.WeatherBean
import com.amonteiro.a25_12_plb_kmp.presentation.ui.MyError
import com.amonteiro.a25_12_plb_kmp.presentation.ui.WeatherGallery
import com.amonteiro.a25_12_plb_kmp.presentation.viewmodel.MainViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel {MainViewModel()},
    onPictureItemClick: (WeatherBean) -> Unit = {}
) {

    var showFavorite by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Météo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                actions = {
                    IconButton(onClick = { showFavorite = !showFavorite }) {
                        Icon(
                            imageVector =
                                if (showFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "favoris"
                        )
                    }
                }

            )
        }


    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var searchText by rememberSaveable { mutableStateOf("") }

            SearchBar(
                text = searchText,
                onValueChange = { searchText = it },
                onSearchEnter = { mainViewModel.loadWeathers(searchText) }

            )

            val runInProgress by mainViewModel.runInProgress.collectAsStateWithLifecycle()
            val errorMessage by mainViewModel.errorMessage.collectAsStateWithLifecycle()

            MyError(errorMessage = errorMessage)
            AnimatedVisibility(visible = runInProgress) {
                CircularProgressIndicator()
            }

            val list = mainViewModel.dataList.collectAsStateWithLifecycle().value
                .filter { !showFavorite || it.favorite }

            WeatherGallery(urlList = list, onPictureClick = onPictureItemClick, modifier = Modifier.weight(1f))

            Row {
                Button(
                    onClick = { searchText = "" },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(Res.string.bt_filter))
                }

                Button(
                    onClick = { mainViewModel.loadWeathers(searchText) },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(Res.string.load_data))
                }

            }

        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, text: String, onValueChange: (String) -> Unit, onSearchEnter: () -> Unit) {

    TextField(
        value = text, //Valeur affichée
        onValueChange = onValueChange, //Nouveau texte entrée
        leadingIcon = { //Image d'icône
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans string.xml, R de votre package com.nom.projet
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchEnter() }),

        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        //keyboardActions = KeyboardActions(onSearch = {onSearchAction()}), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}


@Composable //Composable affichant 1 élément
fun PictureRowItem(modifier: Modifier = Modifier, data: WeatherBean, onPictureClick: () -> Unit) {
    var expended by remember { mutableStateOf(false) }

    Card {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {

            //Permission Internet nécessaire
            AsyncImage(
                model = data.weather.firstOrNull()?.icon,
                //Pour aller le chercher dans string.xml R de votre package com.nom.projet
                //contentDescription = getString(R.string.picture_of_cat),
                //En dur
                contentDescription = "une photo de chat",
                contentScale = ContentScale.FillWidth,

                //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
                //Image d'échec de chargement qui sera utilisé par la preview
                error = painterResource(Res.drawable.error),
                //Image d'attente.
                //placeholder = painterResource(R.drawable.toto),

                onError = { println(it) },
                modifier = Modifier
                    .heightIn(max = 100.dp)
                    .widthIn(max = 100.dp)
                    .clickable(onClick = onPictureClick)
            )

            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable {
                        expended = !expended
                    }) {
                Text(
                    text = data.name,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = if (expended) data.getResume() else (data.getResume().take(20) + "..."),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.animateContentSize()
                )
            }
        }
    }
}
