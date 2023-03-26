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
import com.example.fitfluent.databinding.ActivityMainBinding
import com.example.fitfluent.databinding.ActivityRegisterBinding
import com.example.fitfluent.databinding.FragmentBmiBinding
import com.example.fitfluent.databinding.FragmentFoodBinding
import org.w3c.dom.Text
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FoodFragment: Fragment() {

    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = FoodFragment()
    }

    private lateinit var viewModel: FoodViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFoodBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity

        // Get references to the views we'll be using
        val headline = binding.headlineFood
        val ingredient_search = binding.searchBarFood
        val gram_search = binding.gramsBarFood
        val get_food_btn = binding.getFood
        val shown_food = binding.foundFood
        val subtract_calories_button = binding.subtractFood
        val subtract_food_error = binding.errorSubstract

        // Nutrition will be a string containing the response from the nutrition API
        var nutrition = ""

        // Get the logged in user from the MainActivity
        var user = mainActivity.getLoggedUser()

        // Check if today's date matches the user's last log date
        checkDate(user)

        // Refresh the user object after the checkDate function call
        user = mainActivity.getLoggedUser()

        // Calculate the usual calorie intake based on user data
        var usualCalories = (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age))

        // Enable the "Get Food" button by default
        get_food_btn.isEnabled = true

        // Set the click listener for the "Get Food" button
        get_food_btn.setOnClickListener{
            try {
                // Make the API call to get the nutrition information for the ingredient
                nutrition = mainActivity.getNutritionIngredient(ingredient_search.text.toString())
                // Parse the nutrition information and multiply it by the user's input grams
                var nutritients = getNutrients(nutrition)
                var gram_search_double = gram_search.text.toString().toDouble()

                // Set the "found food" text view to show the results of the API call
                shown_food.text = gram_search.text.toString() + " gramm vom Gericht " + ingredient_search.text.toString() + " haben folgende Nährwerte:" + makeShownFoodText(nutritients, gram_search_double)
                // Enable the "Subtract Food" button
                subtract_calories_button.isEnabled = true



            }
            catch (e: java.lang.Exception){
                // If there was an error with the API call, display an error message and disable the "Subtract Food" button
                shown_food.text = "There was an Error with your search... the api wasn't able to find any food with that name"
                subtract_calories_button.isEnabled = false
            }
        }

        // Set the click listener for the "Subtract Food" button
        subtract_calories_button.setOnClickListener{
            try {
                // Parse the calories from the "found food
                var calories = shown_food.text.toString().split("Calories: ").get(1).split(" kcal").get(0).toDouble()
                // Parse the calories from the "found food"
                mainActivity.lowerCalories(user, calories)
                // Update user object with latest changes
                user = mainActivity.getLoggedUser()

                // Calculate user's usual calorie intake using the Harris-Benedict equation
                usualCalories = (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age))
                // Update the headline based on the user's calorie intake and usual calorie intake
                if (user.calorie_intake > -(user.activity_level * 100))
                {
                    headline.text = "Hallo ${user.username}! Sie haben für heute noch ${user.calorie_intake + user.activity_level * 100} von ${usualCalories + user.activity_level * 100} offen"
                } else {
                    headline.text = "Hallo ${user.username}! Sie haben heut schon ${- (user.calorie_intake) - user.activity_level * 100} zu viel gegessen"
                }



            }
            // Catch any exceptions and show an error message if necessary
            catch (e : Exception)
            {
                subtract_food_error.isVisible = true
            }

        }


        if (user.calorie_intake > -(user.activity_level * 100))
        {
            binding.headlineFood.text = "Hallo ${user.username}! Sie haben für heute noch ${user.calorie_intake + user.activity_level * 100} von ${usualCalories + user.activity_level * 100} offen"
        } else {
            binding.headlineFood.text = "Hallo ${user.username}! Sie haben heut schon ${ - (user.calorie_intake) - user.activity_level * 100} zu viel gegessen"
        }

        user = mainActivity.getLoggedUser()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FoodViewModel::class.java)
    }

    // This function creates a list of nutrients from a given nutrition string
    fun getNutrients(nutrition: String) : List<String> {
        if (nutrition.startsWith("There was an Error")){
            // If there was an error in the nutrition string, return a list containing only the string "Error"
            return listOf<String>("Error")
        }
        else{
            // If there was no error, extract the nutrients from the nutrition string
            val nutrients_only = nutrition.split("\"nutrients\":{").get(1).split("},").get(0)
            val calories = nutrients_only.split("\"ENERC_KCAL\":").get(1).split(",").get(0)
            val proteine = nutrients_only.split("\"PROCNT\":").get(1).split(",").get(0)
            val fat = nutrients_only.split("\"FAT\":").get(1).split(",").get(0)
            val carbs = nutrients_only.split("\"CHOCDF\":").get(1).split(",").get(0)

            // Create a list containing the extracted nutrients
            val nutrients = listOf<String>(calories, proteine, fat, carbs)

            // Return the list of nutrients
            return nutrients
        }

    }

    // This function creates a string to display the nutritional information of a food
    fun makeShownFoodText(nutrients : List<String>, amaount : Double) : String {
        // Calculate the amount of each nutrient based on the given amount and create a string with the nutritional information
        var text = "\nCalories: " + (nutrients.get(0).toDouble() / 100 * amaount) + " kcal" +
                    "\nProtein: " + (nutrients.get(1).toDouble() / 100 * amaount) + " grams" +
                    "\nFat: " + (nutrients.get(2).toDouble() / 100 * amaount) + " grams" +
                    "\nCarbs: " + (nutrients.get(3).toDouble() / 100 * amaount) + "grams"

        // Return the nutritional information string
        return text

    }

    // This function checks if the user's calorie data is up to date and updates it if necessary
    fun checkDate(user: User){
        var x = user.calorie_time
        println(x)
        // Get the date from the user's calorie_time string
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val string = user.calorie_time.split("T").get(0) + " 00:00:00"
        val dateTime = LocalDateTime.parse(string, formatter)
        val dateTimeNow = LocalDateTime.parse(LocalDateTime.now().toString().split("T").get(0) + " 00:00:00", formatter)
        // Get the current date and time
        // If the user's calorie data is not up to date, rearrange the user's calorie data
        if (dateTime != dateTimeNow){
            val mainActivity = activity as MainActivity
            mainActivity.rearangeCalories(user)
        }
    }


}