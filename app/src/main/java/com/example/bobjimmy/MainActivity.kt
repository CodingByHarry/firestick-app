package com.example.bobjimmy

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.example.bobjimmy.ui.theme.BobJimmyTheme
import com.example.bobjimmy.ui.theme.functions.fetchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BobJimmyTheme {
                val navController = rememberNavController()
                var latestVersion by remember { mutableStateOf<Int?>(null) }
                var latestVersionUrl by remember { mutableStateOf<String>("") }
                var responseData by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(true) }
                val isDialogOpen = remember { mutableStateOf(false) }

                when {
                    isDialogOpen.value -> {
                        AlertDialogBox(
                            onDismissRequest = { isDialogOpen.value = false },
                            onConfirmation = {
                                isDialogOpen.value = false
                                println("Dialog Confirmed $latestVersionUrl")

                                DoTheThing(
                                    this,
                                    latestVersionUrl
                                )

                                             },
                            dialogTitle = "Update Available",
                            dialogText = "There is an update available for this app. Update now to get the latest version."
                        )
                    }
                }

                NavHost(navController = navController, startDestination = "login") {
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("login") {
                        LoginScreen(navController)
                    }
                }

                // Check for update
                val apiUrl = "https://bobjimmy.xyz/updates.php"

                // Launch the coroutine to make the network request
                LaunchedEffect(Unit) {
                    try {
                        val response = fetchData(apiUrl)
                        val jsonResponse = JSONObject(response)
                        latestVersion = jsonResponse.getInt("version")
                        latestVersionUrl = jsonResponse.getString("url")

                        responseData = "Response: $response"
                    } catch (e: Exception) {
                        responseData = "Error fetching data: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }

                // Display the result or loading indicator
                if (isLoading) {
                    Text("Loading...")
                } else {
                    responseData?.let {
                        Text(it)
                    }
                }

                val currentBuild = getVersionCode().toInt()
                println("VERSION: $latestVersion")
                println("URL: $latestVersionUrl")
                println("CURRENT VERSION: $currentBuild")

                // If found and update needed, popup and ask user if they want to update
                if(currentBuild != latestVersion) {
                    println("UPDATED REQUIRED!")
                    isDialogOpen.value = true
                } else {
                    println("UP-TO-DATE")
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
fun DoTheThing(activity: ComponentActivity, url: String) {
    GlobalScope.launch(Dispatchers.IO) {
        downloadAndInstallAPK(activity, url)
    }
}

suspend fun downloadAndInstallAPK(activity: ComponentActivity, apkUrl: String) {
    try {
        val url = URL(apkUrl)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream: InputStream = connection.inputStream
            val apkFile = saveApkLocally(activity, inputStream)
            println("APP DOWNLOADED")

            GlobalScope.launch(Dispatchers.Main) {
                installAPK(activity, apkFile)
            }
        } else {
            Log.e("DownloadAPK", "Error downloading APK: ${connection.responseCode}")
        }

        connection.disconnect()
    } catch (e: Exception) {
        Log.e("DownloadAPK", "Error downloading APK: ${e.message}")
    }
}

suspend fun saveApkLocally(activity: ComponentActivity, inputStream: InputStream): File? {
    try {
        // Check if external storage is available
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val externalFilesDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val apkFile = File(externalFilesDir, "app.apk")

            val outputStream = FileOutputStream(apkFile)

            try {
                val buffer = ByteArray(4096)
                var len: Int

                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                println("COMPLETED SAVE?")
            } finally {
                println("CLOSED STREAMS")
                inputStream.close()
                outputStream.close()
            }

            return apkFile
        } else {
            Log.e("SaveAPKLocally", "External storage is not available.")
        }
    } catch (e: Exception) {
        Log.e("SaveAPKLocally", "Error saving APK locally: ${e.message}")
    }

    return null
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTvMaterial3Api::class)
@Composable
fun AlertDialogBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    ) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        }
    )
}

@Composable
fun getVersionCode(): Int {
    val context = LocalContext.current
    return try {
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        // Handle the exception as needed
        -1
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    BobJimmyTheme {
        //LoginScreen()
    }
}
