package com.n26.cointracker

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.n26.cointracker.feature.details.ui.PriceDetailsOnDayScreen
import com.n26.cointracker.feature.home.ui.HomeScreen
import com.n26.cointracker.ui.designsystem.DesignSystemTheme
import kotlinx.serialization.Serializable

@Serializable
object HomeScreenDestination

@Serializable
data class PriceDetailsOnDayDestination(val dateTimestamp: Long)

@Suppress("ktlint:compose:modifier-missing-check")
@Composable
fun ComposeApp() {
	DesignSystemTheme {
		Surface {
			val navController: NavHostController = rememberNavController()

			NavHost(navController = navController, startDestination = HomeScreenDestination) {
				composable<HomeScreenDestination> {
					HomeScreen(navigateToDetails = { objectId ->
						navController.navigate(PriceDetailsOnDayDestination(objectId))
					})
				}
				composable<PriceDetailsOnDayDestination> { backStackEntry ->
					PriceDetailsOnDayScreen(
						dateTimeStamp = backStackEntry.toRoute<PriceDetailsOnDayDestination>().dateTimestamp,
						navigateBack = {
							navController.popBackStack()
						},
					)
				}
			}
		}
	}
}
