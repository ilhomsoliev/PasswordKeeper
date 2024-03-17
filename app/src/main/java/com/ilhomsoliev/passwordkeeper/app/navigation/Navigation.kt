package com.ilhomsoliev.passwordkeeper.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ilhomsoliev.passwordkeeper.feature.add.presentation.AddPasswordScreen
import com.ilhomsoliev.passwordkeeper.feature.home.presentation.HomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(snackbarHostState: SnackbarHostState) {

    val navController: NavHostController = rememberNavController()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
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
}