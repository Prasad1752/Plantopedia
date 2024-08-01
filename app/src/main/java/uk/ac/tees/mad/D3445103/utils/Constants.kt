package uk.ac.tees.mad.D3445103.utils

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.D3445103.AppNavigationComponent

const val BASE_URL = "https://perenual.com/"
const val APIKEY = "sk-q5qh66a2052f696966329"
const val USER = "users"
val GreenColor = Color(0xFF6EA049)
val DarkGrey = Color(0xFF3d3d3d)

fun NavigateWithBackStack(navController: NavController,route: AppNavigationComponent){
    navController.navigate(route.route)
}

fun NavigateWithoutBackStack(navController: NavController,route: AppNavigationComponent){
    navController.navigate(route.route){
        popUpTo(0)
    }
}

@Composable
fun ProgressBar(){
    CircularProgressIndicator(strokeWidth = 3.dp, color = DarkGrey)
}
