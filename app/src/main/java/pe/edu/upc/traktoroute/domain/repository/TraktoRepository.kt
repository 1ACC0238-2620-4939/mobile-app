package pe.edu.upc.traktoroute.domain.repository

import pe.edu.upc.traktoroute.domain.model.Trip
import pe.edu.upc.traktoroute.domain.model.Vehicle

interface TraktoRepository {
    fun getTrips(): List<Trip>
    fun getVehicles(): List<Vehicle>
}
