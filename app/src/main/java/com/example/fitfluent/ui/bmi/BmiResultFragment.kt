package com.example.fitfluent.ui.bmi

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



class BmiResultFragment : Fragment() {

    private var _binding: FragmentBmiResultBinding? = null
    private val binding get() = _binding!!
    //private lateinit var viewModel: BmiViewModel
    private val args: BmiResultFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBmiResultBinding.inflate(inflater, container, false)
        return binding.root
    }

/*
    companion object {
        fun newInstance(bundle: Bundle) = BmiResultFragment()
    }
*/

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(BmiViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.yourBmi.text = "${args.bmi.toString() + resources.getString(R.string.unit_kgm2)} "
        binding.ageTxt.text = args.age


        var result = Calculation.checkAdult(args.age.toInt(), args.bmi)
        binding.condition.text = "${result}"



        binding.recalculate.setOnClickListener {

            findNavController().navigate(R.id.action_bmiResultFragment_to_bmiFragment)


        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}