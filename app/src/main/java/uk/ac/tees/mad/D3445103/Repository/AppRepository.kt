package uk.ac.tees.mad.D3445103.Repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.D3445103.data.local.PlantsDao
import uk.ac.tees.mad.D3445103.data.remote.FetchPlantsApi
import uk.ac.tees.mad.D3445103.models.local.PlantsData
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiService: FetchPlantsApi,
    private val plantsDao : PlantsDao
) {
    suspend fun getPlants(key: String, page: Int) = apiService.getPlants(key,page)

    fun getAllFromDB(): Flow<List<PlantsData>> {
        return plantsDao.getAllPlants()
    }

    suspend fun insertIntoDB (plantsData: PlantsData){
        plantsDao.insertPlant(plantsData)
    }

    fun deleteFromDB(plantsData: PlantsData){
        plantsDao.delete(plantsData)
    }
}