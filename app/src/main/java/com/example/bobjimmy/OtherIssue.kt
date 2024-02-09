package com.example.bobjimmy

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun OtherIssue(navController: NavHostController) {
    Button(onClick = {
        navController.navigate("home")
    }, modifier = Modifier.width(200.dp)) {
        Text("Home")
    }
}