package com.example.fitfluent.data

data class User(
    val username: String,               // User's username
    val password: String,               // User's password
    var height_in_cm: Int,              // User's height in centimeters
    var weight_in_kg: Int,              // User's weight in kilograms
    var age: Int,                       // User's age
    var calorie_intake: Double,         // User's recommended daily calorie intake
    var calorie_time: String,           // Time of day for calorie intake
    var activity_level: Int,            // User's activity level
    var gender: String,                 // User's gender
    var bmi_score: Float                // User's BMI score
) : java.io.Serializable{
}