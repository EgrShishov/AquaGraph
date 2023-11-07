package com.example.aquagraphapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme
import org.json.JSONArray
import org.json.JSONObject
import com.example.aquagraphapp.models.QualityModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var point = Pair(27.587433, 53.919585)
        val dataForTable = mutableListOf<QualityModel>()
        com.example.aquagraphapp.qualityData.getQualityData(point, dataForTable, applicationContext)
        Log.d("data", "$dataForTable")

        setContent {
            AquaGraphAppTheme {
                MaterialTheme.colorScheme.primary
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    com.example.aquagraphapp.navigation.NavigationBar()
                }
            }
        }

    }
}

