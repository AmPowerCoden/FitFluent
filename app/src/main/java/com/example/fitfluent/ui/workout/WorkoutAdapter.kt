package com.example.fitfluent.ui.workout

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.common.R
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfluent.data.Workout
import com.example.fitfluent.databinding.ItemWorkoutBinding

class WorkoutAdapter(val workoutList: List<Workout>) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    // inner class for the ViewHolder
    inner class WorkoutViewHolder(val binding: ItemWorkoutBinding) : RecyclerView.ViewHolder(binding.root)

    // create a new ViewHolder and inflate its layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding : ItemWorkoutBinding = ItemWorkoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WorkoutViewHolder(binding)
    }

    // bind data to the views in the ViewHolder
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val context = holder.binding.root.context
        val c = workoutList[position]
        val übungen = c.exercises.split(", ")
        val wiederholungen = c.times.split(", ")
        val sb = java.lang.StringBuilder()
        var x = ""
        // set workout description based on workout type
        if (c.typ.toString() == "time-intervall")
        {
            x = "s"
            holder.binding.workoutDescription.text = "Gehen Sie die Übungen mit jeweils einer Minute Pause durch. Fangen sie von vorne an wenn Sie fertig sind bis Sie die volle Rotation 3 mal gemacht haben."
        }
        else if (c.typ.toString() == "wiederholungen")
        {
            x = "x"
            holder.binding.workoutDescription.text = "Machen sie jede Übung 3 mal mit jeweils 1:30 Pause dazwischen. Wechseln sie danach immer zur nächsten Übunng bis Sie fertig sind."
        }
        var counter = 0
        // loop through exercises and repetitions and append to string builder
        übungen.forEach(){
            sb.append(it).append(": ").append(wiederholungen[counter]).append("$x; ")
            counter++
        }
        val string = sb.toString()
        // set workout heading, exercise list, and frequency
        holder.binding.workoutHeading.text = "Workout " + (position + 1).toString()
        holder.binding.workoutList.text = string
        holder.binding.workoutFrequency.text = c.frequency
        holder.binding.workoutFrequency.setBackgroundColor(Color.parseColor("#FFFFE3"))
    }

    // return the number of items in the workout list
    override fun getItemCount(): Int {
        return workoutList.size
    }
}