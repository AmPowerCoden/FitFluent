package com.example.fitfluent.ui.food

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.fitfluent.MainActivity
import com.example.fitfluent.R
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User
import com.example.fitfluent.databinding.ActivityRegisterBinding
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        val ingredient_search = view.findViewById<EditText>(R.id.search_bar_food) as EditText
        val gram_search = view.findViewById<EditText>(R.id.grams_bar_food) as EditText
        val get_food_btn = view.findViewById<Button>(R.id.get_food) as Button
        val shown_food = view.findViewById<TextView>(R.id.found_food) as TextView
        val subtract_calories_button = view.findViewById<Button>(R.id.subtract_food) as Button
        val subtract_food_error = view.findViewById<TextView>(R.id.error_substract) as TextView
        var nutrition = ""

        var user = mainActivity.getLoggedUser()

        var usualCalories = (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age))

        get_food_btn.isEnabled = true


        get_food_btn.setOnClickListener{
            try {
                nutrition = mainActivity.getNutritionIngredient(ingredient_search.text.toString())
                var nutritients = getNutrients(nutrition)
                var gram_search_double = gram_search.text.toString().toDouble()
                shown_food.text = gram_search.text.toString() + " gramm vom Gericht " + ingredient_search.text.toString() + " haben folgende Nährwerte:" + makeShownFoodText(nutritients, gram_search_double)
                subtract_calories_button.isEnabled = true
            }
            catch (e: java.lang.Exception){
                shown_food.text = "There was an Error with your search... the api wasn't able to find any food with that name"
            }
        }

        subtract_calories_button.setOnClickListener{
            try {
                var calories = shown_food.text.toString().split("Calories: ").get(1).split(" kcal").get(0).toDouble()
                mainActivity.lowerCalories(user, calories)
                user = mainActivity.getLoggedUser()

                usualCalories = (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age))
                if (user.calorie_intake > (user.activity_level * 100) + usualCalories)
                {
                    //headline.text = "Hallo ${user.username}! Sie haben für heute noch ${user.calorie_intake + user.activity_level * 100} von ${usualCalories + user.activity_level * 100} offen"
                    headline.text = ""
                } else {
                    headline.text = "Hallo ${user.username}! Sie haben heut schon ${(usualCalories) - (user.calorie_intake)} zu viel gegessen"
                }



            }
            catch (e : Exception)
            {
                subtract_food_error.isVisible = true
            }
        }


        if (user.calorie_intake > (user.activity_level * 100) + usualCalories)
        {
            headline.text = "Hallo ${user.username}! Sie haben für heute noch ${user.calorie_intake + user.activity_level * 100} von ${usualCalories + user.activity_level * 100} offen"
        } else {
            headline.text = "Hallo ${user.username}! Sie haben heut schon ${(usualCalories) - (user.calorie_intake)} zu viel gegessen"
        }

        checkDate(user)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
    }

    fun getNutrients(nutrition: String) : List<String> {
        val nutrients_only = nutrition.split("\"nutrients\":{").get(1).split("},").get(0)
        val calories = nutrients_only.split("\"ENERC_KCAL\":").get(1).split(",").get(0)
        val proteine = nutrients_only.split("\"PROCNT\":").get(1).split(",").get(0)
        val fat = nutrients_only.split("\"FAT\":").get(1).split(",").get(0)
        val carbs = nutrients_only.split("\"CHOCDF\":").get(1).split(",").get(0)


        val nutrients = listOf<String>(calories, proteine, fat, carbs)

        return nutrients
    }

    fun makeShownFoodText(nutrients : List<String>, amaount : Double) : String {
        var text = "\nCalories: " + (nutrients.get(0).toDouble() / 100 * amaount) + " kcal" +
                    "\nProtein: " + (nutrients.get(1).toDouble() / 100 * amaount) + " grams" +
                    "\nFat: " + (nutrients.get(2).toDouble() / 100 * amaount) + " grams" +
                    "\nCarbs: " + (nutrients.get(3).toDouble() / 100 * amaount) + "grams"

        return text

    }

    fun checkDate(user: User){
        var x = user.calorie_time
        println(x)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val string = user.calorie_time.split("T").get(0) + " 00:00:00"
        val dateTime = LocalDateTime.parse(string, formatter)
        val dateTimeNow = LocalDateTime.parse(LocalDateTime.now().toString().split("T").get(0) + " 00:00:00", formatter)
        if (dateTime != dateTimeNow){
            val mainActivity = activity as MainActivity
            mainActivity.rearangeCalories(user)
        }
    }


}