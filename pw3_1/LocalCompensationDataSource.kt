package com.example.pw3_1.advanced
class LocalCompensationDataSource {
    private val data = mutableListOf(
        CompensationNode(1, "ПС Центральна", 10.0, 1250.5, 4),
        CompensationNode(2, "ПС Північна", 10.0, 850.0, 3)
    )

    fun getAll(): List<CompensationNode> = data.toList()

    fun insert(item: CompensationNode) {
        data.add(item)
    }

    fun update(item: CompensationNode) {
        data.replaceAll { if (it.id == item.id) item else it }
    }

    fun delete(id: Int) {
        data.removeIf { it.id == id }
    }

    fun clear() {
        data.clear()
    }

    fun filterByName(query: String): List<CompensationNode> =
        data.filter { it.substationName.contains(query, ignoreCase = true) }

    fun sortByPower(descending: Boolean = false): List<CompensationNode> =
        if (descending) data.sortedByDescending { it.reactivePowerQ }
        else data.sortedBy { it.reactivePowerQ }
}