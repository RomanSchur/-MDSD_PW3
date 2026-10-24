package com.example.pw3_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pw3_2.room.CompensationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompensationScreen()
                }
            }
        }
    }
}
@Composable
fun CompensationScreen(viewModel: CompensationViewModel = viewModel()) {
    val nodes by viewModel.nodes.collectAsState()

    var substationName by remember { mutableStateOf("") }
    var voltageKv by remember { mutableStateOf("10.0") }
    var reactivePowerQ by remember { mutableStateOf("") }
    var activeSteps by remember { mutableStateOf("6") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Компенсація реактивної потужності",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )


        OutlinedTextField(
            value = substationName,
            onValueChange = { substationName = it },
            label = { Text("Назва підстанції") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = voltageKv,
                onValueChange = { voltageKv = it },
                label = { Text("Напруга (кВ)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = reactivePowerQ,
                onValueChange = { reactivePowerQ = it },
                label = { Text("Потужність Q") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = activeSteps,
            onValueChange = { activeSteps = it },
            label = { Text("Активні ступені БСК") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Кнопка додавання запису в базу даних
        Button(
            onClick = {
                val voltage = voltageKv.toDoubleOrNull() ?: 0.0
                val power = reactivePowerQ.toDoubleOrNull() ?: 0.0
                val steps = activeSteps.toIntOrNull() ?: 0
                if (substationName.isNotBlank()) {
                    viewModel.addNode(substationName, voltage, power, steps)
                    substationName = ""
                    reactivePowerQ = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Додати вузол")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Виведення списку збережених вузлів через LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(nodes) { node ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = node.substationName, style = MaterialTheme.typography.titleSmall)
                            Text(text = "Напруга: ${node.voltageKv} кВ | Q: ${node.reactivePowerQ}")
                            Text(text = "Ступені БСК: ${node.activeSteps}")
                        }
                        IconButton(onClick = { viewModel.deleteNode(node) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Видалити")
                        }
                    }
                }
            }
        }
    }
}