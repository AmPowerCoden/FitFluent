// Declare a package for this code, indicating it belongs to a specific module or project
package com.example.fitfluent.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * @param userId The unique identifier of the logged-in user
 * @param displayName The name or display name of the logged-in user
 */
data class LoggedInUser(
    val userId: String,         // A val property to store the user ID as a string
    val displayName: String     // A val property to store the user's display name as a string
)