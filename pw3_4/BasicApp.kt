package com.example.pw3_4.composables
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.pw3_4.basic.RetrofitClient
import com.example.pw3_4.basic.ReactivePowerData
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicApp() {
    val context = LocalContext.current
    var networkData by remember { mutableStateOf<ReactivePowerData?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Електричні мережі: Реактивна потужність") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    // 1. Перевірка наявності підключення до Інтернету
                    if (!isInternetAvailable(context)) {
                        snackbarHostState.showSnackbar("Немає підключення до Інтернету")
                        return@launch
                    }

                    try {
                        val response = RetrofitClient.api.getReactivePowerData()
                        withContext(Dispatchers.Main) {
                            networkData = response
                        }
                    } catch (e: Exception) {
                        val errorMsg = if (e.message?.contains("failed to connect") == true || e is java.net.ConnectException) {
                            "Сервер недоступний (відсутній за адресою 10.0.2.2:5156)"
                        } else {
                            "Помилка завантаження: ${e.localizedMessage}"
                        }
                        snackbarHostState.showSnackbar(errorMsg)
                    }
                }
            }) {
                Text("Отримати параметри мережі")
            }
            Spacer(modifier = Modifier.height(20.dp))
            networkData?.let { data ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📅 Дата: ${data.date}", style = MaterialTheme.typography.bodyLarge)
                        Text("⚡ Реактивна потужність: ${data.reactivePowerKvar} квар", style = MaterialTheme.typography.bodyLarge)
                        Text("📊 Коефіцієнт потужності (cos φ): ${data.powerFactor}", style = MaterialTheme.typography.bodyLarge)
                        Text("⚙️ Статус компенсації: ${data.status}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}