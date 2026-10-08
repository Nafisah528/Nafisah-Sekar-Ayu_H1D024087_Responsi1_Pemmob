package com.example.digimonexplorer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.digimonexplorer.ui.ViewModelFactory
import com.example.digimonexplorer.ui.detail.DetailScreen
import com.example.digimonexplorer.ui.detail.DetailViewModel
import com.example.digimonexplorer.ui.home.HomeScreen
import com.example.digimonexplorer.ui.home.HomeViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detail : Screen("detail/{digimonId}") {
        fun createRoute(digimonId: Int): String = "detail/$digimonId"
    }
}

@Composable
fun DigimonNavGraph(
    viewModelFactory: ViewModelFactory,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(factory = viewModelFactory)
            HomeScreen(
                viewModel = homeViewModel,
                onDigimonClick = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("digimonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val digimonId = backStackEntry.arguments?.getInt("digimonId") ?: 0
            val detailViewModel: DetailViewModel = viewModel(factory = viewModelFactory)
            DetailScreen(
                digimonId = digimonId,
                viewModel = detailViewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
