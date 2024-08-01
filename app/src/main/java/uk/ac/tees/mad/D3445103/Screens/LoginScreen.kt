package uk.ac.tees.mad.D3445103.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.D3445103.AppNavigationComponent
import uk.ac.tees.mad.D3445103.R
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel
import uk.ac.tees.mad.D3445103.ui.theme.poppins
import uk.ac.tees.mad.D3445103.utils.GreenColor
import uk.ac.tees.mad.D3445103.utils.NavigateWithBackStack
import uk.ac.tees.mad.D3445103.utils.NavigateWithoutBackStack
import uk.ac.tees.mad.D3445103.utils.ProgressBar


@Composable
fun LoginScreen(vm : PlantViewModel, navController: NavController) {

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val isLoading = vm.isLoading
    val isSignedIn = vm.isSignedIn.value

    if (isSignedIn){
        NavigateWithoutBackStack(navController,AppNavigationComponent.HOMESCREEN)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.height(120.dp))
            Row(modifier = Modifier.padding(90.dp)) {
                Image(
                    painter = painterResource(id = R.drawable._fnp_8egk_220404),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Column {
                    Text(
                        text = "Log in",
                        fontSize = 25.sp,
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Welcome to Plantopedia",
                        fontFamily = poppins,
                    )
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 200.dp)
        ) {
            Spacer(modifier = Modifier.height(200.dp))
            TextField(value = email.value, onValueChange = { email.value = it },
                label = { Text(text = "Email Address", fontFamily = poppins) },
                singleLine = true, )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = password.value, onValueChange = { password.value = it },
                label = { Text(text = "Password", fontFamily = poppins) },
                singleLine = true)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { vm.logIn(context = context, email = email.value, password = password.value) },
                colors = ButtonDefaults.buttonColors(GreenColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 56.dp, end = 56.dp)
            ) {
                if (isLoading.value){
                    ProgressBar()
                }else {
                    Text(
                        text = "Log in",
                        fontFamily = poppins,
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Don't have an account? Sign up",
                fontFamily = poppins,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    NavigateWithBackStack(navController = navController, AppNavigationComponent.SIGNUPSCREEN)
                })
        }
        Image(
            painter = painterResource(id = R.drawable.ski_2360939_1280),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }

}