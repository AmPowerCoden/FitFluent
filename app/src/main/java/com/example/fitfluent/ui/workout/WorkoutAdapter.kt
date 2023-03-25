package com.example.fitfluent.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfluent.data.Workout
import com.example.fitfluent.databinding.ItemWorkoutBinding

class WorkoutAdapter(val workoutList: List<Workout>) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    inner class WorkoutViewHolder(val binding: ItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding : ItemWorkoutBinding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val context = holder.binding.root.context
        val c = workoutList[position]
        val übungen = c.exercises.split(", ")
        val wiederholungen = c.times.split(", ")
        val sb = java.lang.StringBuilder()
        var x = ""
        if (c.typ.toString() == "time-intervall")
        {
            x = "s"
        }
        else if (c.typ.toString() == "wiederholungen")
        {
            x = "x"
        }
        var counter = 0
        übungen.forEach(){
            sb.append(it).append(": ").append(wiederholungen[counter]).append("$x; ")
            counter++
        }
        val string = sb.toString()
        holder.binding.workoutList.text = string
        holder.binding.workoutFrequency.text = c.frequency
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }
}