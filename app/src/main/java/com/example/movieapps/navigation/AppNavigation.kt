package com.example.movieapps.navigation

import MovieDetailsScreen
import MovieListScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapps.ui.screen.LoginScreen
import com.example.movieapps.ui.screen.OnboardingScreen
import com.example.movieapps.ui.screen.SignupScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding_screen") {
        // Onboarding Screen
        composable("onboarding_screen") {
            OnboardingScreen(navController)
        }

        // Login Screen
        composable("login_screen") {
            LoginScreen(navController)
        }

        // Sign Up Screen
        composable("signup_screen") {
            SignupScreen(navController)
        }

        // Movie List Screen
        composable("movieList_screen") {
            MovieListScreen(navController)
        }

        // Movie Details Screen
        composable("movieDetail_screen/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            MovieDetailsScreen(navController, movieId)
        }

    }
}