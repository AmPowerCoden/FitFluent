package com.example.fitfluent.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfluent.data.Exercise
import com.example.fitfluent.databinding.ItemExerciseBinding

// ExerciseAdapter class that extends RecyclerView.Adapter
class ExerciseAdapter(val exerciseList: List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    // ExerciseViewHolder class that extends RecyclerView.ViewHolder and takes in a binding parameter
    inner class ExerciseViewHolder(val binding: ItemExerciseBinding) : RecyclerView.ViewHolder(binding.root)

    // onCreateViewHolder function that returns an ExerciseViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        // Inflate item_exercise layout using the provided LayoutInflater and parent view
        val binding : ItemExerciseBinding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Return ExerciseViewHolder object with the inflated binding as parameter
        return ExerciseViewHolder(binding)
    }

    // onBindViewHolder function that sets the data to the views of an ExerciseViewHolder
    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        // Get the context of the root view of the binding
        val context = holder.binding.root.context
        // Get the Exercise object from the exerciseList at the specified position
        val c = exerciseList[position]
        // Get the name of the Exercise object
        val name = c.name
        val description = c.description
        val image = c.image


        // Set the text of the exerciseHeading view of the binding to the name of the Exercise object
        holder.binding.exerciseHeading.text = c.name
        holder.binding.exerciseDescription.text = c.description

    }

    // getItemCount function that returns the size of the exerciseList
    override fun getItemCount(): Int {
        return exerciseList.size
    }
}