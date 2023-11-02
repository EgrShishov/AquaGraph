package com.example.aquagraphapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnit.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaGraphAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    //Text(text = "Ярик пидорас", color = Color.Black, fontSize = 32.sp)
                    myScreen()
                }
            }
        }
    }
}

@Composable
fun myScreen(){
    val items = listOf("Главная", "Инфо", "Проблема?", "Уведомления")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Info,
        Icons.Filled.Build,
        Icons.Filled.Notifications
    )
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .paddingFromBaseline(bottom = 0.dp)
        ,
        containerColor = Color(0xAA77DD77),
        contentColor = Color.Green
    )
    {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = false,
                onClick = { 0 },
                modifier = Modifier.height(90.dp)
            )
        }
    }
}