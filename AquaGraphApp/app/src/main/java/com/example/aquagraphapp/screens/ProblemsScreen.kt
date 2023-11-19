package com.example.aquagraphapp.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.aquagraphapp.models.ScheduledWork

class ProblemsScreen {
    @Composable
    fun ShowProblemsScreen(applicationContext: Context) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "ProblemsScreen",
                fontSize = 30.sp,
                color = Color.Blue
            )
        }
    }

        @Composable
        fun ShowWorksSchedule(schedule: List<ScheduledWork>) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in 0..schedule.size - 1) {
                    Log.d("plan$i", "${schedule[i].Data}")
                    Log.d("plan$i", "${schedule[i].Addresses}")
                    Log.d("plan$i", "${schedule[i].Time}")
                }
            }
        }
    }