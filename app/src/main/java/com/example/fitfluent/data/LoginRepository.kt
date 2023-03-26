package com.example.fitfluent.data

import com.example.fitfluent.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    public var user: User? = null
        private set

    // Check if user is logged in
    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        // Initialize user to null
        user = null
    }

    // Log out user and revoke authentication
    fun logout() {
        user = null
        dataSource.logout()
    }

    // Handle login
    fun login(username: String, password: String, dbReader: DatabaseReader): User {
        // Call login function of dataSource
        val result = dataSource.login(username, password, dbReader)
        // Set the loggedInUser object in memory


        setLoggedInUser(result)

        // Return the loggedInUser object
        return result
    }

    // Set the loggedInUser object in memory
    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

}