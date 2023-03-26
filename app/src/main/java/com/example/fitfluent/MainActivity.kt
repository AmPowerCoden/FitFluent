package com.example.fitfluent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitfluent.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.fitfluent.data.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //dropDBs() - this line is commented out, so it won't run when the app is launched

        // Inflate the layout and set it as the content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get the logged in user object from the previous activity
        val logged_user = intent.getSerializableExtra("logged_user") as User
        // Get nutrition information for an ingredient (this is just an example, the variable isn't used elsewhere)
        var nutrition = getNutritionIngredient("chicken")
        // Create a bundle to pass the user object to the fragments
        val bundle = Bundle().apply {
            putSerializable("logged_user", logged_user)
        }

        // Set up the bottom navigation view and connect it to the nav controller
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

    // Function to lower a user's calorie intake by a specified amount
    fun lowerCalories(user: User, calories: Double){
        val dbReader = DatabaseReader(this)

        var new_user = User(user.username, user.password, user.height_in_cm, user.weight_in_kg, user.age, user.calorie_intake - calories, user.calorie_time, user.activity_level, user.gender, user.bmi_score)

        dbReader.updateUser(user, new_user)
    }

    // Function to recalculate a user's calorie intake based on their profile information
    fun rearangeCalories(user:User){
        val dbReader = DatabaseReader(this)

        var new_user = User(user.username, user.password, user.height_in_cm, user.weight_in_kg, user.age, (655.1 + (9.6 * user.weight_in_kg) + (1.8 * user.height_in_cm) - (4.7 * user.age)), user.calorie_time, user.activity_level, user.gender, user.bmi_score)

        dbReader.updateUser(user, new_user)
    }

    // Function to retrieve the logged in user object from the database
    fun getLoggedUser() : User{
        val dbReader = DatabaseReader(this)
        val user = intent.getSerializableExtra("logged_user") as User
        val newuser = dbReader.getUser(user.username, user.password)
        return newuser
    }

    fun getNutritionIngredient(food: String) : String = runBlocking{
        // Create an instance of the OkHttp client
        val client = OkHttpClient()

        // Create a request using the provided `food` parameter and the Edamam Food Database API credentials
        val request = Request.Builder()
            .url("https://api.edamam.com/api/food-database/v2/parser?app_id=759dd5cd&app_key=36de7eace633d3e2c8dd40e94fe0c390&ingr=" + food)
            .build()

        // Initialize the `nutrition` variable to an empty string
        var nutrition : String
        nutrition = ""

        // Send the request asynchronously and wait for the response using Kotlin coroutines
        val response = client.newCall(request).await()

        // If the response is successful, set the `nutrition` variable to the string response from the API.
        // Otherwise, set the `nutrition` variable to the string "API request failed".
        if (response.isSuccessful) {
            nutrition = response.body?.string() ?: ""
        } else {
            nutrition = "API request failed"
        }

        // Return the `nutrition` variable
        nutrition
    }

    // This function retrieves a list of workouts for the given user from the database
    fun getWorkouts (user: User) : MutableList<Workout>
    {
        // Create a new instance of the DatabaseReaderWorkouts class
        val dbReaderWorkouts =DatabaseReaderWorkouts(this)
        // Call the getWorkouts method of the DatabaseReaderWorkouts object and pass in the user object
        return dbReaderWorkouts.getWorkouts(user)
    }

    // This function adds a new workout to the database
    fun registerWorkout (workout: Workout){
        // Create a new instance of the DatabaseReaderWorkouts class
        val dbReaderWorkouts = DatabaseReaderWorkouts(this)

        // Call the registerWorkout method of the DatabaseReaderWorkouts object and pass in the workout object
        dbReaderWorkouts.registerWorkout(workout)
    }

    // This function retrieves a list of exercises from the database
    fun getExercises() : MutableList<Exercise>
    {
        // Create a new instance of the DatabaseReaderExercises class
        val DatabaseReaderExercises = DatabaseReaderExercises(this)

        // Call the getExercises method of the DatabaseReaderExercises object
        return DatabaseReaderExercises.getExercises()
    }

    // This function is an extension function for the Call class that allows it to be used with coroutines
    suspend fun Call.await(): Response = suspendCancellableCoroutine { cont ->
        // Enqueue a callback for when the HTTP response is received
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

        // If the coroutine is cancelled, cancel the HTTP request
        cont.invokeOnCancellation {
            try {
                cancel()
            } catch (ex: Throwable) {
                // Ignore cancel exception
            }
        }
    }

    // This function creates a new database for workouts if one does not already exist
    fun createWorkoutDb()
    {
        // Create a new instance of the DatabaseReaderWorkouts class
        val dbReader = DatabaseReaderWorkouts(this)

        // Call the createIfNotExists method of the DatabaseReaderWorkouts object to create a new database
        dbReader.createIfNotExists()
    }

    // This function drops the databases for the main app and workouts
    fun dropDBs()
    {
        // Create new instances of the DatabaseReader and DatabaseReaderWorkouts classes
        val dbReader = DatabaseReader(this)
        val dbWorkoutsReader = DatabaseReaderWorkouts(this)

        // Call the dropDB method of the DatabaseReader object to drop the main app database
        dbReader.dropDB()
        // Call the dropDB method of the DatabaseReaderWorkouts object to drop the workouts database
        dbWorkoutsReader.dropDB()
    }


}