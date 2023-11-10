package com.example.aquagraphapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme
import com.example.aquagraphapp.models.QualityModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var point = Pair(27.587433, 53.919585)
        lifecycleScope.launch {
            val data = async{ com.example.aquagraphapp.qualityData.getQualityData(point, applicationContext)}
            val dataForTable = data.await()
            Log.d("coroutine", "$dataForTable")

            setContent {
                AquaGraphAppTheme {
                    MaterialTheme.colorScheme.primary
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                    ) {
                        com.example.aquagraphapp.navigation.NavigationBar(dataForTable)
                    }
                }
            }
        }
    }
}

