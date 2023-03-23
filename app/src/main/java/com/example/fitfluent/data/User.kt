package com.example.fitfluent.data

data class User(
    val username: String,
    val password: String,
    var height_in_cm: Int,
    var weight_in_kg: Int,
    var age: Int,
    var calorie_intake: Double,
    var calorie_time: String,
    var activity_level: Int,
    var gender: String,
    var bmi_score: Float
) : java.io.Serializable{
}