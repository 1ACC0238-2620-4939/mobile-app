package pe.edu.upc.traktoroute

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.traktoroute.application.TraktoFacade
import pe.edu.upc.traktoroute.domain.model.Trip
import pe.edu.upc.traktoroute.domain.model.TripStatus
import pe.edu.upc.traktoroute.domain.model.Vehicle
import pe.edu.upc.traktoroute.infrastructure.local.DemoTraktoRepository
import pe.edu.upc.traktoroute.presentation.theme.TraktoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraktoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TraktoApp(remember { TraktoFacade(DemoTraktoRepository()) })
                }
            }
        }
    }
}

private enum class Section(val label: String, val marker: String) {
    HOME("Inicio", "I"), TRIPS("Viajes", "V"), TRACKING("Ruta", "R"), FLEET("Flota", "F")
}

@Composable
private fun TraktoApp(facade: TraktoFacade) {
    var section by remember { mutableStateOf(Section.HOME) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar {
                Section.entries.forEach { item ->
                    NavigationBarItem(
                        selected = section == item,
                        onClick = { section = item },
                        icon = { Marker(item.marker, section == item) },
                        label = { Text(item.label) },
                    )
                }
            }
        },
    ) { padding ->
        when (section) {
            Section.HOME -> Dashboard(facade, padding)
            Section.TRIPS -> TripsScreen(facade.trips, padding)
            Section.TRACKING -> TrackingScreen(facade.activeTrips.first(), padding)
            Section.FLEET -> FleetScreen(facade.vehicles, padding)
        }
    }
}

@Composable
private fun Marker(text: String, selected: Boolean) {
    Box(
        modifier = Modifier.size(26.dp).clip(CircleShape)
            .background(if (selected) MaterialTheme.colorScheme.primary else Color(0xFFE7EBF2)),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = if (selected) Color.White else Color(0xFF526071), fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Page(title: String, subtitle: String, padding: PaddingValues, content: @Composable () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text("TRAKTO ROUTE", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = Color(0xFF657084))
        }
        item { content() }
    }
}

@Composable
private fun Dashboard(facade: TraktoFacade, padding: PaddingValues) {
    Page("Panel de operaciones", "Buenas tardes. Este es el estado de tu flota.", padding) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard("Viajes activos", facade.activeTrips.size.toString(), "2 requieren seguimiento", Modifier.weight(1f))
                MetricCard("Vehículos libres", facade.availableVehicles.toString(), "Listos para asignar", Modifier.weight(1f))
            }
            SectionTitle("Operación prioritaria")
            TripCard(facade.activeTrips.first())
            SectionTitle("Alertas")
            InfoCard("Demora detectada", "TR-2049 reportó tráfico intenso cerca de Chimbote.", Color(0xFFFFF1D6))
        }
    }
}

@Composable
private fun TripsScreen(trips: List<Trip>, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text("TRAKTO ROUTE", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Viajes", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Programación y estado en un solo lugar", color = Color(0xFF657084))
        }
        items(trips) { TripCard(it) }
    }
}

@Composable
private fun TrackingScreen(trip: Trip, padding: PaddingValues) {
    Page("Seguimiento en vivo", "Viaje ${trip.id} · ${trip.origin} → ${trip.destination}", padding) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F1FF)), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("${(trip.progress * 100).toInt()}% completado", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(progress = { trip.progress }, modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape))
                    Text("Llegada estimada: ${trip.eta}", color = Color(0xFF526071))
                }
            }
            SectionTitle("Hitos de la ruta")
            TimelineItem("Salida de almacén", "Completado · 08:10", true)
            TimelineItem("Control de carretera", "Completado · 12:35", true)
            TimelineItem("Punto de descanso", "Próximo · 16:20", false)
            TimelineItem("Destino", trip.destination, false)
        }
    }
}

@Composable
private fun FleetScreen(vehicles: List<Vehicle>, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text("TRAKTO ROUTE", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("Flota", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Vehículos y conductores asignados", color = Color(0xFF657084))
        }
        items(vehicles) { VehicleCard(it) }
    }
}

@Composable
private fun MetricCard(label: String, value: String, detail: String, modifier: Modifier = Modifier) {
    Card(modifier, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text(label, color = Color(0xFF657084), fontSize = 13.sp)
            Text(value, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(detail, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun TripCard(trip: Trip) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(trip.id, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                StatusPill(trip.status)
            }
            Text("${trip.origin}  →  ${trip.destination}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            LinearProgressIndicator(progress = { trip.progress }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape))
            HorizontalDivider(color = Color(0xFFEDF0F4))
            Text("${trip.driverName} · ${trip.vehiclePlate}", color = Color(0xFF657084), fontSize = 13.sp)
            Text("ETA ${trip.eta}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun VehicleCard(vehicle: Vehicle) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(Modifier.fillMaxWidth().padding(18.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(vehicle.plate, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(vehicle.model, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                Text(vehicle.driverName, color = Color(0xFF657084), fontSize = 13.sp)
            }
            Text(
                if (vehicle.available) "Disponible" else "En viaje",
                color = if (vehicle.available) Color(0xFF067647) else Color(0xFF526071),
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun StatusPill(status: TripStatus) {
    val color = when (status) {
        TripStatus.IN_TRANSIT -> Color(0xFF067647)
        TripStatus.DELAYED -> Color(0xFFB54708)
        TripStatus.SCHEDULED -> Color(0xFF175CD3)
        TripStatus.COMPLETED -> Color(0xFF526071)
    }
    Text(status.label, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun TimelineItem(title: String, detail: String, complete: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(14.dp).clip(CircleShape).background(if (complete) Color(0xFF12B76A) else Color(0xFFD0D5DD)))
        Spacer(Modifier.size(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(detail, color = Color(0xFF657084), fontSize = 13.sp)
        }
    }
}

@Composable
private fun SectionTitle(text: String) = Text(text, fontSize = 18.sp, fontWeight = FontWeight.Bold)

@Composable
private fun InfoCard(title: String, detail: String, color: Color) {
    Card(colors = CardDefaults.cardColors(containerColor = color), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(detail, color = Color(0xFF526071), fontSize = 13.sp)
        }
    }
}
