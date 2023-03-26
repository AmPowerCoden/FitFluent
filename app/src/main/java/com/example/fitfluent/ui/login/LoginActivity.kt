package com.example.fitfluent.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.fitfluent.MainActivity
import com.example.fitfluent.databinding.ActivityLoginBinding

import com.example.fitfluent.R
import com.example.fitfluent.Register_Activity
import com.example.fitfluent.data.DatabaseReader

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get references to UI elements
        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val register = binding.register
        // Initialize database reader and enable register button
        val dbReader = DatabaseReader(this)
        if (register != null) {
            register.isEnabled = true
        }

        // Create the database if it doesn't exist
        dbReader.onCreate(dbReader.writableDatabase)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // Disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            // Display error messages for username and password fields, if any
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            // Hide loading spinner
            loading.visibility = View.GONE
            // Display error message if login was unsuccessful
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            // Update UI with logged in user information if login was successful
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            // Complete and destroy login activity once successful
            finish()
        })

        // Update login form state and login when username or password fields change
        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString(),
                            dbReader
                        )
                }
                false
            }
            // Log user in when login button is clicked
            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                val logged_user = loginViewModel.login(username.text.toString(), password.text.toString(), dbReader)
                // Start MainActivity with logged in user information if login was successful
                if (logged_user.username != ""){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("logged_user", logged_user)
                    startActivity(intent)
                }
            }
        }

        if (register != null) {
            register.setOnClickListener {
                val intent = Intent(this, Register_Activity::class.java)
                startActivity(intent)
            }
        }


    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName


        // Show a toast with the welcome message and the user's display name
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        // Show a toast with the login failed message
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
