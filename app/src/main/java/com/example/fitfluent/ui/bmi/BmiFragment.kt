package com.example.fitfluent.ui.bmi

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fitfluent.MainActivity
import com.example.fitfluent.R
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User
import com.example.fitfluent.databinding.FragmentBmiBinding
import kotlin.math.log

// Define the BmiFragment class
class BmiFragment() : Fragment() {

    // Initialize the view binding variable
    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!

    // Initialize the view model variable
    private lateinit var viewModel: BmiViewModel

    // Initialize the height and chosen variables
    var height: Float? = null
    private var chosen: Boolean = true

    // Define the companion object
    companion object {
        fun newInstance(bundle: Bundle) = BmiFragment()
    }


    // Create the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        return binding.root
    }



    // Create the activity for the fragment
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Initialize the view model
        viewModel = ViewModelProvider(this).get(BmiViewModel::class.java)

        // Get the logged in user
        var logged_user = (activity as MainActivity).getLoggedUser()

        viewModel.getData(logged_user)

        // Set the data in the UI
        binding.apply {

            welcomeText.text = "Hallo ${logged_user.username}! Mit diesem Rechner kannst du deinen BMI berechnen lassen."
            Seekbar.progress = logged_user.height_in_cm
            heightTxt.text = "${logged_user.height_in_cm.toString() + resources.getString(R.string.unit_cm)} "
            weightTxt.text = "${logged_user.weight_in_kg.toString() + resources.getString(R.string.unit_kg)} "
            age.text = "${logged_user.age.toString()}"

            if (logged_user.gender == "male") {
                maleTxt.setTextColor(Color.parseColor("#FFFFFF"))
                maleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_male_white, 0, 0)
                cardViewFemale.isEnabled = false
                viewModel._gender = "male"
                chosen = false

            } else if(logged_user.gender == "female"){
                femaleTxt.setTextColor(Color.parseColor("#FFFFFF"))
                femaleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_female_white, 0, 0)
                cardViewMale.isEnabled = false
                viewModel._gender = "female"
                chosen = false
            }
        }

    }


    // Create the view for the fragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set the click listeners for the UI elements
        binding.apply {
            // Handle the click event for the male card view
            cardViewMale.setOnClickListener {
                if(chosen) {
                    // If already selected, reset to default state and disable the female card view
                    maleTxt.setTextColor(Color.parseColor("#FFFFFF"))
                    maleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_male_white, 0, 0)
                    cardViewFemale.isEnabled = false
                    viewModel._gender = "male"
                    chosen = false

                } else {
                    // If not selected, change appearance and enable the female card view
                    maleTxt.setTextColor(Color.parseColor("#8D8E99"))
                    maleTxt.setTextColor(Color.parseColor("#8D8E99"))
                    maleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_male, 0, 0)
                    cardViewFemale.isEnabled = true
                    chosen = true
                }
            }

            // Handle the click event for the female card view
            cardViewFemale.setOnClickListener {
                if(chosen) {
                    // If already selected, reset to default state and disable the male card view
                    femaleTxt.setTextColor(Color.parseColor("#FFFFFF"))
                    femaleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_female_white, 0, 0)
                    cardViewMale.isEnabled = false
                    viewModel._gender = "female"
                    chosen = false

                } else {
                    // If not selected, change appearance and enable the male card view
                    femaleTxt.setTextColor(Color.parseColor("#8D8E99"))
                    femaleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_female, 0, 0)
                    cardViewMale.isEnabled = true
                    chosen = true
                }
            }

            // Handle the seekbar value change event
            Seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    // Update the height text view and view model variable
                    val ht = progress.toString() + resources.getString(R.string.unit_cm)
                    binding.heightTxt.text = ht
                    viewModel._height = progress
                    //height = progress.toFloat() / 100
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })


            // Handle the click events for the weight plus and minus buttons
            weightPlus.setOnClickListener {
                binding.weightTxt.text = "${viewModel._weight++.toString() + resources.getString(R.string.unit_kg)} "
            }

            weightMinus.setOnClickListener {
                binding.weightTxt.text = "${viewModel._weight--.toString() + resources.getString(R.string.unit_kg)} "
            }


            // Handle the click events for the age plus and minus buttons
            agePlus.setOnClickListener {
                binding.age.text = viewModel._age++.toString()
            }

            ageMinus.setOnClickListener {
                binding.age.text = viewModel._age--.toString()
            }
        }

        // Handle the click event for the calculate button
        binding.calculate.setOnClickListener {

            // Navigate to the BMI result fragment with the calculated BMI and user age
            //findNavController().navigate(R.id.bmiResultFragment)
            //val action = BmiFragmentDirections.actionBmiFragmentToBmiResultFragment(viewModel.calculate_bmi(), viewModel._age)
            val action = BmiFragmentDirections.actionBmiFragmentToBmiResultFragment(viewModel.calculate_bmi(), viewModel._age.toString())

            // Get the logged in user and save their updated data to the database
            var logged_user = (activity as MainActivity).getLoggedUser()
            saveData(logged_user)

            findNavController().navigate(action)
        }

    }

    // Save updated user data to the database
    fun saveData(user : User)
    {
        val dbReader = getContext()?.let { DatabaseReader(it) }

        var new_user = User(user.username, user.password, viewModel._height, viewModel._weight, viewModel._age, user.calorie_intake, user.calorie_time, user.activity_level, viewModel._gender, viewModel._bmi_score)
        dbReader?.updateUser(user, new_user)
    }



        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }







}