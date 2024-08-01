package uk.ac.tees.mad.D3445103.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.D3445103.models.remote.Plants

interface FetchPlantsApi {
    @GET("api/species-list")
    suspend fun getPlants(@Query("key") key : String,
                          @Query("page") page: Int )
    : Plants
}