package com.example.classiccarchecklist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.classiccarchecklist.data.ChecklistRepository
import com.example.classiccarchecklist.ui.ChecklistDetailScreen
import com.example.classiccarchecklist.ui.ChecklistListScreen
import com.example.classiccarchecklist.ui.ChecklistListViewModel
import com.example.classiccarchecklist.ui.NewChecklistScreen

/**
 * Navigation routes
 */
sealed class Screen(val route: String) {
    object ChecklistList : Screen("checklist_list")
    object NewChecklist : Screen("new_checklist")
    object ChecklistDetail : Screen("checklist_detail/{checklistId}") {
        fun createRoute(checklistId: Long) = "checklist_detail/$checklistId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    checklistListViewModel: ChecklistListViewModel,
    repository: ChecklistRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ChecklistList.route
    ) {
        composable(Screen.ChecklistList.route) {
            ChecklistListScreen(
                viewModel = checklistListViewModel,
                onAddChecklist = {
                    navController.navigate(Screen.NewChecklist.route)
                },
                onChecklistClick = { checklistId ->
                    navController.navigate(Screen.ChecklistDetail.createRoute(checklistId))
                }
            )
        }
        
        composable(Screen.NewChecklist.route) {
            NewChecklistScreen(
                repository = repository,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveComplete = { checklistId ->
                    // Navigate to the checklist detail screen
                    // Pop the NewChecklist screen from back stack so back button goes to list
                    navController.popBackStack()
                    navController.navigate(Screen.ChecklistDetail.createRoute(checklistId))
                }
            )
        }
        
        composable(
            route = Screen.ChecklistDetail.route,
            arguments = listOf(
                navArgument("checklistId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val checklistId = backStackEntry.arguments?.getLong("checklistId")
            if (checklistId != null) {
                ChecklistDetailScreen(
                    repository = repository,
                    checklistId = checklistId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

