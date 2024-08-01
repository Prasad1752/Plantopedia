package uk.ac.tees.mad.D3445103.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.motion.widget.KeyCycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel
import uk.ac.tees.mad.D3445103.ui.theme.poppins

@Composable
fun SavedScreen(vm: PlantViewModel, navController: NavController) {
    val plantsInfo by vm.plantsInfoDB.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        bottomBar = {
            BottomNavigation(
                selectedItem = BottomNavigationItem.SavedScreen,
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            LazyColumn() {
                items(plantsInfo) { items ->
                    HomeScreenView(
                        items.default_image!!.original_url,
                        items.common_name,
                        items.watering,
                        items.scientific_name,
                        items.other_name,
                        items.cycle,
                        items.sunlight
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreenView(
    imageUrl: String = "",
    name: String = "",
    watering: String = "",
    scientific: List<String> = emptyList(),
    otherName: List<String> = emptyList(),
    cycle: String = "",
    sunlight: List<String> = emptyList(),
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(24.dp)
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = name,
                    fontFamily = poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(4.dp)
                )
                LazyRow {
                    item {
                        Text(text = "Scientific Name : ", fontFamily = poppins ,  fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(4.dp))
                    }
                    items(scientific) { items ->
                        Text(text = "$items - ", fontFamily = poppins , fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(4.dp))
                    }
                }
                LazyRow {
                    item {
                        Text(text = "Sunlight : ", fontFamily = poppins ,  fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(4.dp))
                    }
                    items(sunlight) { items ->
                        Text(text = "$items - ", fontFamily = poppins,  fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(4.dp))
                    }
                }
                LazyRow {
                    item {
                        Text(text = "Other Name : ", fontFamily = poppins,  fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(4.dp))
                    }
                    items(otherName) { items ->
                        Text(text = "$items - ", fontFamily = poppins, fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }

        Text(
            text = "watering: $watering",
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = "cycle: $cycle",
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}


