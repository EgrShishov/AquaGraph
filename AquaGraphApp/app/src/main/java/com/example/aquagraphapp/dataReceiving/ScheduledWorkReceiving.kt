package com.example.aquagraphapp.dataReceiving

import android.content.Context
//import android.graphics.Point
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aquagraphapp.models.ScheduledWork
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


suspend fun getListOfScheduledWork(
    applicationContext: Context
): List<ScheduledWork> = suspendCoroutine { continuation ->
    val url = "http://192.168.98.248:1337/works"
    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            Log.d("mama","$result")
            val parsed = parseScheduledWorkData(result)
            continuation.resume(parsed)
        },
        { error ->
            Log.d("ScheduledWorkError", "$error")
            continuation.resume(listOf())
        }
    )
    queue.add(request)
}

fun parseScheduledWorkData(result: String): List<ScheduledWork> {
    val jsonarray = JSONObject(result)
    val arrayOfWorks = jsonarray.getJSONArray("Works")
    val listOfWorks = mutableListOf<ScheduledWork>()

    for (i in 0 until arrayOfWorks.length()) {
        val el = arrayOfWorks.getJSONObject(i)
        val addresses = mutableListOf<String>()
        val array = el.getJSONArray("Addresses")
        for (j in 0 until array.length()) {
            addresses.add(j, array.getString(j))
        }
        listOfWorks.add(
            i,
            ScheduledWork(
                Time = el.getString("Time"),
                Data = el.getString("Data"),
                Addresses = addresses
            )
        )
    }
    Log.d("list of work","$listOfWorks")
    return listOfWorks
}

