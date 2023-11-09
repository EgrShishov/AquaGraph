package com.example.aquagraphapp.screens

import android.text.Html
import android.webkit.WebView
import android.widget.TextView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.seanproctor.datatable.TableColumnDefinition
import com.seanproctor.datatable.Table
import androidx.compose.material3.Text
import com.example.aquagraphapp.models.QualityModel

@Composable
fun InfoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /*Text(
            text = "INFO SCREEN",
            fontSize = 30.sp,
            color = Color.Green
        )*/
        //CreateTable(items)
    }
}

@Composable
fun CreateTable() {

    var selectedRow by remember { mutableStateOf(0) }
    var items = listOf<QualityModel>()
    Table(
        modifier = Modifier
            //.fillMaxWidth()
            //.padding(10.dp, 10.dp)
            .padding(10.dp),
        columns = listOf(
            TableColumnDefinition {
                Text("Критерий")
            },
            TableColumnDefinition {
                Text("ПДК")
            },
            TableColumnDefinition(Alignment.CenterEnd) {
                Text("Значение")
            },
        )
    ) {
        items.forEachIndexed { index, item ->
            row {
                onClick = { selectedRow = index }
                cell { Text("$index") }
                cell { Text("$index") }
                cell { Text("$index") }
            }
        }
    }
}