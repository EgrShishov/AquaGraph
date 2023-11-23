package com.example.aquagraphapp.models

data class QualityModel(
    val MIGX_id: Int,
    val name : String,
    val value : String,
    val pdk : String,
    var metric : String,
    val help : String
    )

//data class ResponseModel(
//    val name : String,
//    val link : Int,
//    val time : String,
//    val params : List<QualityModel>
//)

data class ResponseModel(
    val name : String,
    val time : String,
    val params : List<QualityModel>
)