package com.example.fitfluent.ui.bmi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitfluent.R
import com.example.fitfluent.databinding.FragmentBmiResultBinding


class BmiResultFragment : Fragment() {

    private var _binding: FragmentBmiResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BmiViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBmiResultBinding.inflate(inflater, container, false)

        binding.recalculate.setOnClickListener {


            findNavController().navigate(R.id.action_bmiResultFragment_to_bmiFragment)
        }


        return binding.root
    }

/*
    companion object {
        fun newInstance(bundle: Bundle) = BmiResultFragment()
    }
*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}