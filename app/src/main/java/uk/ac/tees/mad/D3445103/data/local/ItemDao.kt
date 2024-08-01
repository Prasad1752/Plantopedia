package uk.ac.tees.mad.D3445103.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uk.ac.tees.mad.D3445103.models.local.PlantsData
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantsDao {

    @Query("Select * from plants")
    fun getAllPlants() : Flow<List<PlantsData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlant(plant: PlantsData)

    @Delete
    fun delete (plant: PlantsData)
}