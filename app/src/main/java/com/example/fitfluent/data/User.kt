package com.example.fitfluent.data

data class User(
    val username: String,
    val password: String,
    val height_in_cm: Int,
    val weight_in_kg: Int,
    val age: Int
) : java.io.Serializable{
}