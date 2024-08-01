package uk.ac.tees.mad.D3445103.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import uk.ac.tees.mad.D3445103.AppNavigationComponent
import uk.ac.tees.mad.D3445103.R
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel
import uk.ac.tees.mad.D3445103.models.remote.DefaultImage
import uk.ac.tees.mad.D3445103.ui.theme.poppins
import uk.ac.tees.mad.D3445103.utils.GreenColor
import uk.ac.tees.mad.D3445103.utils.NavigateWithBackStack
import uk.ac.tees.mad.D3445103.utils.ProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: PlantViewModel, navController: NavController) {
    val isLoading = vm.isLoading
    val plants = vm.plantsInfo
    val error = vm.error.observeAsState("")
    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(refreshing) {
        if (refreshing) {
            vm.fetchPlants()
            delay(3000)
            refreshing = false
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars),
        topBar = {
            TopAppBar(title = {
                Row(modifier = Modifier.padding(end = 10.dp)) {
                    Text(
                        text = "Plantopedia",
                        fontSize = 30.sp,
                        color = GreenColor,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "search",
                        tint = GreenColor, modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                NavigateWithBackStack(
                                    navController,
                                    AppNavigationComponent.SEARCHSCREEN
                                )
                            })
                }
            }
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedItem = BottomNavigationItem.HomeScreen,
                navController = navController
            )
        }
    ) { innerpadding ->
        Column(modifier = Modifier.padding(innerpadding)) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = refreshing),
                onRefresh = { refreshing = true }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column {

                        if (error.value.isNotEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Error Loading Data", modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                        if (plants.value != null) {
                            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                                items(plants.value!!.data) { item ->
                                    PlantView(
                                        imageurl = item.default_image,
                                        name = item.common_name,
                                        onCardClick = {
                                            navController.navigate(
                                                AppNavigationComponent.DETAILSCREEN.createRoute(item.id.toString())
                                            )
                                        },
                                        onSaveClick = {vm.insertPlant(item)}
                                    )
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Button(
                                            onClick = { vm.fetchPreviousPage() },
                                            colors = ButtonDefaults.buttonColors(
                                                GreenColor
                                            )
                                        ) {
                                            Text(text = "Previous Page")
                                        }
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Button(
                                            onClick = { vm.fetchNextPage() },
                                            colors = ButtonDefaults.buttonColors(
                                                GreenColor
                                            )
                                        ) {
                                            Text(text = "Next Page")
                                        }
                                    }
                                }
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Current Page: ${plants.value!!.current_page}",
                                            fontFamily = poppins,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LinearProgressIndicator()
                            }
                        }
                    }
                }
                if (isLoading.value) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        LinearProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun PlantView(
    imageurl: DefaultImage?,
    name: String,
    onCardClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .size(230.dp, 270.dp)
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