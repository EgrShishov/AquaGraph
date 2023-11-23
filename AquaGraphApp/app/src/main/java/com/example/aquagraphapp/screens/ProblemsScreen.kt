package com.example.aquagraphapp.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.yandex.mapkit.geometry.Point

class ProblemsScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowProblemsScreen(applicationContext: Context) {
        var problemText by rememberSaveable {
            mutableStateOf("")
        }
        var addressText by rememberSaveable {
            mutableStateOf("")
        }
        var isEnable by remember {
            mutableStateOf(false)
        }
        var isInputFinished by remember {
            mutableStateOf(false)
        }
        var isError by remember {
            mutableStateOf(false)
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Сообщите о проблеме",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValues.calculateTopPadding() + 20.dp),
                    )
                    ElevatedCard(
                        modifier = Modifier
                            .padding(15.dp)
                            .defaultMinSize(minHeight = 100.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = problemText,
                            onValueChange = {
                                isError = false
                                problemText = it
                                isInputFinished =
                                    addressText.isNotEmpty() && problemText.isNotEmpty()
                                isEnable = isInputFinished
                            },
                            label = {
                                Text("Опишите кратко проблему")
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background),
                            isError = isError
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            value = addressText,
                            onValueChange = {
                                isError = false
                                addressText = it
                                isInputFinished =
                                    addressText.isNotEmpty() && problemText.isNotEmpty()
                                isEnable = isInputFinished
                            },
                            label = {
                                Text("Введите адрес")
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.background),
                            isError = isError
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (isEnable) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Неполадка успешно добавлена на карту!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        com.example.aquagraphapp.dataReceiving.getNewAdressPoint1(
                                            applicationContext,
                                            addressText
                                        ).thenAccept {
                                            addMark(applicationContext, it, problemText)
                                            problemText = ""
                                            addressText = ""
                                            isEnable = false
                                        }
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Введите данные!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        isError = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                                enabled = isEnable
                            ) {
                                Icon(Icons.Outlined.Done, "add mark")
                                Text("Отметить неполадку")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun addMark(applicationContext: Context, address: Point, info: String) {
    val url =
        "http://192.168.98.248:1337/new-mark?data=${info}&x=${address.latitude}&y=${address.longitude}"
    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { ans ->
            Log.d("MARKS", "${ans}")
        },
        { error ->
            Log.d("MARKS", "Error in adding mark ${error}")
        }
    )
    queue.add(request)
}