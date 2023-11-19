package com.example.aquagraphapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.example.aquagraphapp.dataReceiving.getNewAdressPoint2
import com.example.aquagraphapp.models.MarkModel
import com.example.aquagraphapp.models.ScheduledWork
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var _mapView: MapView
    private lateinit var mapObjectCollection: MapObjectCollection
    private lateinit var placemarkMapObject: PlacemarkMapObject
    var mapView: MapView
        set(value) {
            _mapView = value
        }
        get() = this._mapView

    private var _curPoint: Point = Point(53.919585, 27.587433)
    var curPoint: Point
        set(value) {
            if (value.longitude != 0.0 && value.latitude != 0.0)
                _curPoint = value
        }
        get() = this._curPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("f46e14a5-c996-44dc-8436-3eacfe546a4f")
        MapKitFactory.initialize(this)
        _mapView = MapView(applicationContext)

        lifecycleScope.launch {
            val data = async {
                com.example.aquagraphapp.dataReceiving.getQualityData(
                    _curPoint,
                    applicationContext
                )
            }
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
            val qualityData = data.await()
            val marksData = marks.await()
            setContent {
                AquaGraphAppTheme {
                    if (isSystemInDarkTheme()) {
                        //implement changing to the dark theme
                        _mapView.map.setNightModeEnabled(true)
                        Log.d("night", "night")
                    }
                    MaterialTheme.colorScheme.primary
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                    ) {
                        com.example.aquagraphapp.navigation.NavigationBar(
                            qualityData,
                            worksData,
                            workmarksData,
                            applicationContext
                        )
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        _mapView.onStart()
    }

    override fun onStop() {
        _mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    @Composable
    fun ShowMap(point: Point, worksModel: List<MarkModel>) {
        AndroidView(
            factory = { context ->
                val view = LayoutInflater.from(context).inflate(R.layout.main_activity, null)
                _mapView = view.findViewById<MapView>(R.id.mapview)
                if (_mapView.map.cameraPosition.target != point) {
                    _mapView.map.move(
                        CameraPosition(
                            point,
                            15.0f,
                            150.0f,
                            30.0f
                        )
                    )
                }
                for (item in worksModel) {
                    val marker = R.drawable.ic_pin_black_png // Добавляем ссылку на картинку
                    mapObjectCollection =
                        _mapView.map.mapObjects // Инициализируем коллекцию различных объектов на карте
                    placemarkMapObject = mapObjectCollection.addPlacemark(
                        Point(item.X.toDouble(), item.Y.toDouble()),
                        ImageProvider.fromResource(context, marker)
                    ) // Добавляем метку со значком
                    //ImageProvider.fromResource(this, marker)
                    placemarkMapObject.opacity = 0.5f // Устанавливаем прозрачность метке
                    placemarkMapObject.setText("Работы!") // Устанавливаем текст сверху метки
                }
                view
            },
            modifier = Modifier.fillMaxSize(),
            update = {
//                if (_mapView.map.cameraPosition.target != point) {
//                    _mapView.map.move(
//                        CameraPosition(
//                            point,
//                            15.0f,
//                            150.0f,
//                            30.0f
//                        )
//                    )
//                }
            }
        )

    }
}


suspend fun ToMarksModel(works: List<ScheduledWork>, applicationContext: Context): List<MarkModel> {
    var result_marks = mutableListOf<MarkModel>()
    for (item_works in works) {
        for (adress in item_works.Addresses) {

            var index = adress.indexOf(',')
            var new_adress = ""
            if(index == -1)
                new_adress = adress
            else
                new_adress = adress.substring(0, adress.indexOf(','))
            Log.d("adress", "$new_adress")
            var point = getNewAdressPoint2(applicationContext, new_adress)
            Log.d("mark", "${point.latitude}, ${point.longitude}")
            result_marks.add(
                result_marks.size, MarkModel(
                    0, "0", "0", "${point.latitude}", "${point.longitude}"
                )
            )
        }
    }
    return result_marks
}


