package com.example.aquagraphapp.models

data class QualityModel(
    val MIGX_id: Int,
    val name : String,
    val value : String,
    val pdk : String,
    val metric : String,
    val help : String
    )

data class ResponseModel(
    val name : String,
    val link : Int,
    val params : List<QualityModel>
)

data class JSONResponseModel(
    val Time : String,
    val Data : ResponseModel
)