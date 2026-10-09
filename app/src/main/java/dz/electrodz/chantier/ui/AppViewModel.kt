package dz.electrodz.chantier.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dz.electrodz.chantier.data.AppDb
import dz.electrodz.chantier.data.Calculation
import dz.electrodz.chantier.data.Client
import dz.electrodz.chantier.data.Fiche
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val db: AppDb = Room.databaseBuilder(app, AppDb::class.java, "electrodz.db")
        .fallbackToDestructiveMigration()
        .build()

    val clients = db.clients().all()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val calcs = db.calcs().all()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val fiches = db.fiches().all()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveCalc(c: Calculation, onDone: (Long) -> Unit = {}) = viewModelScope.launch {
        try {
            val id = db.calcs().insert(c)
            onDone(id)
        } catch (_: Exception) { }
    }

    fun deleteCalc(id: Long) = viewModelScope.launch {
        try { db.calcs().deleteById(id) } catch (_: Exception) { }
    }

    fun addClient(c: Client) = viewModelScope.launch {
        try { db.clients().insert(c) } catch (_: Exception) { }
    }

    fun deleteClient(c: Client) = viewModelScope.launch {
        try { db.clients().delete(c) } catch (_: Exception) { }
    }

    fun addFiche(f: Fiche) = viewModelScope.launch {
        try { db.fiches().insert(f) } catch (_: Exception) { }
    }

    fun deleteFiche(f: Fiche) = viewModelScope.launch {
        try { db.fiches().delete(f) } catch (_: Exception) { }
    }

    suspend fun calcById(id: Long): Calculation? = try {
        db.calcs().byId(id)
    } catch (_: Exception) { null }
}
