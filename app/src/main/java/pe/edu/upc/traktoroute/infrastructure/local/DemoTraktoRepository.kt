package pe.edu.upc.traktoroute.infrastructure.local

import pe.edu.upc.traktoroute.domain.model.Trip
import pe.edu.upc.traktoroute.domain.model.TripStatus
import pe.edu.upc.traktoroute.domain.model.Vehicle
import pe.edu.upc.traktoroute.domain.repository.TraktoRepository

/** Temporary TB1 data source. Replace this adapter with the REST implementation. */
class DemoTraktoRepository : TraktoRepository {
    override fun getTrips() = listOf(
        Trip("TR-2048", "Lima", "Arequipa", "Carlos Mendoza", "T8K-421", 0.68f, TripStatus.IN_TRANSIT, "Hoy, 18:40"),
        Trip("TR-2049", "Callao", "Trujillo", "María Torres", "B7P-903", 0.34f, TripStatus.DELAYED, "Mañana, 07:15"),
        Trip("TR-2050", "Lima", "Ica", "Luis Ramos", "A4N-118", 0.0f, TripStatus.SCHEDULED, "Mañana, 11:30"),
    )

    override fun getVehicles() = listOf(
        Vehicle("T8K-421", "Volvo FH 460", "Carlos Mendoza", false),
        Vehicle("B7P-903", "Scania R450", "María Torres", false),
        Vehicle("A4N-118", "Freightliner M2", "Luis Ramos", true),
        Vehicle("C9Q-552", "Mercedes Actros", "Sin asignar", true),
    )
}
