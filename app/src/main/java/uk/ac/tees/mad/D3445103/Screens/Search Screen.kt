package uk.ac.tees.mad.D3445103.Screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.D3445103.AppNavigationComponent
import uk.ac.tees.mad.D3445103.R
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel
import uk.ac.tees.mad.D3445103.models.remote.Data
import uk.ac.tees.mad.D3445103.models.remote.DefaultImage
import uk.ac.tees.mad.D3445103.ui.theme.poppins
import uk.ac.tees.mad.D3445103.utils.GreenColor
import uk.ac.tees.mad.D3445103.utils.NavigateWithBackStack

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SearchScreen(vm: PlantViewModel, navController: NavController) {
    val searchText = remember {
        mutableStateOf("")
    }
    val plantsInfo = remember {
        mutableStateOf<List<Data>>(emptyList())
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),

        bottomBar = {
            BottomNavigation(
                selectedItem = BottomNavigationItem.SearchScreen,
                navController = navController
            )
        }
    ) { innerpadding ->

        Column(modifier = Modifier.padding(innerpadding)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.weight(1f),
                    label = {
                        Text(text = "Search your plant")
                    }
                )
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            val respose = vm.searchPlants(searchText.value)
                            Log.d("Response for Search ", respose.toString())
                            plantsInfo.value = respose
                        })
            }
            if (plantsInfo.value.isEmpty()){
                Text(text = "No Any Plants Info Available", color = Color.Red, fontFamily = poppins, fontWeight = FontWeight.Bold,modifier = Modifier.align(Alignment.CenterHorizontally))
            }else{
            LazyColumn {
                items(plantsInfo.value) { items ->
                    SearchPlantView(imageurl = items.default_image, name = items.common_name,
                        onCardClick = {
                            navController.navigate(
                                AppNavigationComponent.DETAILSCREEN.createRoute(
                                    items.id.toString()
                                )
                            )
                        },
                        onSaveClick = {
                            vm.insertPlant(items)
                        }
                    )
                }
            }
            }
        }
    }
}


@Composable
fun SearchPlantView(imageurl: DefaultImage?, name: String, onCardClick : () -> Unit,
                    onSaveClick : () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .padding(10.dp)
            .background(Color.Transparent)
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.8f) // Set transparency here
        ),
        elevation = CardDefaults.cardElevation(4.dp) // Optional: adjust card elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.Transparent)
        ) {
            if (imageurl != null) {
                AsyncImage(
                    model = imageurl.thumbnail,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No Image",
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontFamily = poppins,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onSaveClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.bookmark__1_),
                        contentDescription = "Save to DB",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}