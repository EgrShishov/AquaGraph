package com.example.aquagraphapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.aquagraphapp.models.ScheduledWork


class NotificationsScreen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowNotificationScreen(worksData: List<ScheduledWork>) {
        var selected_index by remember { mutableStateOf(-1) }
        var clickable by remember {
            mutableStateOf(
                mutableListOf(
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 20.dp),
                text = "Уведомления",
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
            )
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            )
            {
                for (i in 0..worksData.size - 1) {
                    var CardColor: Color
                    if (clickable[i])
                        CardColor = MaterialTheme.colorScheme.primaryContainer
                    else
                        CardColor = MaterialTheme.colorScheme.inversePrimary
                    Card(
                        onClick = {
                            selected_index = i
                            clickable[i] = true
                        },
                        elevation = CardDefaults.cardElevation(5.dp),
                        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onBackground),
                        colors = CardDefaults.cardColors(
                            containerColor = CardColor
                        ),
                        modifier = Modifier
                            .width(380.dp)
                            .height(120.dp)
                            .padding(top = 9.dp, bottom = 9.dp)
                    )
                    {
                        Text(
                            maxLines = 3,
                            text = "${worksData[i].Data}",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(10.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
                if (selected_index != -1) {
                    Dialog(
                        onDismissRequest = {
                            selected_index = -1
                        }
                    )
                    {
                        Card(
                            elevation = CardDefaults.cardElevation(2.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                            shape = RoundedCornerShape(20.dp)
                        )
                        {
                            Text(
                                text = "${worksData[selected_index].Data}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp, 15.dp, 15.dp, 15.dp)
                            )
                            OutlinedButton(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 15.dp),
                                onClick = {
                                    selected_index = -1
                                },
                                shape = RoundedCornerShape(12.dp),
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