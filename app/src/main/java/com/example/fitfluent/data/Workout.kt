package com.example.fitfluent.data

class Workout(
    val ersteller : String,         // the creator of the workout
    val typ : String,               // the type of workout (e.g. strength training, cardio, etc.)
    val exercises : String,         // the list of exercises in the workout
    val times : String,             // the duration of each exercise
    val frequency : String,         // how often the workout should be performed (e.g. daily, weekly, etc.)
    val bmiRange : String           // the target BMI range for the workout
) {
}