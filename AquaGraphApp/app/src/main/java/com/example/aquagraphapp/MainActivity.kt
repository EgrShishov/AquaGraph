package com.example.aquagraphapp

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

    private var _curPoint: Point = Point(53.919585,27.587433)
        var curPoint: Point
        set(value) {
            if(value.longitude != 0.0 && value.latitude != 0.0)
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
            val dataForTable = data.await()
            Log.d("coroutine", "$dataForTable")
            com.example.aquagraphapp.dataReceiving.getListOfScheduledWork(applicationContext).thenAccept{
                Log.d("sheduledWork","$it")
            }

//            val sec = async{
//                com.example.aquagraphapp.dataReceiving.addMarkData(
//                    27.594296f,
//                    53.925545f,
//                    "Суицид cеньора по асемблеру",
//                    applicationContext
//                )
//            }
            val marks = async{
                com.example.aquagraphapp.dataReceiving.getMarksData(
                    applicationContext
                )
            }
            //sec.await()
            val parsedData = data.await()
            val asd = marks.await()
            Log.d("coroutine", "$parsedData")

            setContent {
                AquaGraphAppTheme {
                    if(isSystemInDarkTheme()){
                        //implement changing to the dark theme
                        _mapView.map.setNightModeEnabled(true)
                        Log.d("night","night")
                    }
                    MaterialTheme.colorScheme.primary
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                    ) {
                        com.example.aquagraphapp.navigation.NavigationBar(dataForTable, applicationContext)
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
    fun ShowMap(point: Point) {
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
                val marker = R.drawable.ic_pin_black_png // Добавляем ссылку на картинку
                mapObjectCollection = _mapView.map.mapObjects // Инициализируем коллекцию различных объектов на карте
                placemarkMapObject = mapObjectCollection.addPlacemark(Point(53.919585, 27.587433), ImageProvider.fromResource(context, marker)) // Добавляем метку со значком
                //ImageProvider.fromResource(this, marker)
                placemarkMapObject.opacity = 0.5f // Устанавливаем прозрачность метке
                placemarkMapObject.setText("Обязательно к посещению!") // Устанавливаем текст сверху метки
                view
            },
            modifier = Modifier.fillMaxSize(),
            update = {
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
            }
        )
    }
}
