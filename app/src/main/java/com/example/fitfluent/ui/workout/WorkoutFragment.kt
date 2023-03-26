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

        mainActivity.createWorkoutDb()

        val workouts = mainActivity.getWorkouts(user)

        workouts.removeAt(0)

        var counter = 0

        workouts.forEach {
            if (it.ersteller == "auto")
                counter++
        }

        if (counter == 0)
        {

            // Erstes Workout im klassichen 2er Split
            // Workout 1 -- Untergewicht -- Tag 1 Bauch&Beine -- BMI bis 18  -- 2 Sätze
            mainActivity.registerWorkout(Workout("auto", "wiederholungen", "Wandsitzen, Squats, Planke, Bergsteiger, Wadenheber, Crunches, Arm- und Beinstecker im Vierfuesslerstand", "12-15, 12-15, 12-15, 12-15, 12-15, 12-15, 12-15" , "Dienstag", "0 - 18.50"))

            // Workout 1 -- Untergewicht -- Tag 2 Oberkörper -- BMI bis 18 -- 2 Sätze
            mainActivity.registerWorkout(Workout("auto", "wiederholungen", "Liegestuetze, Dips an Stuhlkannte, Bizeps mit Fitnessband oder Wasserflasche, Seitstuetz, Schulterdruecken am Tuerrahmen, Superman, Schulterbruecke", "12-15, 12-15, 12-15, 12-15, 12-15, 12-15, 12-15" , "Freitag", "0 - 18.50"))


            //###############

            // Zweites Workout im 3er Split
            // Workout 2 -- Normalgewicht -- Tag 1 Brust&Arme -- BMI 19 bis 25 -- 2 Sätze
            mainActivity.registerWorkout(Workout("auto", "wiederholungen", "Liegestuetze, Dips an Stuhlkannte, Bizeps mit Fitnessband oder Wasserflasche, Klimmzuege, Arm- und Beinstecker im Vierfuesslerstand", "12-15, 12-15, 12-15, 12-15, 12-15" , "Montag", "18.51 - 25"))

            // Workout 2 -- Normalgewicht -- Tag 2 Bauch&Beine -- BMI 19 bis 25 -- 2 Sätze
            mainActivity.registerWorkout(Workout("auto", "wiederholungen", "Wandsitzen, Squats, Ausfallschritte mit Rotation, Planke, Wadenheber, Bergsteiger, Crunches", "12-15, 12-15, 12-15, 12-15, 12-15, 12-15, 12-15" , "Mittwoch", "19 - 25"))

            // Workout 2 -- Normalgewicht -- Tag 3 Rücken&Schultern -- BMI 19 bis 25 -- 2 Sätze
            mainActivity.registerWorkout(Workout("auto", "wiederholungen", "Kreuzheben mit Flaschen, Klimmzuege, Latziehen mit Fitnessband, Seitstuetz, Schulterbruecke, Schulterdruecken am Tuerrahmen", "12-15, 12-15, 12-15, 12-15, 12-15, 12-15" , "Freitag", "18.51 - 25"))


            //###############

            // Drittes Workout im Zirkel mit 2 Durchgängen
            // Workout 3 -- Übergewicht -- Tag 1 Ganzkörper -- BMI 26 bis 30 -- 2 Durchgänge / 1 Wiederholung des Zirkels
            mainActivity.registerWorkout(Workout("auto", "time-intervall", "Klimmzuege, Liegestuetze, Ausfallschritte mit Rotation, Schulterbruecke, Wandsitzen, Seitstuetz, Arm- und Beinstecker im Vierfuesslerstand, Planke", "60, 60, 60, 60, 60, 60, 60, 60" , "Montag", "26 - 30"))

            // Workout 3 -- Übergewicht -- Tag 2 Ganzkörper -- BMI 26 bis 30 -- 2 Durchgänge / 1 Wiederholung des Zirkels
            mainActivity.registerWorkout(Workout("auto", "time-intervall", "Klimmzuege, Liegestuetze, Ausfallschritte mit Rotation, Schulterbruecke, Wandsitzen, Seitstuetz, Arm- und Beinstecker im Vierfuesslerstand, Planke", "60, 60, 60, 60, 60, 60, 60, 60" , "Mittwoch", "26 - 30"))

            // Workout 3 -- Übergewicht -- Tag 3 Ganzkörper -- BMI 26 bis 30 -- 2 Durchgänge / 1 Wiederholung des Zirkels
            mainActivity.registerWorkout(Workout("auto", "time-intervall", "Klimmzuege, Liegestuetze, Ausfallschritte mit Rotation, Schulterbruecke, Wandsitzen, Seitstuetz, Arm- und Beinstecker im Vierfuesslerstand, Planke", "60, 60, 60, 60, 60, 60, 60, 60" , "Freitag", "26 - 30"))


            //###############

            // Viertes Workout im Zirkel mit 3 Durchgängen
            // Workout 4 -- Starkes Übergewicht -- Tag 1 Ganzkörper -- BMI 31 bis 50 -- 3 Durchgänge / 2 Wiederholung des Zirkels
            mainActivity.registerWorkout(Workout("auto", "time-intervall", "Klimmzuege, Liegestuetze, Ausfallschritte mit Rotation, Schulterbruecke, Wandsitzen, Seitstuetz, Arm- und Beinstecker im Vierfuesslerstand, Planke", "60, 60, 60, 60, 60, 60, 60, 60" , "Montag", "31 - 50"))

            // Workout 4 -- Starkes Übergewicht -- Tag 2 Ganzkörper -- BMI 31 bis 50 -- 3 Durchgänge / 2 Wiederholung des Zirkels
            mainActivity.registerWorkout(Workout("auto", "time-intervall", "Klimmzuege, Liegestuetze, Ausfallschritte mit Rotation, Schulterbruecke, Wandsitzen, Seitstuetz, Arm- und Beinstecker im Vierfuesslerstand, Planke", "60, 60, 60, 60, 60, 60, 60, 60" , "Mittwoch", "31 - 50"))

            // Workout 4 -- Starkes Übergewicht -- Tag 3 Ganzkörper -- BMI 31 bis 50 -- 3 Durchgänge / 2 Wiederholung des Zirkels
            mainActivity.registerWorkout(Workout("auto", "time-intervall", "Klimmzuege, Liegestuetze, Ausfallschritte mit Rotation, Schulterbruecke, Wandsitzen, Seitstuetz, Arm- und Beinstecker im Vierfuesslerstand, Planke", "60, 60, 60, 60, 60, 60, 60, 60" , "Freitag", "31 - 50"))

        }


        var workoutsUebergabe = mainActivity.getWorkouts(user)
        workoutsUebergabe.removeAt(0)

        workoutList.addAll(workoutsUebergabe)

        binding.WorkoutList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.WorkoutList.adapter = workoutAdapter
        workoutAdapter.notifyDataSetChanged()
        binding.workoutHeading.text = "Hier sind Ihre Workouts"

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        // TODO: Use the ViewModel
    }

}