package uk.ac.tees.mad.D3445103.DI.Module

import android.content.Context
import androidx.room.Room
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.D3445103.data.local.PlantsDao
import uk.ac.tees.mad.D3445103.data.local.PlantsDatabase
import uk.ac.tees.mad.D3445103.data.remote.FetchPlantsApi
import uk.ac.tees.mad.D3445103.utils.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PlantsDatabase {
        return Room.databaseBuilder(
            context,
            PlantsDatabase::class.java,
            "plants_database"
        ).build()
    }

    @Provides
    fun provideCustomDataDao(database: PlantsDatabase): PlantsDao {
        return database.customDataDao()
    }


    @Provides
    fun ProvidesAuthentication() : FirebaseAuth = Firebase.auth

    @Provides
    fun provideFireStore() : FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideStorage() : FirebaseStorage = Firebase.storage

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FetchPlantsApi {
        return retrofit.create(FetchPlantsApi::class.java)
    }
}