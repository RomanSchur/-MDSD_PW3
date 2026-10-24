package com.example.pw3_2.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CompensationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CompensationRepository
    val nodes: StateFlow<List<CompensationEntity>>

    init {
        val dao = CompensationDatabase.getDatabase(application).compensationDao()
        repository = CompensationRepository(dao)
        nodes = repository.allNodes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun addNode(substationName: String, voltageKv: Double, reactivePowerQ: Double, activeSteps: Int) {
        viewModelScope.launch {
            repository.insert(
                CompensationEntity(
                    substationName = substationName,
                    voltageKv = voltageKv,
                    reactivePowerQ = reactivePowerQ,
                    activeSteps = activeSteps
                )
            )
        }
    }

    fun deleteNode(node: CompensationEntity) {
        viewModelScope.launch {
            repository.delete(node)
        }
    }
}