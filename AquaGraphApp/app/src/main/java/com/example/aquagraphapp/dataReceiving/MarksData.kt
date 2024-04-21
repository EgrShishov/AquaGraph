package com.example.aquagraphapp.dataReceiving

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aquagraphapp.models.IPconfig
import com.example.aquagraphapp.models.MarkModel
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun getMarksData(applicationContext: Context): List<MarkModel>
        = suspendCoroutine { continuation ->
    val url = "http://${IPconfig}:1337/marks"

    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            val parsedData = parseMarksData(result)
            continuation.resume(parsedData)
        },
        { error ->
            Log.d("ErrorLog", "Error in requesting API happened : $error")
            continuation.resume(emptyList())
        },
    )
    queue.add(request)
}

fun parseMarksData(result: String): List<MarkModel> {
//    Log.d("MamaLyubitPapu", "$result")
    Log.d("WKHKJNDWINIFBWF", "$result")
    val parsedData = mutableListOf<MarkModel>()
    val responseObject = JSONObject(result)
    val marks = responseObject.getJSONArray("Marks")
//    Log.d("marks","$marks")
    for (i in 0 until marks.length()) {
        val item = marks[i] as JSONObject
        Log.d("item$i", "$item")
        val model = MarkModel(
        item.getInt("Id"),
        item.getString("Data"),
        item.getString("Time"),
        item.getString("X"),
        item.getString("Y")
        )
        parsedData.add(parsedData.size, model)
    }
    return parsedData
}

suspend fun addMarkData(X: Float, Y: Float, Data: String, applicationContext: Context): String
        = suspendCoroutine { continuation ->
    val url = "http://$IPconfig:1337/new-mark?x=$X&y=$Y&data=$Data"

    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            Log.d("new-mark", "x=$X&y=$Y&data=$Data")
            continuation.resume("$result")
        },
        { error ->
            Log.d("ErrorLog", "Error in requesting API happened : $error")
            continuation.resume("")
        },
    )
    queue.add(request)
}

suspend fun DeleteMarkData(markId: Int, applicationContext: Context): String
        = suspendCoroutine { continuation ->
    val url = "http://$IPconfig:1337/delete-mark?id=${markId}"

    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            Log.d("delete-mark", "${markId}")
            continuation.resume("$result")
        },
        { error ->
            Log.d("ErrorLog", "Error in requesting API happened : $error")
            continuation.resume("")
        },
    )
    queue.add(request)
}

