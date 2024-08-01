package uk.ac.tees.mad.D3445103.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.D3445103.models.remote.DefaultImage

@Entity(tableName = "plants")
data class PlantsData(
    @PrimaryKey(autoGenerate = true)val primaryId : Int = 0,
    val common_name: String,
    val cycle: String,
    val default_image: DefaultImage?,
    val id: Int,
    val other_name: List<String>,
    val scientific_name: List<String>,
    val sunlight: List<String>,
    val watering: String,
)