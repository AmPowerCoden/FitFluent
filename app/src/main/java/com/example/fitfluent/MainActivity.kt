package com.example.fitfluent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitfluent.data.User
import com.example.fitfluent.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.ui.bmi.BmiFragment
import com.example.fitfluent.ui.exercise.ExerciseFragment
import com.example.fitfluent.ui.food.FoodFragment
import com.example.fitfluent.ui.workout.WorkoutFragment
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logged_user = intent.getSerializableExtra("logged_user") as User

        var nutrition = getNutritionIngredient("chicken")

        val bundle = Bundle().apply {
            putSerializable("logged_user", logged_user)
        }



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)


        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bmiFragment, R.id.foodFragment, R.id.exerciseFragment, R.id.workoutFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

    }

    fun lowerCalories(user: User, calories: Double){
        val dbReader = DatabaseReader(this)

        var new_user = User(user.username, user.password, user.height_in_cm, user.weight_in_kg, user.age, user.calorie_intake - calories, user.calorie_time, user.activity_level)

        dbReader.updateUser(user, new_user)
    }

    fun rearangeCalories(user:User){
        val dbReader = DatabaseReader(this)

        var new_user = User(user.username, user.password, user.height_in_cm, user.weight_in_kg, user.age, (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age)), user.calorie_time, user.activity_level)

        dbReader.updateUser(user, new_user)
    }

    fun getLoggedUser() : User{
        val dbReader = DatabaseReader(this)
        val user = intent.getSerializableExtra("logged_user") as User
        val newuser = dbReader.getUser(user.username, user.password)
        return newuser
    }

    fun getNutritionIngredient(food: String) : String = runBlocking{
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://api.edamam.com/api/food-database/v2/parser?app_id=759dd5cd&app_key=36de7eace633d3e2c8dd40e94fe0c390&ingr=" + food)
            .build()

        var nutrition : String
        nutrition = ""

        val response = client.newCall(request).await()

        if (response.isSuccessful) {
            nutrition = response.body?.string() ?: ""
        } else {
            nutrition = "API request failed"
        }

        nutrition
    }


    suspend fun Call.await(): Response = suspendCancellableCoroutine { cont ->
        enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                cont.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (cont.isActive) {
                    cont.resume(response)
                }
            }
        })

        cont.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                // Ignore cancel exception
            }
        }
    }


}