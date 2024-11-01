package com.example.movieapps.ui.component

import android.util.Patterns

fun String.checkError(type: FieldType, confirmPass: String = ""): String {
    if (this.isEmpty() || this.isBlank()) {
        return "This field cannot be empty"
    }

    return when(type) {
        FieldType.EMAIL_FIELD -> {
            if(!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
                "This email format is invalid"
            } else ""
        }
        FieldType.PASSWORD_FIELD -> {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
            val passwordMatcher = Regex(passwordPattern)
            if (!passwordMatcher.matches(this)) {
                return "Minimum 8 characters with at least 1 letter, 1 number and 1 special character"
            } else ""
        }
        FieldType.CONFIRM_PASSWORD_FIELD -> {
            if (this != confirmPass) {
                return "Confirm Password does not match. Please check and try again."
            } else ""
        }
        else -> ""
    }
}

enum class FieldType {
    EMAIL_FIELD,
    NAME_FIELD,
    PASSWORD_FIELD,
    CONFIRM_PASSWORD_FIELD
}