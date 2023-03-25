package com.example.fitfluent.ui.bmi

object Calculation {

    fun checkAdult(age: Int, result: Float): String {
        val category:String = when (age) {
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

    fun getSuggestions(result: Float): String {
        val suggestion: String = when {
            result < 18.5 -> {
                "A BMI of under 18.5 indicates that a person has insufficient weight, so they may need to put on some weight. They should ask a doctor or dietitian for advice."
            }
            result in 18.5..24.9 -> {
                "A BMI of 18.5–24.9 indicates that a person has a healthy weight for their height. By maintaining a healthy weight, they can lower their risk of developing serious health problems."
            }
            result < 25 && result >=29.9 -> {
                "A BMI of 25–29.9 indicates that a person is slightly overweight. A doctor may advise them to lose some weight for health reasons. They should talk with a doctor or dietitian for advice."
            }
            else -> {
                "A BMI of over 30 indicates that a person has obesity. Their health may be at risk if they do not lose weight. They should talk with a doctor or dietitian for advice."
            }
        }
        return suggestion
    }
}