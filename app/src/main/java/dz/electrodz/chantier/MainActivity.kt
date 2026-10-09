package dz.electrodz.chantier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dz.electrodz.chantier.ui.AppViewModel
import dz.electrodz.chantier.ui.CalculatorScreen
import dz.electrodz.chantier.ui.ClientsScreen
import dz.electrodz.chantier.ui.FichesScreen
import dz.electrodz.chantier.ui.HistoryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface { App() }
            }
        }
    }
}

private data class Tab(val route: String, val label: String)

@Composable
private fun App(vm: AppViewModel = viewModel()) {
    val nav = rememberNavController()
    val tabs = listOf(
        Tab("calc", "Calcul"), Tab("fiches", "Fiches"),
        Tab("clients", "Clients"), Tab("history", "Historique")
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                val back by nav.currentBackStackEntryAsState()
                val cur = back?.destination?.route
                tabs.forEach { t ->
                    val icon = when (t.route) {
                        "calc" -> Icons.Filled.Build
                        "fiches" -> Icons.Filled.List
                        "clients" -> Icons.Filled.Person
                        else -> Icons.Filled.History
                    }
                    NavigationBarItem(
                        selected = cur == t.route,
                        onClick = { nav.navigate(t.route) { launchSingleTop = true } },
                        icon = { Icon(icon, null) },
                        label = { Text(t.label) }
                    )
                }
            }
        }
    ) { pad ->
        NavHost(nav, startDestination = "calc", modifier = Modifier.padding(pad)) {
            composable("calc") { CalculatorScreen(vm) }
            composable("fiches") { FichesScreen(vm) }
            composable("clients") { ClientsScreen(vm) }
            composable("history") { HistoryScreen(vm) }
        }
    }
}
