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
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logged_user = intent.getSerializableExtra("logged_user") as User



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


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.bmiFragment -> {
                    val fragment = BmiFragment.newInstance(bundle)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, fragment)
                        .commit()
                }
                R.id.foodFragment -> {
                    val fragment = FoodFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, fragment)
                        .commit()
                }
                R.id.exerciseFragment -> {
                    val fragment = ExerciseFragment.newInstance(bundle)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, fragment)
                        .commit()
                }
                R.id.workoutFragment -> {
                    val fragment = WorkoutFragment.newInstance(bundle)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, fragment)
                        .commit()
                }
            }
        }

        navView.setupWithNavController(navController)

    }

    fun lowerCalories(user: User, calories: Int){
        val dbReader = DatabaseReader(this)

        var new_user = User(user.username, user.password, user.height_in_cm, user.weight_in_kg, user.age, user.calorie_intake - 100, user.calorie_time, user.activity_level)

        dbReader.updateUser(user, new_user)
    }

    fun getLoggedUser() : User{
        val dbReader = DatabaseReader(this)
        val user = intent.getSerializableExtra("logged_user") as User
        val newuser = dbReader.getUser(user.username, user.password)
        return newuser
    }


}