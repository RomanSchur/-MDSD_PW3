package com.example.pw3_3.composables
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.pw3_3.basic.AuthApi
import com.example.pw3_3.basic.AuthViewModel
import com.example.pw3_3.basic.LoginRequest

@Composable
fun BasicApp(authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val mockServerInterceptor = Interceptor { chain ->
        val request = chain.request()
        val path = request.url().encodedPath()
        if (path.contains("login")) {
            val responseJson = "{\"token\": \"fake-jwt-token-variant-23-reactive-power\", \"role\": \"ADMIN\"}"

            val mediaType = okhttp3.MediaType.parse("application/json")
            val responseBody = okhttp3.ResponseBody.create(mediaType, responseJson)

            Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(responseBody)
                .addHeader("content-type", "application/json")
                .build()
        } else {
            chain.proceed(request)
        }
    }
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(mockServerInterceptor)
        .build()

    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("http://localhost/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val authApi = retrofit.create(AuthApi::class.java)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Електричні мережі з компенсацією реактивної потужності",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = authApi.login(LoginRequest(username, password))
                            authViewModel.saveToken(response.token, response.role)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Login successful! Role: ${response.role}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Login failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            authViewModel.token?.let {
                Text("Saved token: $it", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
