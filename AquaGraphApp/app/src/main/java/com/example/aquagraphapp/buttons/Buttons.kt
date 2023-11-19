package com.example.aquagraphapp.buttons

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.ButtonColors.*
import com.example.aquagraphapp.dataReceiving.getNewAdressPoint1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewAdressButton(applicationContext: Context) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    var text by rememberSaveable {
        mutableStateOf("")
    }

    var wrongInput by remember {
        mutableStateOf(false)
    }

    if (!showDialog) {
        ExtendedFloatingActionButton(
            expanded = true,
            icon = { Icon(Icons.Outlined.Add, "desc") },
            text = { Text(text = "Добавить адрес") },
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            onClick = {
                showDialog = true
            }
        )
    }

    //Dialog window
    if (showDialog) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Dialog(
                onDismissRequest = {
                    showDialog = false
                    wrongInput = false
                    text = ""
                },
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(15.dp),
                            value = text,
                            onValueChange = { text = it },
                            label = {
                                val labelText =
                                    if (wrongInput) "Неккоректный адрес" else "Введите адрес"
                                Text(labelText)
                            },
                            isError = wrongInput,
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Outlined.Search, "Search icon") },
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //accept button
                            OutlinedButton(
                                enabled = text.isNotEmpty(),
                                onClick = {
                                    getNewAdressPoint1(
                                        applicationContext,
                                        text
                                    ).thenAccept { newPoint ->
                                        if (newPoint.latitude != 0.0 && newPoint.longitude != 0.0) {
                                            Log.d(
                                                "point",
                                                "${newPoint.latitude} + ${newPoint.longitude}"
                                            )
                                            wrongInput = false

                                        } else {
                                            wrongInput = true
                                            return@thenAccept
                                        }
                                        showDialog = false
                                        text = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                Icon(Icons.Outlined.Done, "Done")
                                Text("Добавить")
                            }
                            //cancel button
                            OutlinedButton(
                                onClick = {
                                    showDialog = false
                                    text = ""
                                    wrongInput = false
                                },
                                shape = RoundedCornerShape(12.dp),
//                                colors = ButtonColors(
//                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//                                    contentColor = MaterialTheme.colorScheme.primary,
//                                    disabledContainerColor = MaterialTheme.colorScheme.background,
//                                    disabledContentColor = MaterialTheme.colorScheme.onSurface
//                                )
                            ) {
                                Icon(Icons.Outlined.Close, "Cancel")
                                Text("Отмена")
                            }
                        }
                    }
                }
            }
        }
    }
}

