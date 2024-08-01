package uk.ac.tees.mad.D3445103.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.tees.mad.D3445103.models.local.PlantsData

@Database(entities = [PlantsData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PlantsDatabase : RoomDatabase() {
    abstract fun customDataDao(): PlantsDao
}