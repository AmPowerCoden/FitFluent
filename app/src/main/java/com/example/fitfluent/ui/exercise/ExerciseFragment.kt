package com.example.fitfluent.ui.exercise

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfluent.MainActivity
import com.example.fitfluent.R
import com.example.fitfluent.data.Exercise
import com.example.fitfluent.data.Workout
import com.example.fitfluent.databinding.FragmentExerciseBinding
import com.example.fitfluent.databinding.FragmentWorkoutBinding
import com.example.fitfluent.ui.workout.WorkoutAdapter
import com.example.fitfluent.ui.workout.WorkoutFragment
import com.example.fitfluent.ui.workout.WorkoutViewModel

class ExerciseFragment() : Fragment() {

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private var exerciseList: MutableList<Exercise> = mutableListOf()
    private val exerciseAdapter = ExerciseAdapter(exerciseList)

    companion object {
        fun newInstance(bundle: Bundle) = ExerciseFragment()
    }

    private lateinit var viewModel: ExerciseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity

        val exercises = mainActivity.getExercises()

        exerciseList.addAll(exercises)

        binding.ExerciseList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.ExerciseList.adapter = exerciseAdapter
        exerciseAdapter.notifyDataSetChanged()

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}