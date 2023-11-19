package com.example.aquagraphapp.screens

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.aquagraphapp.R
import com.example.aquagraphapp.databinding.MainActivityBinding
import com.example.aquagraphapp.models.MarkModel
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider

class HomeScreen {

    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowHomeScreen(applicationContext: Context, marks: List<MarkModel>) {
        var point by remember {
            mutableStateOf(com.yandex.mapkit.geometry.Point(53.919585, 27.587433))
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            floatingActionButton = {
                val Point = com.example.aquagraphapp.buttons.AddNewAdressButton(
                    applicationContext
                )
                if (Point.latitude != 0.0 && Point.longitude != 0.0) {
                    point = Point
                }
            },
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
                    Log.d("marksinfunc", "$marks")
                    ShowMap(point, marks)
                }
            }
        }
    }

    @Composable
    fun ShowMap(point: com.yandex.mapkit.geometry.Point, marks: List<MarkModel>) {
        var showMarkInfo by remember {
            mutableStateOf(false)
        }
        var text by remember {
            mutableStateOf("")
        }

        if (showMarkInfo) {
            Dialog(
                onDismissRequest = {
                    showMarkInfo = false
                    text = ""
                }
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                    //.height(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("$text")
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            //understood button
                            OutlinedButton(
                                onClick = {
                                    showMarkInfo = false
                                    text = ""
                                },
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(6.dp)
                            ) {
                                Icon(Icons.Outlined.Check, "Accept")
                                Text("Понятно")
                            }
                        }
                    }
                }
            }
        }
        //com.example.aquagraphapp.screens.BarChartSample(data = listOf() , index = 0)
        AndroidView(
            factory = { context ->
                val binding = MainActivityBinding.inflate(LayoutInflater.from(context))

                if (binding.mapview.map.cameraPosition.target != point) {
                    binding.mapview.map.move(
                        CameraPosition(
                            point,
                            15.0f,
                            150.0f,
                            30.0f
                        )
                    )
                }
                val marker = R.drawable.ic_pin_black_png
                mapObjectCollection = binding.mapview.map.mapObjects
                marks.forEachIndexed { index, markModel ->
                    if (index != 0) {
                        val point = com.yandex.mapkit.geometry.Point(
                            markModel.Y.toDouble(),
                            markModel.X.toDouble()
                        )
                        Log.d("mamamarka", "$markModel")
                        placemarkMapObject = mapObjectCollection.addPlacemark(
                            point,
                            ImageProvider.fromResource(context, marker)
                        )
                        placemarkMapObject.opacity = 0.5f
                        placemarkMapObject.setText(markModel.Data)
                        val mapObjectTapListener =
                            MapObjectTapListener { mapObject, p1 ->
                                text = "Время : ${markModel.Time}\nИнфо : ${markModel.Data}"
                                showMarkInfo = true
                                true
                            }
                        placemarkMapObject.addTapListener(
                            mapObjectTapListener
                        )
                    }
                }
                val redMarker = R.drawable.image
                placemarkMapObject = mapObjectCollection.addPlacemark(
                    point,
                    ImageProvider.fromResource(context, redMarker)
                )
                placemarkMapObject.setText("Введенный адрес ${point.latitude}")
                binding.root
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.findViewById<MapView>(R.id.mapview).map.move(
                    CameraPosition(
                        point,
                        15.0f,
                        150.0f,
                        30.0f
                    )
                )
            }
        )
    }
}
