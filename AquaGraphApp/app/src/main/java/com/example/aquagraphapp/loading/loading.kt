package com.example.aquagraphapp.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowLoadingCircle(loading: Boolean) {
    if (!loading) return
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .width(64.dp)
//                    .background(MaterialTheme.colorScheme.background),
//                color = MaterialTheme.colorScheme.primary,
//            )
        }
    }
}