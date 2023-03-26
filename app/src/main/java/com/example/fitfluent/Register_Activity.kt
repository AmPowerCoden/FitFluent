package com.example.fitfluent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User
import com.example.fitfluent.databinding.ActivityRegisterBinding
import com.example.fitfluent.ui.login.LoginActivity

class Register_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get references to the input fields and buttons
        val username = binding.usernameRegister
        val password = binding.passwordRegister
        val age = binding.ageRegister
        val height = binding.heightRegister
        val weight = binding.weightRegister
        val activityLevel = binding.activityLevelRegister
        val dbReader = DatabaseReader(this)
        val register = binding.register
        val register_error = binding.registerError

        // Hide the error message
        register_error.visibility = View.INVISIBLE

        //dbReader.recreateDB()

        // Enable the register button
        register.isEnabled = true

        // Set a click listener for the register button
        register.setOnClickListener{
            // Try to register the user in the database
            try {
                dbReader.register_person(User(username.text.toString(), password.text.toString(), height.text.toString().toInt(), weight.text.toString().toInt(), age.text.toString().toInt(), 0.0, "", activityLevel.text.toString().toInt(),"", 0F))
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            // If there's an exception, show the error message
            catch (e : Exception){
                register_error.visibility = View.VISIBLE
            }

        }

        //setContentView(R.layout.activity_register)
    }
}