package com.example.fitfluent.ui.food

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fitfluent.MainActivity
import com.example.fitfluent.R
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User
import java.time.LocalDateTime

class FoodFragment: Fragment() {

    companion object {
        fun newInstance() = FoodFragment()
    }

    private lateinit var viewModel: FoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_food, container, false)
        val mainActivity = activity as MainActivity

        val headline = view.findViewById<View>(R.id.headline_food) as TextView

        var user = mainActivity.getLoggedUser()
        mainActivity.lowerCalories(user, 100)
        user = mainActivity.getLoggedUser()

        var usualCalories = (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age))
        if (user.calorie_intake > -(user.activity_level))
        {
            headline.text = "Hallo ${user.username}! Sie haben für heute noch ${user.calorie_intake + user.activity_level * 100} von ${usualCalories + user.activity_level * 100} offen"
        } else {
            headline.text = "Hallo ${user.username}! Sie haben heut schon ${(user.calorie_intake) - (usualCalories)} zu viel gegessen"
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
    }


}