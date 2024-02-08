package com.example.bobjimmy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bobjimmy.ui.theme.BobJimmyTheme
import com.example.bobjimmy.ui.theme.PreferenceManager
import com.example.bobjimmy.ui.theme.User
import com.example.bobjimmy.ui.theme.components.HeaderText
import com.example.bobjimmy.ui.theme.components.SmallText
import com.example.bobjimmy.ui.theme.components.TextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun LoginScreen(navController: NavHostController) {
    val (userName, setUsername) = rememberSaveable {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }
    val user = remember { preferenceManager.getUser().username }

    // TODO: This should also check that the user is real again
    if (!user.isNullOrEmpty()) {
        navController.navigate("home")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(text = "Login to report")
        TextField(
            value = userName,
            onValueChange = setUsername,
            labelText = "Please enter your username",
            leadingIcon = Icons.Filled.Person
        )
        Button(onClick = {
            // TODO: Extract this out to a checkUser function that just returns true or false
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val baseUrl = "https://bobjimmy.xyz/auth.php" // Replace with your API endpoint
                    val variableUrl = "$baseUrl?username=$userName"

                    val url = URL(variableUrl)
                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                    println(url)
                    println("CURRENT USER ON DEVICE: $user")

                    // Set the request method to GET
                    connection.requestMethod = "GET"

                    // Get the response code
                    val responseCode: Int = connection.responseCode

                    // Read the response
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val reader = BufferedReader(InputStreamReader(connection.inputStream))
                        val response = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            response.append(line)
                        }
                        reader.close()

                        // Handle the response as needed
                        println("Response: $response")

                        val jsonResponse = JSONObject(response.toString())
                        val status = jsonResponse.getString("status")

                        if (status == "success") {
                            println("Request successful!")

                            //Save the user to PreferenceManager
                            // TODO: Once this is extracted to a checkUser func move this
                            val updatedUserDetails = User(userName)
                            preferenceManager.saveUser(updatedUserDetails)
                        } else {
                            println("Request failed. Status: $status")
                        }

                    } else {
                        // Handle the error case
                        println("Error: ${connection.responseMessage}")
                    }

                    // Disconnect the connection
                    connection.disconnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, modifier = Modifier.width(200.dp)) {
            Text("Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    BobJimmyTheme {
        //LoginScreen()
    }
}