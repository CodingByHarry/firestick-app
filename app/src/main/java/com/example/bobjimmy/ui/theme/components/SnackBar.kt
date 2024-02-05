package com.example.bobjimmy.ui.theme.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bobjimmy.ui.theme.BobJimmyTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter") // TODO: REVIEW ME
@Composable
fun SnackBar(

) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost =  { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Show Snackbar")},
                icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    scope.launch {
                        var result = snackbarHostState.showSnackbar(
                            message = "Show"
                        )
                    }
                },
            )
        }
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun SnackBarPreview() {
    BobJimmyTheme {
        SnackBar()
    }
}
