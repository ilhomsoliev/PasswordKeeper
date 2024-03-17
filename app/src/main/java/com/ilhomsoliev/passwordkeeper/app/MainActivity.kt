package com.ilhomsoliev.passwordkeeper.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.FragmentActivity
import com.ilhomsoliev.passwordkeeper.app.navigation.Navigation
import com.ilhomsoliev.passwordkeeper.feature.shared.snackbar.ProvideSnackbarController


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()
            ProvideSnackbarController(
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            ) {
                Navigation(
                    snackbarHostState = snackbarHostState,

                )
            }
        }
    }
}