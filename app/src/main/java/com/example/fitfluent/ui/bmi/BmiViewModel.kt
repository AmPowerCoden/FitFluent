package com.example.fitfluent.ui.bmi

import androidx.lifecycle.ViewModel
import com.example.fitfluent.MainActivity
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User

class BmiViewModel : ViewModel() {



    var _gender = ""
    var _weight = 0
    var _height = 0

    var _bmi_score = 0


    fun getData(user : User) {
        _weight = user.weight_in_kg
    }


    fun calculate_bmi() : Float {


        var test = 0F



        return test
    }



    // TODO: Implement the ViewModel
}