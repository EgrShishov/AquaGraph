package com.example.aquagraphapp.qualityData

import android.app.Application
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aquagraphapp.models.QualityModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun getQualityData(
    point: Pair<Double, Double>,
    applicationContext: Context
) : List<QualityModel> = suspendCoroutine{ continuation ->
    val url = "http://192.168.23.100:1337/qualities?x=" +
            point.first +
            "&y=" +
            point.second

    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            val dataForTable = parseQualityData(result)
            continuation.resume(dataForTable)
        },
        { error ->
            Log.d("ErrorLog", "Error in requesting API happened : $error")
            continuation.resume(emptyList())
        },
    )
    queue.add(request)
}

fun parseQualityData(result: String):MutableList<QualityModel> {

    val resParamsArray = mutableListOf<JSONArray>()
    val paramsForTable = mutableListOf<QualityModel>()
    val responseObject = JSONObject(result)
    val qualities = responseObject.getJSONArray("Qualities")
    //Log.d("qualities","$qualities")
    for (i in 0 until qualities.length()) {
        val item = qualities[i] as JSONObject
        //Log.d("item$i", "$item")
        val Time = item.getString("Time")
        val Data = item.getJSONObject("Data")
        //Log.d("TimeLog", "${Time}")
        //Log.d("dataLog", "${Data}")
        val paramsArray = Data.getJSONArray("params")
        resParamsArray.add(i, paramsArray)
    }

    //Log.d("mama", "${resParamsArray.size}")
    for (i in 0 until resParamsArray.size) {
        val arrayElem = resParamsArray[i]
        //Log.d("elem","${arrayElem}")
        for (j in 0 until arrayElem.length()) {
            val finalElement = arrayElem[j] as JSONObject
            //Log.d("params$j", "$finalElement")
            val model = QualityModel(
                finalElement.getInt("MIGX_id"),
                finalElement.getString("name"),
                finalElement.getString("value"),
                finalElement.getString("pdk"),
                finalElement.getString("metric"),
                finalElement.getString("help")
            )
            paramsForTable.add(j, model)
        }
        Log.d("JSONInfo", "$paramsForTable")
        Log.d("JSONInfo", "Success parsing JSON")
    }
    return paramsForTable
}
