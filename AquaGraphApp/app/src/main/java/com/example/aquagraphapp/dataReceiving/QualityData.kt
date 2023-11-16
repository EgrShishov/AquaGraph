package com.example.aquagraphapp.dataReceiving

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.aquagraphapp.models.QualityModel
import com.example.aquagraphapp.models.ResponseModel
import org.json.JSONObject
import java.net.InetAddress
import java.net.NetworkInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun getLocalIPv4Address(): String? {
    val networkInterfaces = NetworkInterface.getNetworkInterfaces()
    if (networkInterfaces != null) {
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface: NetworkInterface = networkInterfaces.nextElement()
            val inetAddresses = networkInterface.inetAddresses
            while (inetAddresses.hasMoreElements()) {
                val inetAddress = inetAddresses.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress.isSiteLocalAddress
                    && inetAddress.hostAddress.indexOf(':') == -1
                ) {
                    return inetAddress.hostAddress
                }
            }
        }
    }
    return null
}

fun getNetworkIPv4Address(context: Context): String? {
    try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        if (wifiManager?.isWifiEnabled == true) {
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo != null) {
                val ipAddress: Int = wifiInfo.ipAddress
                val byteBuffer = ByteBuffer.allocate(4)
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
                byteBuffer.putInt(ipAddress)
                val byteAddress: ByteArray = byteBuffer.array()

                val inetAddress = InetAddress.getByAddress(byteAddress)
                return inetAddress.hostAddress
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

suspend fun getQualityData(
    point: Pair<Double, Double>,
    applicationContext: Context
): List<ResponseModel> = suspendCoroutine { continuation ->
    Log.d("ipv4","${getNetworkIPv4Address(applicationContext)}")
    val url = "http://192.168.209.248:1337/qualities?x=${point.first}&y=${point.second}"

    val queue = Volley.newRequestQueue(applicationContext)
    val request = StringRequest(
        Request.Method.GET,
        url,
        { result ->
            val parsedData = parseQualityData(result)
            continuation.resume(parsedData)
        },
        { error ->
            Log.d("ErrorLog", "Error in requesting API happened : $error")
            continuation.resume(emptyList())
        },
    )
    queue.add(request)
}

fun parseQualityData(result: String): List<ResponseModel> {

    val parsedData = mutableListOf<ResponseModel>()
    val responseObject = JSONObject(result)
    val qualities = responseObject.getJSONArray("Qualities")
    //Log.d("qualities","$qualities")
    for (i in 0 until qualities.length())
    {
        val item = qualities[i] as JSONObject
        //Log.d("item$i", "$item")
        val Time = item.getString("Time")
        //val Name = item.getString("Name")
        val Data = item.getJSONObject("Data")
        //Log.d("TimeLog", "${Time}")
        //Log.d("dataLog", "${Data}")
        val paramsArray = Data.getJSONArray("params")
        val paramsForTable = mutableListOf<QualityModel>()
        for (j in 0 until paramsArray.length())
        {
            val finalElement = paramsArray[j] as JSONObject
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
        parsedData.add(i, ResponseModel("", Time, paramsForTable))
//        Log.d("JSONInfo", "$paramsForTable")
//        Log.d("JSONInfo", "Success parsing JSON")
    }
    return parsedData
}