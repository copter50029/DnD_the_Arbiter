package com.example.final_project.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.final_project.ArbiterApplication
import com.example.final_project.R
import com.example.final_project.ui.screens.CampaignScreen
import com.example.final_project.ui.screens.CompendiumScreen
import com.example.final_project.ui.screens.DramatisPersonaeScreen
import com.example.final_project.ui.screens.SessionArchitectScreen
import com.example.final_project.ui.theme.ArcaneGold
import com.example.final_project.ui.theme.ArcaneGoldBright
import com.example.final_project.ui.theme.ArcaneGoldDark
import com.example.final_project.ui.theme.DarkLeatherBorder
import com.example.final_project.ui.theme.DarkLeatherCard
import com.example.final_project.ui.theme.DarkLeatherDeep
import com.example.final_project.ui.viewmodel.CampaignViewModel
import com.example.final_project.ui.viewmodel.CompendiumViewModel
import com.example.final_project.ui.viewmodel.DramatisPersonaeViewModel
import com.example.final_project.ui.viewmodel.SessionArchitectViewModel

sealed class Screen(val route: String, val titleRes: Int, val icon: ImageVector) {
    object Campaigns : Screen("campaigns", R.string.nav_campaigns, Icons.Default.Book)
    object SessionArchitect : Screen("session_architect", R.string.nav_session_architect, Icons.Default.EditNote)
    object DramatisPersonae : Screen("dramatis_personae", R.string.nav_dramatis_personae, Icons.Default.Groups)
    object GrandCompendium : Screen("grand_compendium", R.string.nav_grand_compendium, Icons.Default.AutoStories)
}

val bottomNavScreens = listOf(
    Screen.Campaigns,
    Screen.SessionArchitect,
    Screen.DramatisPersonae,
    Screen.GrandCompendium
)

@Composable
fun ArbiterMainScreen() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as ArbiterApplication

    val campaignViewModel: CampaignViewModel = viewModel(
        factory = CampaignViewModel.Factory(app.campaignRepository, app.userSettingsRepository)
    )
    val sessionViewModel: SessionArchitectViewModel = viewModel(
        factory = SessionArchitectViewModel.Factory(
            app.campaignRepository,
            app.sessionRepository,
            app.aiRepository,
            app.userSettingsRepository
        )
    )
    val dramatisViewModel: DramatisPersonaeViewModel = viewModel(
        factory = DramatisPersonaeViewModel.Factory(
            app.campaignRepository,
            app.characterRepository,
            app.dndRepository,
            app.aiRepository,
            app.userSettingsRepository
        )
    )
    val compendiumViewModel: CompendiumViewModel = viewModel(
        factory = CompendiumViewModel.Factory(app.dndRepository)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = DarkLeatherDeep,
                tonalElevation = androidx.compose.ui.unit.Dp(8f)
            ) {
                bottomNavScreens.forEach { screen ->
                    val selected = currentDestination?.route?.startsWith(screen.route) == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = screen.icon, contentDescription = stringResource(screen.titleRes))
                        },
                        label = {
                            Text(
                                text = stringResource(screen.titleRes),
                                fontFamily = FontFamily.Serif,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArcaneGoldBright,
                            selectedTextColor = ArcaneGoldBright,
                            indicatorColor = DarkLeatherCard,
                            unselectedIconColor = ArcaneGoldDark.copy(alpha = 0.6f),
                            unselectedTextColor = ArcaneGoldDark.copy(alpha = 0.6f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Campaigns.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Campaigns.route) {
                CampaignScreen(
                    viewModel = campaignViewModel,
                    onNavigateToSession = { campId ->
                        sessionViewModel.selectCampaign(campId)
                        navController.navigate(Screen.SessionArchitect.route)
                    },
                    onNavigateToDramatis = { campId ->
                        dramatisViewModel.selectCampaign(campId)
                        navController.navigate(Screen.DramatisPersonae.route)
                    }
                )
            }

            composable(Screen.SessionArchitect.route) {
                SessionArchitectScreen(viewModel = sessionViewModel)
            }

            composable(
                route = Screen.DramatisPersonae.route + "?monsterIndex={monsterIndex}",
                arguments = listOf(
                    navArgument("monsterIndex") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val monsterIndex = backStackEntry.arguments?.getString("monsterIndex")
                DramatisPersonaeScreen(
                    viewModel = dramatisViewModel,
                    prefilledMonsterIndex = monsterIndex
                )
            }

            composable(Screen.GrandCompendium.route) {
                CompendiumScreen(
                    viewModel = compendiumViewModel,
                    onExportToDramatis = { monsterIndex ->
                        navController.navigate("${Screen.DramatisPersonae.route}?monsterIndex=$monsterIndex") {
                            popUpTo(Screen.Campaigns.route)
                        }
                    }
                )
            }
        }
    }
}
