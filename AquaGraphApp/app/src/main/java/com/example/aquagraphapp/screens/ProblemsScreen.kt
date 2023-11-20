package com.example.aquagraphapp.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aquagraphapp.models.ScheduledWork

class ProblemsScreen {
    @Composable
    fun ShowProblemsScreen(applicationContext: Context) {
        var problemText by rememberSaveable {
            mutableStateOf("")
        }
        var addressText by rememberSaveable {
            mutableStateOf("")
        }
        var isExpanded by remember {
            mutableStateOf(false)
        }
        var isInputFinished by remember {
            mutableStateOf(false)
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Отметить неполадку") },
                    icon = { Icon(Icons.Outlined.Done, "Add mark") },
                    onClick = {
//                        if (isExpanded) {
//
//                        }
                    },
                    expanded = isExpanded,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(12.dp),
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Сообщите о проблеме, чтобы другие пользователи были осведомлены",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 4.sp)
                    Divider()
                    TextField(
                        modifier = Modifier
                            .padding(15.dp),
                        value = problemText,
                        onValueChange = {
                            problemText = it
                            isInputFinished = addressText.isNotEmpty() && problemText.isNotEmpty()
                            isExpanded = isInputFinished
                        },
                        label = {
                            Text("Опишите кратко проблему")
                        },
                        singleLine = true
                    )
                    Divider()
                    TextField(
                        modifier = Modifier
                            .padding(15.dp),
                        value = addressText,
                        onValueChange = {
                            addressText = it
                            isInputFinished = addressText.isNotEmpty() && problemText.isNotEmpty()
                            isExpanded = isInputFinished
                        },
                        label = {
                            Text("Введите адрес")
                        },
                        singleLine = true
                    )
                    Divider()
                }
            }
        }
    }
}