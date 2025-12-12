package com.amonteiro.a25_12_plb_kmp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amonteiro.a25_12_plb_kmp.data.remote.WeatherBean
import com.amonteiro.a25_12_plb_kmp.presentation.ui.screens.PictureRowItem

@Composable
actual fun WeatherGallery(
    modifier: Modifier,
    urlList: List<WeatherBean>,
    onPictureClick: (WeatherBean) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier
    ) {
        items(urlList.size) {
            PictureRowItem(
                data = urlList[it],
                onPictureClick = { onPictureClick(urlList[it]) })
        }
    }
}