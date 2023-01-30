package com.example.fitfluent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.fitfluent.data.LoginRepository
import com.example.fitfluent.data.User
import com.example.fitfluent.databinding.ActivityLoginBinding
import com.example.fitfluent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logged_user = intent.getSerializableExtra("logged_user") as User
        val textBlock = binding.testBox

        textBlock.text = "Hello ${logged_user.username}! Check your workout options! By clicking one of the following buttons"

    }
}