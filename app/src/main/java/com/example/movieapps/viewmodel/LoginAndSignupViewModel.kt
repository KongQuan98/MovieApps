package com.example.movieapps.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginAndSignUpViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStateStatus()
    }

    private fun checkAuthStateStatus() {
        if (firebaseAuth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun performLogin(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("username", email)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val emailFound = documents.documents[0].getString("email")
                        emailFound?.let {
                            firebaseAuth.signInWithEmailAndPassword(it, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        _authState.value = AuthState.Authenticated
                                    } else {
                                        _authState.value =
                                            AuthState.Error("Authentication error: ${task.exception?.message}")
                                    }
                                }
                        }
                    } else {
                        _authState.value =
                            AuthState.Error("Authentication error: user name not found.")
                    }
                }
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value =
                            AuthState.Error("Authentication error: ${task.exception?.message}")
                    }
                }
        }

    }

    fun performSignup(email: String, password: String, username: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = firebaseAuth.currentUser?.uid
                    userId?.let {
                        val user = hashMapOf("username" to username, "email" to email)
                        FirebaseFirestore.getInstance().collection("users").document(it).set(user)
                    }
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error("Authentication error: ${task.exception?.message}")
                }
            }
    }

    fun performSignout() {
        firebaseAuth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState() {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val errorMessage: String) : AuthState()
}