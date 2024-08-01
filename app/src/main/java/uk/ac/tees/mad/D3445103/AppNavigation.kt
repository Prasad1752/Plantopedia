package uk.ac.tees.mad.D3445103

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import uk.ac.tees.mad.D3445103.Screens.DetailScreen
import uk.ac.tees.mad.D3445103.Screens.HomeScreen
import uk.ac.tees.mad.D3445103.Screens.LoginScreen
import uk.ac.tees.mad.D3445103.Screens.ProfileScreen
import uk.ac.tees.mad.D3445103.Screens.SavedScreen
import uk.ac.tees.mad.D3445103.Screens.SearchScreen
import uk.ac.tees.mad.D3445103.Screens.SignUpScreen
import uk.ac.tees.mad.D3445103.Screens.SplashScreen
import uk.ac.tees.mad.D3445103.ViewModel.PlantViewModel

sealed class AppNavigationComponent(val route:String){
    object SPLASHSCREEN: AppNavigationComponent("splashscreen")
    object SIGNUPSCREEN : AppNavigationComponent("signupscreen")
    object LOGINSCREEN : AppNavigationComponent("loginscreen")
    object HOMESCREEN: AppNavigationComponent("homescreen")
    object DETAILSCREEN : AppNavigationComponent("detailsscreen/{id}"){
        fun createRoute(id:String) = "detailsscreen/$id"
    }
    object PROFILESCREEN : AppNavigationComponent("profilescreen")
    object SAVEDSCREEN : AppNavigationComponent("savedscreen")
    object SEARCHSCREEN : AppNavigationComponent("searchscreen")
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppNavigation() {
    val vm: PlantViewModel = viewModel()
    val navController = rememberNavController()
    Surface {
        NavHost(
            navController = navController,
            startDestination = AppNavigationComponent.SPLASHSCREEN.route
        ) {
            composable(route = AppNavigationComponent.SPLASHSCREEN.route) {
                SplashScreen(vm = vm, navController =  navController)
            }
            composable(route = AppNavigationComponent.LOGINSCREEN.route) {
                LoginScreen(vm = vm, navController = navController)
            }
            composable(route = AppNavigationComponent.SIGNUPSCREEN.route) {
                SignUpScreen(vm = vm, navController = navController)
            }
            composable(route = AppNavigationComponent.HOMESCREEN.route) {
                HomeScreen(vm = vm, navController = navController)
            }
            composable(route = AppNavigationComponent.DETAILSCREEN.route,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                    }
                )
                ) {backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                DetailScreen(vm = vm, navController = navController, id = id)
            }
            composable(route = AppNavigationComponent.PROFILESCREEN.route) {
                ProfileScreen(vm = vm, navController = navController)
            }
            composable(route = AppNavigationComponent.SAVEDSCREEN.route) {
                SavedScreen(vm = vm, navController = navController)
            }
            composable(route = AppNavigationComponent.SEARCHSCREEN.route){
                SearchScreen(vm = vm, navController = navController)
            }
        }
    }
}