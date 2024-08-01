package uk.ac.tees.mad.D3445103.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.D3445103.AppNavigationComponent
import uk.ac.tees.mad.D3445103.utils.GreenColor
import uk.ac.tees.mad.D3445103.utils.NavigateWithBackStack


enum class BottomNavigationItem(
    val navDestination : AppNavigationComponent,
    val icon: ImageVector,
) {
    HomeScreen(AppNavigationComponent.HOMESCREEN, Icons.Filled.Home),
    SearchScreen(AppNavigationComponent.SEARCHSCREEN,  Icons.Filled.Search),
    SavedScreen(AppNavigationComponent.SAVEDSCREEN, Icons.Filled.Favorite),
    ProfileScreen(AppNavigationComponent.PROFILESCREEN, Icons.Filled.Person)
}


@Composable
fun BottomNavigation(selectedItem: BottomNavigationItem,navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenColor), verticalAlignment = Alignment.Bottom
    ) {


        for (items in BottomNavigationItem.entries) {
            Image(
                imageVector = items.icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .weight(1f)
                    .clickable { NavigateWithBackStack(navController = navController,items.navDestination) },
                colorFilter = if (selectedItem == items){
                    ColorFilter.tint(color = Color.White)
                }
                else{
                    ColorFilter.tint(Color.Black)
                })
        }
    }
}