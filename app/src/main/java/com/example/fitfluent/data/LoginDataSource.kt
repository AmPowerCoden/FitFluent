package com.example.fitfluent.data

import com.example.fitfluent.data.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

// A class that handles authentication with login credentials and retrieves user information
class LoginDataSource {

    /**
     * Authenticates a user given a username and password and returns the User object if successful.
     * @param username The user's username.
     * @param password The user's password.
     * @param dbReader The DatabaseReader object used to access the database.
     * @return A User object representing the authenticated user.
     */

    // A function that authenticates the user with the given credentials and retrieves the user information from the database
    fun login(username: String, password: String, dbReader: DatabaseReader): User {
        try {
            // Create a fake user for demonstration purposes
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            // Retrieve the user information from the database using the provided database reader
            val logged_user = dbReader.getUser(username, password)
            // Return the retrieved user information
            return logged_user



        } catch (e: Throwable) {
            // If an exception is thrown, return an empty user object
            return User("", "", 0, 0, 0, 0.0, "", 0, "", 0F)
        }
    }

    /**
     * Revokes authentication for the current user.
     */
    fun logout() {
        // TODO: revoke authentication
    }
}