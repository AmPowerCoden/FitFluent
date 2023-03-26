package com.example.fitfluent.ui.bmi

// Define a Kotlin object called Calculation to encapsulate BMI calculation functions
object Calculation {

    // Check if age falls within a certain range and return the corresponding BMI category
    fun checkAdult(age: Int, result: Float): String {
        // Use a when expression to choose between the adult and child BMI categories
        val category:String = when (age) {
            in 2..19 -> {
                // If age is between 2 and 19, use the child BMI category
                getAdultCategory(result)
            }
            else -> {
                // Otherwise, use the adult BMI category
                getChildCategory(result)
            }
        }
        // Return the BMI category as a string
        return category
    }

    // Determine the BMI category for adults based on a given result
    private fun getAdultCategory(result: Float): String {
        // Use an if-else statement to determine the correct BMI category based on the result
        val category: String = if (result < 15) {
            "Schweres Untergewicht"
        } else if (result in 15.0..16.0) {
            "Moderates Untergewicht"
        } else if (result > 16 && result <= 18.5) {
            "Leichtes Untergewicht"
        } else if (result > 18.5 && result <= 25) {
            "Normalgewicht"
        } else if (result > 25 && result <= 30) {
            "Übergewicht"
        } else if (result > 30 && result <= 35) {
            "Adipositas Grad I"
        } else if (result > 35 && result <= 40) {
            "Adipositas Grad II"
        } else {
            "Adipositas Grad III"
        }
        // Return the BMI category as a string
        return category
    }

    // Determine the BMI category for children based on a given result
    private fun getChildCategory(result: Float): String {
        // Use a when expression to determine the correct BMI category based on the result
        val category: String = when {
            result < 10 -> {
                "Untergewicht"
            }
            result in 10.0..90.0 -> {
                "Normalgewicht"
            }
            result > 90.0 && result <= 97.0 -> {
                "Übergewicht"
            }
            result > 97.0 && result <= 99.5 -> {
                "Adipositas"
            }
            else -> {
                "Extreme Adipositas"
            }
        }
        // Return the BMI category as a string
        return category
    }


}