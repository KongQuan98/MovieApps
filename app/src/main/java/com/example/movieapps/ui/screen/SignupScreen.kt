package com.example.movieapps.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.movieapps.R
import com.example.movieapps.ui.component.FieldType
import com.example.movieapps.ui.component.checkError
import com.example.movieapps.ui.theme.Purple40
import com.example.movieapps.ui.theme.PurpleGrey40
import com.example.movieapps.viewmodel.AuthState
import com.example.movieapps.viewmodel.LoginAndSignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue("")) }

    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    var isUserNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var isConfirmPassError by remember { mutableStateOf(false) }

    val authViewModel: LoginAndSignUpViewModel = hiltViewModel()
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(route = "movieList_screen")
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context, (authState.value as AuthState.Error).errorMessage, Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Back Button
        IconButton(onClick = {
            navController.navigate(route = "onboarding_screen") {
                popUpTo("onboarding_screen") { inclusive = true }
            }
        }) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Purple40
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.join_us_today),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Purple40,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = stringResource(id = R.string.signup_message),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = PurpleGrey40,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Email Field
        TextField(
            value = email,
            onValueChange = { value ->
                email = value
                isEmailError = value.text.checkError(FieldType.EMAIL_FIELD) != ""
            },
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.email),
                    color = PurpleGrey40
                )
            },
            isError = isEmailError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(6.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Display error message if field is empty
        if (isEmailError) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = email.text.checkError(FieldType.EMAIL_FIELD),
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username Field
        TextField(
            value = userName,
            onValueChange = { value ->
                userName = value
                isUserNameError = value.text.checkError(FieldType.NAME_FIELD) != ""
            },
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.username),
                    color = PurpleGrey40
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(6.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Display error message if field is empty
        if (isUserNameError) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = email.text.checkError(FieldType.NAME_FIELD),
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field
        TextField(
            value = password,
            onValueChange = { value ->
                password = value
                isPasswordError = password.text.checkError(FieldType.PASSWORD_FIELD) != ""
            },
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.password),
                    color = PurpleGrey40
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(6.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Face
                else Icons.Outlined.Face

                val description = if (passwordVisibility) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, description)
                }
            }
        )

        // Display error message if field is empty
        if (isPasswordError) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = password.text.checkError(FieldType.PASSWORD_FIELD),
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        TextField(
            value = confirmPassword,
            onValueChange = { value ->
                confirmPassword = value
                isConfirmPassError = confirmPassword.text.checkError(
                    FieldType.CONFIRM_PASSWORD_FIELD, password.text
                ) != ""
            },
            placeholder = {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(id = R.string.confirm_password),
                    color = PurpleGrey40
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(6.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisibility)
                    Icons.Filled.Face
                else Icons.Outlined.Face

                val description =
                    if (confirmPasswordVisibility) "Hide password" else "Show password"

                IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                    Icon(imageVector = image, description)
                }
            }
        )

        // Display error message if field is empty
        if (isConfirmPassError) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = confirmPassword.text.checkError(FieldType.CONFIRM_PASSWORD_FIELD),
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                authViewModel.performSignup(
                    email.text.trim(),
                    password.text.trim(),
                    userName.text.trim()
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Purple40),
            shape = RoundedCornerShape(10.dp),
            enabled = !(isEmailError || isConfirmPassError || isUserNameError || isConfirmPassError)
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.signup),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val existingAccountMsgText = stringResource(id = R.string.existing_account_message)

        val annotatedText = buildAnnotatedString {
            append(existingAccountMsgText)
            pushStringAnnotation(tag = "login", annotation = stringResource(id = R.string.login))
            withStyle(
                style = SpanStyle(
                    color = Purple40, fontWeight = FontWeight.Bold
                )
            ) {
                append(stringResource(id = R.string.login))
            }
            pop()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = annotatedText,
                modifier = Modifier.clickable {
                    annotatedText.getStringAnnotations(
                        tag = "login",
                        start = existingAccountMsgText.length,
                        end = annotatedText.length
                    ).firstOrNull()?.let { _ ->
                        navController.navigate(route = "login_screen")
                    }
                },
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = PurpleGrey40,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    SignupScreen(rememberNavController())
}