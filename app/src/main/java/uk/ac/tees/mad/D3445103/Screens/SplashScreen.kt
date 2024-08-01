package uk.ac.tees.mad.D3445103.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.D3445103.AppNavigationComponent
import uk.ac.tees.mad.D3445103.R
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel
import uk.ac.tees.mad.D3445103.ui.theme.poppins
import uk.ac.tees.mad.D3445103.utils.NavigateWithoutBackStack

@Composable
fun SplashScreen(vm: PlantViewModel, navController: NavController) {

    LaunchedEffect(key1 = true) {
        delay(3000L)
        if (vm.isSignedIn.value) {
            NavigateWithoutBackStack(navController, AppNavigationComponent.HOMESCREEN)
        } else {
            navController.navigate(AppNavigationComponent.LOGINSCREEN.route)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Image(
                painterResource(id = R.drawable.designer), contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Plantopedia",
                fontSize = 30.sp,
                fontFamily = poppins,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(text = "A perfect Dictionary for the Plants",
                fontSize = 14.sp,
                fontFamily = poppins,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}