package com.example.fitfluent.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfluent.data.Exercise
import com.example.fitfluent.databinding.ItemExerciseBinding

class ExerciseAdapter(val exerciseList: List<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    inner class ExerciseViewHolder(val binding: ItemExerciseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding : ItemExerciseBinding = ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val context = holder.binding.root.context
        val c = exerciseList[position]
        val name = c.name

        holder.binding.exerciseHeading.text = c.name
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}