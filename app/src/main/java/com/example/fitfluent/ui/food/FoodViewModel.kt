package com.example.fitfluent.ui.food

import androidx.lifecycle.ViewModel
import com.example.fitfluent.data.User

class FoodViewModel : ViewModel() {


    // This function simply returns the user that is passed in as an argument
    fun local_user(user: User): User {

        return user
    }
    // TODO: Implement the ViewModel
}