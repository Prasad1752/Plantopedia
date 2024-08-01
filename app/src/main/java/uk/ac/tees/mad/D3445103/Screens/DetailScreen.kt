package uk.ac.tees.mad.D3445103.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.D3445103.AppNavigationComponent
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel
import uk.ac.tees.mad.D3445103.ui.theme.poppins
import uk.ac.tees.mad.D3445103.utils.GreenColor
import uk.ac.tees.mad.D3445103.utils.NavigateWithoutBackStack


@Composable
fun DetailScreen(vm: PlantViewModel, navController: NavController, id: String?) {
    val plant = vm.plantsInfo
    val newid = id?.toInt()
    val plantInfo = plant.value?.data?.find { it.id == newid }
    val image = plantInfo?.default_image!!.original_url
    Log.d("image", image)
    val name = plantInfo.common_name
    val cycle = plantInfo.cycle
    val watering = plantInfo.watering
    val scroll = rememberScrollState()

    // Loading state for the image
    var isLoading by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent
            ) {
                Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    GreenColor)) {
                    Text(text = "Add to Favorite")
                }
            }
        }
    ) { iv ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(iv)
                .verticalScroll(scroll)
        ) {

            Column {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onLoading = {
                        isLoading = true
                    },
                    onSuccess = {
                        isLoading = false
                    },
                    onError = {
                        isLoading = false
                    }
                )
                if (isLoading) {
                    // Show a loading indicator while the image is being loaded
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)

                    ) {
                        Text(
                            text = name, fontSize = 30.sp,
                            fontFamily = poppins, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = plantInfo.scientific_name.toString(), fontSize = 22.sp,
                            fontFamily = poppins, fontWeight = FontWeight.Bold
                        )
                        Text(text = "Cycle : $cycle", fontSize = 20.sp, fontFamily = poppins)
                        Text(text = "Watering : $watering", fontSize = 20.sp, fontFamily = poppins)

                        LazyRow {
                            item {
                                Text(text = "Sunlight : ", fontSize = 20.sp, fontFamily = poppins)
                            }
                            items(plantInfo.sunlight) { items ->
                                Text(text = "${items.capitalizeFirstLetter()}   ", fontSize = 20.sp, fontFamily = poppins)
                            }
                        }
                        LazyRow {
                            item {
                                Text(text = "Other Name : ", fontSize = 20.sp, fontFamily = poppins)
                            }
                            items(plantInfo.other_name) { items ->
                                Text(text = "$items   ", fontSize = 20.sp, fontFamily = poppins)
                            }
                        }
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Navigate back",
                modifier = Modifier
                    .size(60.dp)
                    .padding(4.dp)
                    .clickable {
                        NavigateWithoutBackStack(navController, AppNavigationComponent.HOMESCREEN)
                    },
                tint = Color.White
            )
        }
    }
}

fun String.capitalizeFirstLetter(): String {
    return this.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}