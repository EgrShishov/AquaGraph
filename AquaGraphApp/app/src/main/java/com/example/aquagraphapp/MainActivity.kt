package com.example.aquagraphapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme
import com.example.aquagraphapp.models.QualityModel
import com.google.android.gms.common.internal.ShowFirstParty
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val point = Pair(27.587433, 53.919585)
        MapKitFactory.setApiKey("f46e14a5-c996-44dc-8436-3eacfe546a4f")
        MapKitFactory.initialize(this)
        mapView = MapView(applicationContext)

        lifecycleScope.launch {
            val data = async {
                com.example.aquagraphapp.qualityData.getQualityData(
                    point,
                    applicationContext
                )
            }
            val dataForTable = data.await()
            Log.d("coroutine", "$dataForTable")

            setContent {
                AquaGraphAppTheme {
                    MaterialTheme.colorScheme.primary
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.White,
                    ) {
                        com.example.aquagraphapp.navigation.NavigationBar()
                        //ShowMap(point = Point(point.second,point.first))
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    @Composable
    fun ShowMap(point: Point) {
        AndroidView(
            factory = { context ->
                val view = LayoutInflater.from(context).inflate(R.layout.main_activity, null)
                mapView = view.findViewById<MapView>(R.id.mapview)
                if (mapView.map.cameraPosition.target != point) {
                    mapView.map.move(
                        CameraPosition(
                            point,
                            15.0f,
                            150.0f,
                            30.0f
                        )
                    )
                }
                view
            },
            modifier = Modifier.fillMaxSize(),
            update = {
                if (mapView.map.cameraPosition.target != point) {
                    mapView.map.move(
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
