package com.example.fitfluent.data

import com.example.fitfluent.data.model.LoggedInUser

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String, dbReader: DatabaseReader): User {
        try {
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            val logged_user = dbReader.getUser(username, password)
            return logged_user



        } catch (e: Throwable) {
            return User("", "", 0, 0, 0, 0.0, "", 0, "", 0F)
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}