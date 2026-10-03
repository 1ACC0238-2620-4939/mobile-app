package pe.edu.upc.traktoroute.domain.model

data class Trip(
    val id: String,
    val origin: String,
    val destination: String,
    val driverName: String,
    val vehiclePlate: String,
    val progress: Float,
    val status: TripStatus,
    val eta: String,
)

enum class TripStatus(val label: String) {
    SCHEDULED("Programado"),
    IN_TRANSIT("En ruta"),
    DELAYED("Con demora"),
    COMPLETED("Completado"),
}
