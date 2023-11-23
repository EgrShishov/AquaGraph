package com.example.aquagraphapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            AquaGraphAppTheme {
                SplashScreen()
            }
        }
    }

    @Composable
    private fun SplashScreen(){
        var loading by remember {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = true){
            loading = true
            Log.d("mama","logg")
            delay(2000)
            loading = false
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.baseline_water_24),
                contentDescription = null,
            )
            if(loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .paddingFromBaseline(200.dp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}