package com.example.pw3_1.composables

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pw3_1.advanced.CompensationNode
import com.example.pw3_1.advanced.CompensationViewModel

@Composable
fun AdvancedApp(viewModel: CompensationViewModel = viewModel()) {
    val nodes by viewModel.nodes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var sortDesc by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }
    var voltageInput by remember { mutableStateOf("10.0") }
    var powerInput by remember { mutableStateOf("") }
    var stepsInput by remember { mutableStateOf("4") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Просунутий рівень: КРП (Варіант 23)",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.filterByName(it)
                },
                label = { Text("Фільтр по назві ПС") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                sortDesc = !sortDesc
                viewModel.sortByPower(sortDesc)
            }) {
                Text(if (sortDesc) "Сорт 📉" else "Сорт 📈")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Назва підстанції (напр. ПС Західна)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = powerInput,
                onValueChange = { powerInput = it },
                label = { Text("Реактивна Q (квар)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = stepsInput,
                onValueChange = { stepsInput = it },
                label = { Text("Ступені БСК") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                val q = powerInput.toDoubleOrNull()
                val steps = stepsInput.toIntOrNull()
                if (nameInput.isNotBlank() && q != null && steps != null) {
                    val newNode = CompensationNode(
                        id = nodes.size + 1,
                        substationName = nameInput,
                        voltageKv = 10.0,
                        reactivePowerQ = q,
                        activeSteps = steps
                    )
                    viewModel.addNode(newNode)
                    nameInput = ""
                    powerInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Додати вузол компенсації")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.loadNodes(forceRefresh = true) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Оновити з сервера (Force Refresh)")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(nodes) { node ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = node.substationName, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Напруга: ${node.voltageKv} кВ | Q: ${node.reactivePowerQ} квар")
                            Text(text = "Активні ступені БСК: ${node.activeSteps}")
                        }
                        IconButton(onClick = { viewModel.deleteNode(node.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Видалити")
                        }
                    }
                }
            }
        }
    }
}