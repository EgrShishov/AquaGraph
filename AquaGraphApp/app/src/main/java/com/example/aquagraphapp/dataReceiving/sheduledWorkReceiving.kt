package com.example.aquagraphapp.dataReceiving

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aquagraphapp.models.SheduledWork
import org.json.JSONObject
import java.util.concurrent.CompletableFuture


fun getListOfScheduledWork(applicationContext: Context): CompletableFuture<List<SheduledWork>> {
    val url = "http://192.168.209.248:1337/works"
    val response = CompletableFuture<List<SheduledWork>>()
    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            Log.d("mama","$result")
            val parsed = parseScheduledWorkData(result)
            response.complete(parsed)
        },
        { error ->
            Log.d("ScheduledWorkError", "$error")
            response.complete(listOf())
        }
    )
    queue.add(request)
    return response
}

fun parseScheduledWorkData(result: String): List<SheduledWork> {
    val jsonarray = JSONObject(result)
    val arrayOfWorks = jsonarray.getJSONArray("Works")
    val listOfWorks = mutableListOf<SheduledWork>()

    for (i in 0 until arrayOfWorks.length()) {
        val el = arrayOfWorks.getJSONObject(i)
        val addresses = mutableListOf<String>()
        val array = el.getJSONArray("Addresses")
        for (j in 0 until array.length()) {
            addresses.add(j, array.getString(j))
        }
        listOfWorks.add(
            i,
            SheduledWork(
                Time = el.getString("Time"),
                Data = el.getString("Data"),
                Addresses = addresses
            )
        )
    }
    Log.d("list of work","$listOfWorks")
    return listOfWorks
}