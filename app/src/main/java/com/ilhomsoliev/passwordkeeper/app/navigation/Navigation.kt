package com.ilhomsoliev.passwordkeeper.app.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilhomsoliev.passwordkeeper.feature.add.presentation.AddPasswordScreen
import com.ilhomsoliev.passwordkeeper.feature.home.presentation.HomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            HomeScreen(vm = koinViewModel(),
                openAddPasswordScreen = {
                    navController.navigate(Screens.AddPassword.buildRoute(it))
                }
            )
        }
        composable(route = Screens.AddPassword.route) {
            val id = Screens.AddPassword.getId(it)
            AddPasswordScreen(vm = koinViewModel(), id = id, onBack = {
                navController.popBackStack()
            })
        }
    }
}