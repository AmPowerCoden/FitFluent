package com.example.fitfluent.ui.bmi

import androidx.lifecycle.ViewModel
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User
import kotlin.math.roundToInt

// ViewModel class for the BMI feature
class BmiViewModel : ViewModel() {


    // Properties for user data and calculated BMI
    var _gender = ""
    var _weight = 0
    var _height  = 0
    var _age = 0
    var _bmi_score = 0F


    // Function to set the user data for BMI calculation
    fun getData(user : User) {
        _gender = user.gender
        _weight = user.weight_in_kg
        _height = user.height_in_cm
        _age = user.age
    }


    // Function to calculate BMI based on user data and return the score
    fun calculate_bmi() : Float {
        // BMI formula: weight (kg) / height (m)^2
        var bmi_score = (((_weight.toFloat() / (_height.toFloat() / 100 * _height.toFloat() / 100)) * 100).roundToInt()).toFloat() / 100
        _bmi_score = bmi_score
        return bmi_score
    }



}