package com.example.fitfluent.ui.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.fitfluent.data.LoginRepository

import com.example.fitfluent.R
import com.example.fitfluent.data.DatabaseReader
import com.example.fitfluent.data.User

/**
 * ViewModel for the LoginActivity.
 */

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * Tries to log in a user given a username and password.
     * @param username The user's username.
     * @param password The user's password.
     * @param dbReader An instance of DatabaseReader for querying user data.
     * @return A User object containing the user's data if login was successful.
     */

    fun login(username: String, password: String, dbReader: DatabaseReader) : User {
        // Can be launched in a separate asynchronous job.
        print("test")
        val result = loginRepository.login(username, password, dbReader)

        if (result.username != "") {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.username))

        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }

        return result
    }

    /**
     * Checks whether the inputted username and password are valid.
     * @param username The user's username.
     * @param password The user's password.
     */

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    /**
     * Checks whether the inputted username is valid.
     * @param username The user's username.
     * @return Whether the username is valid.
     */

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    /**
     * Checks whether the inputted password is valid.
     * @param password The user's password.
     * @return Whether the password is valid.
     */

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 0
    }
}