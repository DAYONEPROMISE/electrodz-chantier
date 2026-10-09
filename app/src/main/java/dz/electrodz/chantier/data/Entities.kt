package dz.electrodz.chantier.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String = "",
    val wilaya: String = "",
    val address: String = "",
    val notes: String = ""
)

@Entity(tableName = "calculations")
data class Calculation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ts: Long = System.currentTimeMillis(),
    val powerW: Double,
    val threePhase: Boolean,
    val cosPhi: Double,
    val lengthM: Double,
    val material: String,
    val insulation: String,
    val usage: String,
    val currentA: Double,
    val sectionMm2: Double,
    val dropPct: Double,
    val breakerA: Int
)

@Entity(tableName = "fiches")
data class Fiche(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long = 0,
    val clientName: String = "",
    val date: String = "",
    val siteAddress: String = "",
    val wilaya: String = "",
    val description: String = "",
    val tasksDone: String = "",
    val calcId: Long = 0
)

@Dao
interface ClientDao {
    @Query("SELECT * FROM clients ORDER BY name") fun all(): Flow<List<Client>>
    @Insert suspend fun insert(c: Client): Long
    @Update suspend fun update(c: Client)
    @Delete suspend fun delete(c: Client)
}

@Dao
interface CalcDao {
    @Query("SELECT * FROM calculations ORDER BY ts DESC") fun all(): Flow<List<Calculation>>
    @Insert suspend fun insert(c: Calculation): Long
    @Query("SELECT * FROM calculations WHERE id = :id") suspend fun byId(id: Long): Calculation?
    @Query("DELETE FROM calculations WHERE id = :id") suspend fun deleteById(id: Long)
}

@Dao
interface FicheDao {
    @Query("SELECT * FROM fiches ORDER BY id DESC") fun all(): Flow<List<Fiche>>
    @Insert suspend fun insert(f: Fiche): Long
    @Update suspend fun update(f: Fiche)
    @Delete suspend fun delete(f: Fiche)
}

@Database(entities = [Client::class, Calculation::class, Fiche::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
    abstract fun clients(): ClientDao
    abstract fun calcs(): CalcDao
    abstract fun fiches(): FicheDao
}
