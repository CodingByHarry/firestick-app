package com.example.bobjimmy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bobjimmy.ui.theme.BobJimmyTheme
import com.example.bobjimmy.ui.theme.components.RegularText
import com.example.bobjimmy.ui.theme.PreferenceManager

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val user = remember { preferenceManager.getUser().username }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegularText(text = "Reporting as user: $user")
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("tv_issue")
        }, modifier = Modifier.width(200.dp)) {
            Text("TV Issue")
        }
        Button(onClick = {
            navController.navigate("plex_issue")
        }, modifier = Modifier.width(200.dp)) {
            Text("Plex Issue")
        }
        Button(onClick = {
            navController.navigate("other_issue")
        }, modifier = Modifier.width(200.dp)) {
            Text("Other")
        }

        RegularText(
            text = "My issues",
            modifier = Modifier
                .padding(8.dp)
        )
        val itemList: List<String> = List(20) { index -> "Report Type $index" }

        // Display the list using LazyColumn
        LazyColumn {
            items(itemList) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)
                            .padding(8.dp)
                    ) {
                        Row {
                            Text(
                                text = item,
                                modifier = Modifier
                                    .background(Color.Gray)
                                    .padding(8.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(top = 8.dp, end = 8.dp)
                            )
                            Text(
                                text = "Report response comment",
                                modifier = Modifier
                                    .background(Color.Gray)
                                    .padding(8.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BobJimmyTheme {
        //HomeScreen()
    }
}
