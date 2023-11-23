package com.example.aquagraphapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.aquagraphapp.buttons.AddNewAdressButton
import com.example.aquagraphapp.databinding.MainActivityBinding
import com.example.aquagraphapp.models.ResponseModel
import com.example.aquagraphapp.navigation.NavigationBar
import com.example.aquagraphapp.notifications.NotificationService
import com.example.aquagraphapp.dataReceiving.getNewAdressPoint2
import com.example.aquagraphapp.models.MarkModel
import com.example.aquagraphapp.models.ScheduledWork
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var _mapView: MapView
    private lateinit var binding: MainActivityBinding
    private var marksAdresses = listOf<MarkModel>()
    private var curLocation: Point = Point(53.919585, 27.587433)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setApiKey(savedInstanceState)
        MapKitFactory.initialize(this)
        binding = MainActivityBinding.inflate(layoutInflater)
        val NavigationBar = NavigationBar()
        val service = NotificationService(applicationContext)

        lifecycleScope.launch {
            val marks = async {
                com.example.aquagraphapp.dataReceiving.getMarksData(
                    applicationContext
                )
            }

            val works = async {
                com.example.aquagraphapp.dataReceiving.getListOfScheduledWork(
                    applicationContext
                )
            }

            val worksData = works.await()
            val workMarks = async {
                ToMarksModel(
                    worksData,
                    applicationContext
                )
            }

            val workmarksData = workMarks.await()
            marksAdresses = marks.await()

            setContent {
                AquaGraphAppTheme {
                    if (isSystemInDarkTheme()) {
                        //implement changing to the dark theme
                        binding.mapview.map.isNightModeEnabled = true
                        Log.d("night", "night")
                    }
                    MaterialTheme.colorScheme.primary
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                    ) {
                        NavigationBar.ShowNavigationBar(
                            //qualityData = qualityData,
                            worksData = worksData,
                            workMarks = workmarksData,
                            applicationContext = applicationContext,
                            valCurrPoint = curLocation
                        )
                    }
                }
            }
        }
    }

    private fun setApiKey(savedInstanceState: Bundle?) {
        val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false
        if (!haveApiKey) {
            MapKitFactory.setApiKey("f46e14a5-c996-44dc-8436-3eacfe546a4f")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("haveApiKey", true)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onStop() {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}


suspend fun ToMarksModel(works: List<ScheduledWork>, applicationContext: Context): List<MarkModel> {
    var result_marks = mutableListOf<MarkModel>()
    for (item_works in works) {
        for (adress in item_works.Addresses) {

            var index = adress.indexOf(',')
            var new_adress = ""
            if (index == -1)
                new_adress = adress
            else
                new_adress = adress.substring(0, adress.indexOf(','))
            Log.d("adress", "$new_adress")
            var point = getNewAdressPoint2(applicationContext, new_adress)
            Log.d("mark", "${point.latitude}, ${point.longitude}")
            result_marks.add(
                result_marks.size, MarkModel(
                    0, "Запланированные работы. Возможно изменения качества воды", "${item_works.Time}", "${point.latitude}", "${point.longitude}"
                )
            )
        }
    }
    return result_marks
}


