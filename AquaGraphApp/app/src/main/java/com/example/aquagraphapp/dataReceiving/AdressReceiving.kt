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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun getNewAdressPoint1(applicationContext: Context, adress: String): CompletableFuture<Point>{
    var future = CompletableFuture<Point>()
//    val url = "https://geocode.maps.co/search?" +
//            "street=${adress}" +
//            "&city=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA" +
//            "&country=%D0%B1%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C" +
//            "&api_key=66254d49f19dc257221630keobd2d86"
//    val url = "https://api.geoapify.com/v1/geocode/search?" +
//        "text=${adress}" +
//        "&city=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA" +
//        "&country=%D0%B1%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C" +
//        "&apiKey=ac7bebd8da404c768c128b2bd7441125"
    val url = "https://api.geoapify.com/v1/geocode/search?" +
            "text=${adress} %D0%9C%D0%B8%D0%BD%D1%81%D0%BA %D0%B1%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C" +
            "&apiKey=ac7bebd8da404c768c128b2bd7441125"
    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            val ans = processAddress(result)
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

suspend fun getNewAdressPoint2(applicationContext: Context, adress: String): Point = suspendCoroutine{
    continuation ->
//    val url = "https://geocode.maps.co/search?" +
//            "street=${adress}" +
//            "&city=%D0%9C%D0%B8%D0%BD%D1%81%D0%BA" +
//            "&country=%D0%B1%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C" +
//            "&api_key=66254d49f19dc257221630keobd2d86"
    val url = "https://api.geoapify.com/v1/geocode/search?" +
            "text=${adress} %D0%9C%D0%B8%D0%BD%D1%81%D0%BA %D0%B1%D0%B5%D0%BB%D0%B0%D1%80%D1%83%D1%81%D1%8C" +
            "&apiKey=ac7bebd8da404c768c128b2bd7441125"
    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            val ans = processAddress(result)
            Log.d("resadd", "$ans")
            continuation.resume(ans)
        },
        { error ->
            Log.d("Error in parsing point", "$error")
            continuation.resume(Point())
        }
    )
    queue.add(request)
}



fun processAddress(result: String): Point {
    val jsonObj = JSONObject(result)
    val features = jsonObj.getJSONArray("features")
    if (features.length() > 0) {
        val geometry = (features[0] as JSONObject).getJSONObject("geometry")
        val coordinates = geometry.getJSONArray("coordinates")
        val latitude = coordinates.getDouble(1)
        val longitude = coordinates.getDouble(0)
        return Point(latitude, longitude)
    }
    return Point(0.0, 0.0)
}