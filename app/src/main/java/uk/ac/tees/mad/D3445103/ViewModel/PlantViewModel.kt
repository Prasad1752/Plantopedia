package uk.ac.tees.mad.D3445103.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.D3445103.Repository.AppRepository
import uk.ac.tees.mad.D3445103.models.local.PlantsData
import uk.ac.tees.mad.D3445103.models.remote.Data
import uk.ac.tees.mad.D3445103.models.remote.Plants
import uk.ac.tees.mad.D3445103.models.remote.UserData
import uk.ac.tees.mad.D3445103.utils.APIKEY
import uk.ac.tees.mad.D3445103.utils.USER
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val repository: AppRepository,
) : ViewModel() {
    val isSignedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    private val _plantsInfo = mutableStateOf<Plants?>(null)
    val plantsInfo: State<Plants?> = _plantsInfo

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _plantsInfoDB = MutableStateFlow<List<PlantsData>>(emptyList())
    val plantsInfoDB: StateFlow<List<PlantsData>> get() = _plantsInfoDB

    val page = mutableStateOf(1)

    init {
        auth.currentUser?.let { user ->
            isSignedIn.value = true
            getUserData(user.uid)
        }
        fetchPlants()
        getEveryThingFromDB()
    }

    fun fetchPlants() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getPlants(APIKEY, 1)
                _plantsInfo.value = response
                Log.d("Plants", _plantsInfo.value.toString())
                isLoading.value = false
            } catch (e: Exception) {
                isLoading.value = false
                Log.d("Error Fetching Data", e.message.toString())
                _error.value = "Error Loading data: ${e.message}"
            }
        }
    }

    fun fetchNextPage(){
        isLoading.value = true
        if (page.value < _plantsInfo.value!!.last_page) {
            page.value++
            viewModelScope.launch {
                try {
                    val response = repository.getPlants(APIKEY, page.value)
                    _plantsInfo.value = response
                    Log.d("Plants", _plantsInfo.value.toString())
                    isLoading.value = false
                } catch (e: Exception) {
                    isLoading.value = false
                    _error.value = "Error Loading data: ${e.message}"
                }
            }
        }else{
            return
        }
    }

    fun fetchPreviousPage(){
        isLoading.value = true
        if(page.value > 1) {
            page.value--
            viewModelScope.launch {
                try {
                    val response = repository.getPlants(APIKEY, page.value)
                    _plantsInfo.value = response
                    Log.d("Plants", _plantsInfo.value.toString())
                    isLoading.value = false
                } catch (e: Exception) {
                    isLoading.value = false
                    _error.value = "Error Loading data: ${e.message}"
                }
            }
        }else{
            return
        }
    }

    fun signUp(context: Context, email: String, password: String) {
        isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { authResult ->
            authResult.user?.let { user ->
                getUserData(user.uid)
                createOrUpdateUser(context = context,uid =  user.uid, email =  email, password =  password )
            }
            isLoading.value = false
            isSignedIn.value = true
            Toast.makeText(context, "sign up successful", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            isLoading.value = false
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun logIn(context: Context, email: String, password: String) {
        isLoading.value = true
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            isLoading.value = false
            getUserData(it.user!!.uid)
            Toast.makeText(context, "log in successful", Toast.LENGTH_SHORT).show()
            isSignedIn.value = true
        }.addOnFailureListener {
            isLoading.value = false
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            val storageReference = storage.reference.child("images/${auth.currentUser?.uid}")
            val uploadTask = storageReference.putFile(imageUri)

            try {
                val taskSnapshot = uploadTask.await()
                val downloadUrl = taskSnapshot.storage.downloadUrl.await()
                val imageUrl = downloadUrl.toString()
                createOrUpdateUser(context = context, uid = auth.currentUser?.uid!!, imageUrl = imageUrl, name = userData.value?.name,
                    phone = userData.value?.phone,
                    email = userData.value?.email, password = userData.value?.password , location = userData.value?.location)
            } catch (e: Exception) {
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateEditUser(context: Context, name: String, phone : String, location: String){
        createOrUpdateUser(context = context, uid = auth.currentUser?.uid!!, imageUrl = userData.value?.imageUrl, name = name,
            phone = phone,
            email = userData.value?.email, password = userData.value?.password , location = location)
    }

    fun createOrUpdateUser(
        context: Context,
        uid: String,
        name: String? = "",
        imageUrl: String? = "",
        phone: String? = "",
        email: String? = "",
        password: String? = "",
        location: String? = ""
    ) {
        val user: UserData = UserData(
            id = uid,
            name = name!!,
            email = email!!,
            imageUrl = imageUrl!!,
            phone = phone!!,
            password = password!!,
            location = location!!
        )
        Log.d("USER",user.toString())
        db.collection(USER).document(uid).set(user).addOnSuccessListener {
            Toast.makeText(context, "data updated", Toast.LENGTH_SHORT).show()
            Log.d("Firestore", "data updated")
            getUserData(uid = uid)
        }.addOnFailureListener {
            Log.d("Firestore", it.localizedMessage)
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserData(uid: String) {
        db.collection(USER).document(uid).get().addOnSuccessListener {
            val user = it.toObject(UserData::class.java)
            userData.value = user
            Log.d("User", user.toString())
        }.addOnFailureListener {
            it.localizedMessage?.let { it1 -> Log.d("Error Loading Data", it1) }
        }
    }

    fun searchPlants(query: String) :  List<Data> {
        val searchQuery = query.lowercase()
        val response = _plantsInfo.value?.data?.filter {
            it.common_name.lowercase().contains(searchQuery)
        } ?: emptyList()
        return response
    }

    fun getEveryThingFromDB(){
        viewModelScope.launch {
            repository.getAllFromDB().collect { plantsList ->
                _plantsInfoDB.value = plantsList
                Log.d("Plants", plantsList.toString())
            }
        }
    }

    fun insertPlant(plant: Data) {
        val response = Data(
            common_name = plant.common_name,
            cycle = plant.cycle,
            default_image = plant.default_image,
            id = plant.id,
            other_name = plant.other_name,
            scientific_name = plant.scientific_name,
            sunlight = plant.sunlight,
            watering = plant.watering
        )
        val result = response.toPlantData()
        viewModelScope.launch {
            repository.insertIntoDB(result)
        }
    }

    private fun Data.toPlantData(): PlantsData{
        return PlantsData(
            common_name = this.common_name,
            scientific_name = this.scientific_name,
            cycle = this.cycle,
            default_image = this.default_image,
            id = this.id,
            other_name = this.other_name,
            sunlight = this.sunlight,
            watering = this.watering
        )
    }

    fun signOut() {
        auth.signOut()
        isSignedIn.value = false
        userData.value = null
    }

}