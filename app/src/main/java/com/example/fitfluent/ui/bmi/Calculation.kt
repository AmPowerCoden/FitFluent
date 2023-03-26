package com.example.fitfluent.ui.bmi

object Calculation {

    fun checkAdult(age: Int, result: Float): String {
        val category: String = when (age) {
            in 2..19 -> {
                getAdultCategory(result)
            }
            else -> {
                getChildCategory(result)
            }
        }
        return category
    }

    private fun getAdultCategory(result: Float): String {
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
        return category
    }

    private fun getChildCategory(result: Float): String {
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
        return category
    }
}