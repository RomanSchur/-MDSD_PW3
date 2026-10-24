package com.example.pw3_1.advanced
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
class CompensationViewModel : ViewModel() {
    private val repository = CompensationRepository(
        LocalCompensationDataSource(),
        RemoteCompensationDataSource()
    )
    private val _nodes = MutableStateFlow<List<CompensationNode>>(emptyList())
    val nodes: StateFlow<List<CompensationNode>> = _nodes.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)
    init {
        loadNodes()
    }
    fun loadNodes(forceRefresh: Boolean = false) {
        scope.launch {
            repository.getAll(forceRefresh).onSuccess {
                _nodes.value = it
            }.onFailure {
                println("Error loading nodes: ${it.message}")
            }
        }
    }
    fun addNode(node: CompensationNode) {
        scope.launch {
            repository.add(node).onSuccess {
                loadNodes()
            }
        }
    }
    fun updateNode(node: CompensationNode) {
        scope.launch {
            repository.update(node).onSuccess {
                loadNodes()
            }
        }
    }
    fun deleteNode(id: Int) {
        scope.launch {
            repository.delete(id).onSuccess {
                loadNodes()
            }
        }
    }
    fun filterByName(query: String) {
        _nodes.value = repository.filterByName(query)
    }
    fun sortByPower(descending: Boolean = false) {
        _nodes.value = repository.sortByPower(descending)
    }
}