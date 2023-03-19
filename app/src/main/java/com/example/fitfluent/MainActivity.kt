package com.example.fitfluent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.math.BigDecimal
import java.math.RoundingMode
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
        val BMI_Anzeige = binding.BMIAnzeige
        val BMI = BigDecimal(logged_user.weight_in_kg / ((logged_user.height_in_cm.toDouble() / 100) * (logged_user.height_in_cm.toDouble() / 100))).setScale(2, RoundingMode.HALF_EVEN)
        val BMI_text = getBmiText(BMI = BMI.toFloat())

        BMI_Anzeige.text = "Hello ${logged_user.username}! Your current BMI is ${BMI}. This means your weight is ${BMI_text}"

    }

    fun getBmiText(BMI : Float) : String{
        when (BMI) {
            in 1.0..17.0 -> return "Dangerously low. Please seek out a Doctor for further Advice!"
            in 17.001..19.0 -> return "A bit too low to be healthy. Please try to increase your calorie intake"
            in 19.001..25.0 -> return "Perfectly healthy. Try to keep your weight as it is right now!"
            in 25.001..30.0 -> return "A bit too high. Try to decrease your calorie intake"
            in 30.001.. 99.0 -> return "Dangerously high. Please seek out a Doctor for further Advice!"
            else -> return  "Serious it seems there has been an Error or values are really really unrealistic"
        }
    }
}