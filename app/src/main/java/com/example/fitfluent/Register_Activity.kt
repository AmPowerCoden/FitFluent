package com.example.fitfluent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User
import com.example.fitfluent.databinding.ActivityLoginBinding
import com.example.fitfluent.databinding.ActivityRegisterBinding
import com.example.fitfluent.ui.login.LoginActivity

class Register_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.usernameRegister
        val password = binding.passwordRegister
        val age = binding.ageRegister
        val height = binding.heightRegister
        val weight = binding.weightRegister
        val dbReader = DatabaseReader(this)
        val register = binding.register
        val register_error = binding.registerError

        register_error.visibility = View.INVISIBLE


        register.isEnabled = true


        register.setOnClickListener{
            try {
                dbReader.register_person(User(username.text.toString(), password.text.toString(), height.text.toString().toInt(), weight.text.toString().toInt(), age.text.toString().toInt()))
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            catch (e : Exception){
                register_error.visibility = View.VISIBLE
            }

        }

        //setContentView(R.layout.activity_register)
    }
}