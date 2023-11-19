package com.example.aquagraphapp.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquagraphapp.MainActivity
import com.example.aquagraphapp.models.MarkModel
import com.yandex.mapkit.geometry.Point

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(applicationContext: Context, workMarks: List<MarkModel>) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {com.example.aquagraphapp.buttons.AddNewAdressButton(applicationContext)},
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val maExample = MainActivity()
                maExample.ShowMap(maExample.curPoint, workMarks)
            }
        }
    }
}
