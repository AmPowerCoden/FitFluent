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
    // Create a mutable list to store exercises
    private var exerciseList: MutableList<Exercise> = mutableListOf()
    // Initialize the adapter with the exercise list
    private val exerciseAdapter = ExerciseAdapter(exerciseList)

    // Define a companion object to create a new instance of the fragment with a bundle
    companion object {
        fun newInstance(bundle: Bundle) = ExerciseFragment()
    }

    private lateinit var viewModel: ExerciseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)

        // Get the main activity instance
        val mainActivity = activity as MainActivity

        mainActivity.createExerciseDb()

        // Get the list of exercises from the main activity
        val exercises = mainActivity.getExercises()

        // Add the exercises to the list
        exerciseList.addAll(exercises)

        // Set up the RecyclerView and attach the adapter
        binding.ExerciseList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.ExerciseList.adapter = exerciseAdapter
        exerciseAdapter.notifyDataSetChanged()

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Initialize the view model for this fragment
        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        // TODO: Use the ViewModel
    }

}