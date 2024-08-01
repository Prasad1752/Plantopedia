@file:OptIn(ExperimentalPermissionsApi::class)

package uk.ac.tees.mad.D3445103.Screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import uk.ac.tees.mad.D3445103.AppNavigationComponent
import uk.ac.tees.mad.D3445103.R
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel
import uk.ac.tees.mad.D3445103.ui.theme.poppins
import uk.ac.tees.mad.D3445103.utils.GreenColor
import uk.ac.tees.mad.D3445103.utils.NavigateWithoutBackStack
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@ExperimentalPermissionsApi
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileScreen(vm: PlantViewModel, navController: NavController) {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()){
            capturedImageUri = uri
            Log.d("Image Uri", uri.toString())
            Log.d("Image Uri", capturedImageUri.toString())
            vm.uploadImage(context, uri)
        }


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if (it)
        {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        }
        else
        {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)


    var user by remember { mutableStateOf(vm.userData.value) }
    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var location by remember { mutableStateOf(user?.location ?: "") }

    val coroutineScope = rememberCoroutineScope()

    fun getCurrentLocation() {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGpsEnabled || isNetworkEnabled) {
            Log.d("GPS", "GPS ENABLED")
            if (locationPermissionState.status == PermissionStatus.Granted) {
                Log.d("GPS", "GPS ENABLED2")
                fusedLocationClient.lastLocation.addOnSuccessListener { loc: Location? ->
                    Log.d("Location", "Location Found")
                    Log.d("Location", "${loc?.latitude}, ${loc?.longitude}")
                    loc?.let {
                        val locString = "Lat: ${it.latitude}, Lon: ${it.longitude}"
                        getAddressFromLocation(context = context, location = loc, onChange = {
                            location = it
                        })
                        Log.d("Location", locString)
                    }
                }
            } else {
                Log.d("GPS", "GPS NOT ENABLED 1")

                locationPermissionState.launchPermissionRequest()
            }
        } else {
            Log.d("GPS", "GPS NOT ENABLED")

            Toast.makeText(context, "Please turn on GPS", Toast.LENGTH_LONG).show()
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        bottomBar = {
            BottomNavigation(
                selectedItem = BottomNavigationItem.ProfileScreen,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack, contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp)
                    )
                    Text(
                        text = "Profile",
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 30.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (user!!.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = user?.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            placeholder = painterResource(
                                id = R.drawable.person
                            )
                            , modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .clickable {
                                    val permissionCheckResult =
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CAMERA
                                        )

                                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                        cameraLauncher.launch(uri)
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                }
                        )
                    }

                    else{
                        Image(painterResource(id = R.drawable.person), contentDescription = "",
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    val permissionCheckResult =
                                        ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.CAMERA
                                        )

                                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                        cameraLauncher.launch(uri)
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                })
                    }
                    if (user!!.name.isNotEmpty()) {
                        EditText(Icon = Icons.Default.Person, value = name, label = "Name", onChange = {name = it})
                    }else{
                        EditText(Icon = Icons.Default.Person, value = name, label = "Name", onChange = {name = it})
                    }
                    if (user!!.email.isNotEmpty()) {
                        EditText(Icon = Icons.Default.Email, value = email, label = "Email", onChange = {email = it})
                    }else{
                        EditText(Icon = Icons.Default.Email, value = email, label = "Email", onChange = {email = it})
                    }
                    if (user!!.phone.isNotEmpty()) {
                        EditText(Icon = Icons.Default.Phone, value = phone, label = "Phone Number", onChange = {phone = it})
                    }else{
                        EditText(Icon = Icons.Default.Phone, value = phone, label = "Phone Number", onChange = {phone = it})
                    }
                    if (user!!.location.isNotEmpty()) {
                        EditLocation(Icon = Icons.Default.LocationOn, value = location, label = "Location", onChange = {location = it}){
                            getCurrentLocation()
                        }
                    }else{
                        EditLocation(Icon = Icons.Default.LocationOn, value = location, label = "Location", onChange = {location = it}){
                            getCurrentLocation()
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(onClick = {
                                     if (name.isNotEmpty() && phone.isNotEmpty() && location.isNotEmpty()){
                                         vm.updateEditUser(context = context,name = name, phone = phone, location = location)
                                     }else{
                                         Toast.makeText(context, "Fill All Details", Toast.LENGTH_SHORT).show()
                                     }
                                     }, modifier = Modifier.width(150.dp), colors = ButtonDefaults.buttonColors(GreenColor)) {
                        Text(text = "Save")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { vm.signOut()
                                     NavigateWithoutBackStack(navController,AppNavigationComponent.LOGINSCREEN)
                    }, modifier = Modifier.width(150.dp), colors = ButtonDefaults.buttonColors(GreenColor)) {
                        Text(text = "Log Out")
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditText(Icon : ImageVector,value : String,label : String ,onChange : (String) -> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 20.dp)) {
        Icon(imageVector = Icon, contentDescription = null,modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(30.dp))
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(value = value, onValueChange = {onChange(it)}, colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Black,
            disabledIndicatorColor = Color.Transparent
        )
        , label = {
            Text(text = label)
            },
            modifier = Modifier.width(270.dp))
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Rounded.Edit, contentDescription = null,modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(30.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLocation(Icon : ImageVector,value : String,label : String ,onChange : (String) -> Unit, onLocationClicked:()-> Unit){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 30.dp, end = 30.dp, top = 20.dp)) {
        Icon(imageVector = Icon, contentDescription = null,modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(30.dp)
            .clickable {
                onLocationClicked()
            })
        Spacer(modifier = Modifier.padding(5.dp))
        TextField(value = value, onValueChange = {onChange(it)}, colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = Color.Blue,
            unfocusedIndicatorColor = Color.Black,
            disabledIndicatorColor = Color.Transparent
        )
            , label = {
                Text(text = label)
            },
            modifier = Modifier.width(270.dp))
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Rounded.Edit, contentDescription = null, modifier = Modifier
            .align(Alignment.CenterVertically)
            .size(30.dp))
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyy_MM_dd_HH:mm:ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )

    return image
}

fun getAddressFromLocation(context: Context, location: Location, onChange: (String) -> Unit) {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
    if (addresses != null) {
        Log.d("Address", addresses.toString())
        if (addresses.isNotEmpty()) {
            val address = addresses[0]
            Log.d("Address", address.toString())
            // Use address.getAddressLine(0) to get the full address or other methods as needed
            val addressString = address.getAddressLine(0)
            // Update your TextField or state with the address
            Log.d("Address", addressString.toString())
            onChange(addressString)
        }
    }
}