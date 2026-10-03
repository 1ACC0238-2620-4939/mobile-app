package pe.edu.upc.traktoroute.application

import pe.edu.upc.traktoroute.domain.model.TripStatus
import pe.edu.upc.traktoroute.domain.repository.TraktoRepository

class TraktoFacade(private val repository: TraktoRepository) {
    val trips get() = repository.getTrips()
    val vehicles get() = repository.getVehicles()
    val activeTrips get() = trips.filter { it.status == TripStatus.IN_TRANSIT || it.status == TripStatus.DELAYED }
    val availableVehicles get() = vehicles.count { it.available }
}
