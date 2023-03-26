package com.example.fitfluent.ui.bmi

import android.annotation.SuppressLint
import android.graphics.Color.parseColor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitfluent.R
import com.example.fitfluent.databinding.FragmentBmiResultBinding


// This is the BmiResultFragment class which extends Fragment.
class BmiResultFragment : Fragment() {

    // Binding object instance corresponding to the fragment_bmi_result.xml layout
    private var _binding: FragmentBmiResultBinding? = null
    private val binding get() = _binding!!
    // Private variable to store the arguments passed to the fragment
    //private lateinit var viewModel: BmiViewModel
    private val args: BmiResultFragmentArgs by navArgs()


    // This method is called when the fragment is created. It inflates the layout for the fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBmiResultBinding.inflate(inflater, container, false)
        return binding.root
    }


    companion object {
        fun newInstance(bundle: Bundle) = BmiResultFragment()
    }


    // This method is called when the activity's onCreate() method has returned.
    // It sets up any necessary resources for the fragment.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(BmiViewModel::class.java)
    }

    // This method is called after the view is created.
    // It sets up the UI elements for the fragment and handles their interactions.
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply{
            // Set the BMI result text and age text based on the arguments passed to the fragment.
            yourBmi.text = "${args.bmi.toString() + " " + resources.getString(R.string.unit_kgm2)} "
            ageTxt.text = args.age

            // Check the BMI result and age to determine the BMI category and set the category text.
            var result = Calculation.checkAdult(args.age.toInt(), args.bmi)
            condition.text = "${result}"



            // Set the click listener for the "Recalculate" button.
            // It navigates to the BMI input fragment to allow the user to recalculate their BMI.
            recalculate.setOnClickListener {

                findNavController().navigate(R.id.action_bmiResultFragment_to_bmiFragment)
            }
        }


    }


    // This method is called when the view and its resources should be cleaned up.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}