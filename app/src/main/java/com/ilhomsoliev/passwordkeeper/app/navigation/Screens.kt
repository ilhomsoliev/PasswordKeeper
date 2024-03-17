package com.ilhomsoliev.passwordkeeper.app.navigation

import androidx.navigation.NavBackStackEntry

sealed class Screens(val route: String) {

    data object Home : Screens("Home")
    data object AddPassword : Screens("Add/{item_id}") {
        fun buildRoute(id: Int?): String = "Add/${id}"
        fun getId(entry: NavBackStackEntry): Int? =
            run {
                try {
                    entry.arguments?.getString("item_id")?.toInt()
                } catch (e: Exception) {
                    null
                }
            }
    }
}