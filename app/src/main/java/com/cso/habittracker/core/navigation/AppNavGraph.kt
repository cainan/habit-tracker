package com.cso.habittracker.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cso.habittracker.feature.habit.presentation.createedit.CreateEditRoot
import com.cso.habittracker.feature.habit.presentation.stats.StatsRoot
import com.cso.habittracker.feature.habit.presentation.today.TodayRoot
import kotlinx.serialization.Serializable

@Serializable
data object TodayRoute

@Serializable
data object StatsRoute

@Serializable
data class CreateEditRoute(val habitId: Long = -1L)

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = TodayRoute,
        modifier = modifier
    ) {
        composable<TodayRoute> {
            TodayRoot(
                onNavigateToStats = { navController.navigate(StatsRoute) },
                onNavigateToCreate = { navController.navigate(CreateEditRoute()) },
                onNavigateToEdit = { habitId -> navController.navigate(CreateEditRoute(habitId)) }
            )
        }

        composable<StatsRoute> {
            StatsRoot(
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<CreateEditRoute> { backStackEntry ->
            val route: CreateEditRoute = backStackEntry.toRoute()
            CreateEditRoot(
                habitId = route.habitId,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}
