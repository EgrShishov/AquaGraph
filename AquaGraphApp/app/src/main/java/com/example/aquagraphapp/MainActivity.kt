package com.example.aquagraphapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aquagraphapp.ui.theme.AquaGraphAppTheme
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaGraphAppTheme {
                MaterialTheme.colorScheme.primary
                // A surface container using the 'background' color from the theme
                Surface(
                   // MaterialTheme(colorScheme = Color.Blue, content = null)
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                    shape = MaterialTheme.shapes.medium,

                ) {
                    myScreen()
                    Lazy()
                    var point = Pair(27.587433,53.919585)
                    getQualityData(point)
                }
            }
        }
    }
    private fun getQualityData(point : Pair<Double,Double>){
        val url = "http://192.168.23.100:1337/qualities?x=" +
                point.first +
                "&y=" +
                point.second
        val queue = Volley.newRequestQueue(applicationContext)
        val request = StringRequest(
            com.android.volley.Request.Method.GET,
            url,
            {
                result -> parseQualityData(result)
            },
            {
                error -> Log.d("ErrorLog", "Error in requesting API happened : $error")
            },
        )
        queue.add(request)
    }

    private fun parseQualityData(result : String){

        val paramsForTable = mutableListOf<QualityModel>()
        val resParamsArray = mutableListOf<JSONArray>()
        val responseObject = JSONObject(result)
        val qualities = responseObject.getJSONArray("Qualities")
        //Log.d("qualities","$qualities")
        for(i in 0..qualities.length() - 1) {
            val item = qualities[i] as JSONObject
            //Log.d("item$i", "$item")
            val Time = item.getString("Time")
            val Data = item.getJSONObject("Data")
            //Log.d("TimeLog", "${Time}")
            //Log.d("dataLog", "${Data}")
            val paramsArray = Data.getJSONArray("params")
            resParamsArray.add(i, paramsArray)
        }

        for (i in 0 until resParamsArray.size - 1) {
            if (resParamsArray[i] != null) {
                val arrayElem = resParamsArray[i]
                for(j in 0 until arrayElem.length() - 1){
                   val nextArray = arrayElem[i] as JSONArray
                    for(k in 0 until nextArray.length() - 1){
                        val finalElement = nextArray[k] as JSONObject
                        //Log.d("paramsleng", "${finalElement.length()}")
                        //Log.d("params$j", "$finalElement")
                        val model = QualityModel(
                            finalElement.getInt("MIGX_id"),
                            finalElement.getString("name"),
                            finalElement.getString("value"),
                            finalElement.getString("pdk"),
                            finalElement.getString("metric"),
                            finalElement.getString("help")
                        )
                        paramsForTable.add(k,model)
                        //Log.d("modelLog","$model")
                    }
                }
            }
        }
        Toast.makeText(applicationContext, "$paramsForTable", Toast.LENGTH_LONG).show()
    }
}

@Composable
fun myScreen(){
    val items = listOf("Главная", "Инфо", "Проблема?", "Уведомления")
    val filledIcons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Info,
        Icons.Filled.Build,
        Icons.Filled.Notifications
    )
    val outlinedIcons = listOf(
        Icons.Outlined.Home,
        Icons.Outlined.Info,
        Icons.Outlined.Build,
        Icons.Outlined.Notifications
    )
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .paddingFromBaseline(bottom = 0.dp)
        ,
        //containerColor = Color(0xAA3771DF),
        containerColor = MaterialTheme.colorScheme.onTertiary,
        //contentColor = Color.Red
    )
    {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(outlinedIcons[index], contentDescription = item) },
                label = { Text(item) },
                selected = false,
                onClick = { null },
                modifier = Modifier.height(90.dp)
            )
        }
    }
}

@Composable
fun Lazy(){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ){
        items(100) { position ->

            println("Build item at position $position")

            Row{
               Text("mamaPapa")
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }

}