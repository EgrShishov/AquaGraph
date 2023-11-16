package com.example.aquagraphapp.dataReceiving

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.yandex.mapkit.geometry.Point
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.CompletableFuture

fun getNewAdressPoint(applicationContext: Context, adress: String): CompletableFuture<Point> {
    val future = CompletableFuture<Point>()
    val url = "https://geocode.maps.co/search?" +
            "street=${adress}" +
            "&city=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA" +
            "&country=%D0%B1%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C"
    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            val ans = processAdress(result)
            Log.d("resadd", "$ans")
            future.complete(ans)
        },
        { error ->
            Log.d("Error in parsing point", "$error")
            future.complete(Point())
        }
    )
    queue.add(request)
    return future
}

fun processAdress(result: String): Point {
    val jsonArr = JSONArray(result)
    if (jsonArr.length() != 0) {
        val garbage = jsonArr[0] as JSONObject
        val boundingBox = garbage.getJSONArray("boundingbox")
        return Point(boundingBox.getDouble(0), boundingBox.getDouble(2))
    }
    return Point(0.0, 0.0)
}