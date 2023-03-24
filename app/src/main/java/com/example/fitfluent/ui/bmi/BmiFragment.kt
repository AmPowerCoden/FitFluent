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
import com.example.fitfluent.data.User
import com.example.fitfluent.databinding.FragmentBmiBinding
import kotlin.math.log

class BmiFragment() : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BmiViewModel

    var height: Float? = null
    private var chosen: Boolean = true



    companion object {
        fun newInstance(bundle: Bundle) = BmiFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BmiViewModel::class.java)
        // TODO: Use the ViewModel

        var loged_user = (activity as MainActivity).getLoggedUser()

        viewModel.getData(loged_user)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var logged_user = (activity as MainActivity).getLoggedUser()




        binding.welcomeText.text = "Hallo ${logged_user.username}! Mit diesem Calculator kannst du deinen BMI berechnen und daraus einen Trainingsplan erstellen lassen. "
        binding.Seekbar.progress = logged_user.height_in_cm
        binding.heightTxt.text = "${logged_user.height_in_cm.toString() + resources.getString(R.string.unit_cm)} "
        binding.weightTxt.text  = "${logged_user.weight_in_kg.toString() + resources.getString(R.string.unit_kg)} "

        binding.age.text  = "${logged_user.age.toString()}"


        binding.apply {



            cardViewMale.setOnClickListener {
                if(chosen) {
                    maleTxt.setTextColor(Color.parseColor("#FFFFFF"))
                    maleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_male_white, 0, 0)
                    cardViewFemale.isEnabled = false
                    logged_user.gender = "male"
                    chosen = false

                } else {
                    maleTxt.setTextColor(Color.parseColor("#8D8E99"))
                    maleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_male, 0, 0)
                    cardViewFemale.isEnabled = true
                    chosen = true
                }
            }

            cardViewFemale.setOnClickListener {
                if(chosen) {
                    femaleTxt.setTextColor(Color.parseColor("#FFFFFF"))
                    femaleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_female_white, 0, 0)
                    cardViewMale.isEnabled = false
                    logged_user.gender = "female"
                    chosen = false

                } else {
                    femaleTxt.setTextColor(Color.parseColor("#8D8E99"))
                    femaleTxt.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_bmi_female, 0, 0)
                    cardViewMale.isEnabled = true
                    chosen = true
                }
            }







            Seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val ht = progress.toString() + resources.getString(R.string.unit_cm)
                    binding.heightTxt.text = ht
                    height = progress.toFloat() / 100
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })


/*
            weightPlus.setOnClickListener {
                binding.weightTxt.text = "${logged_user.weight_in_kg++.toString() + resources.getString(R.string.unit_kg)} "
            }

            weightMinus.setOnClickListener {
                binding.weightTxt.text = "${logged_user.weight_in_kg--.toString() + resources.getString(R.string.unit_kg)} "
            }
*/
            weightPlus.setOnClickListener {
                binding.weightTxt.text = "${viewModel._weight++.toString() + resources.getString(R.string.unit_kg)} "
            }

            weightMinus.setOnClickListener {
                binding.weightTxt.text = "${viewModel._weight--.toString() + resources.getString(R.string.unit_kg)} "
            }


            agePlus.setOnClickListener {
                binding.age.text = logged_user.age++.toString()
            }

            ageMinus.setOnClickListener {
                binding.age.text = logged_user.age--.toString()
            }



        }

        binding.calculate.setOnClickListener {
            findNavController().navigate(R.id.action_bmiFragment_to_bmiResultFragment)
        }

    }




        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }







}