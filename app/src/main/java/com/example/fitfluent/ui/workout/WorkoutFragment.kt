package com.example.fitfluent.ui.workout

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.fitfluent.MainActivity
import com.example.fitfluent.R
import com.example.fitfluent.data.Workout
import com.example.fitfluent.databinding.FragmentFoodBinding
import com.example.fitfluent.databinding.FragmentWorkoutBinding

class WorkoutFragment() : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private var workoutList: MutableList<Workout> = mutableListOf()
    private val workoutAdapter = WorkoutAdapter(workoutList)

    companion object {
        fun newInstance(bundle: Bundle) = WorkoutFragment()
    }

    private lateinit var viewModel: WorkoutViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)

        val mainActivity = activity as MainActivity

        val user = mainActivity.getLoggedUser()

        val workouts = mainActivity.getWorkouts(user)


        mainActivity.registerWorkout(Workout("auto", "wiederholungen", "klimmzug, liegestütz, dips", "12, 20, 16" , "Montag, Mittwoch, Freitag", "25 - 30"))

        workoutList.addAll(workouts)

        binding.WorkoutList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.WorkoutList.adapter = workoutAdapter
        workoutAdapter.notifyDataSetChanged()

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        // TODO: Use the ViewModel
    }

}