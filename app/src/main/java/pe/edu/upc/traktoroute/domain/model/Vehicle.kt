package pe.edu.upc.traktoroute.domain.model

data class Vehicle(
    val plate: String,
    val model: String,
    val driverName: String,
    val available: Boolean,
)
