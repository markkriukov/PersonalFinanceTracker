package com.znu.personalfinancetracker.ui.view.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.znu.personalfinancetracker.data.model.transaction.Transaction
import com.znu.personalfinancetracker.navigation.NavRoutes
import com.znu.personalfinancetracker.navigation.TransactionNavType
import com.znu.personalfinancetracker.ui.view.categories.CategoriesView
import com.znu.personalfinancetracker.ui.view.transactions.TransactionManagerView
import com.znu.personalfinancetracker.ui.view.transactions.TransactionsView
import kotlin.reflect.typeOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MainView() {
    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabItems = listOf(NavRoutes.Transactions, NavRoutes.Categories)

    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                        title = {
                            Text(
                                    "Personal Finance Tracker",
                                    style = MaterialTheme.typography.headlineMedium
                            )
                        }
                )
            }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { index, screen ->
                    val title =
                            when (screen) {
                                NavRoutes.Categories -> "Categories"
                                else -> "Transactions"
                            }
                    Tab(
                            text = { Text(title) },
                            selected = selectedTabIndex == index,
                            onClick = {
                                selectedTabIndex = index
                                navController.navigate(screen) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                    )
                }
            }

            NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Transactions, // Start with the object
                    modifier = Modifier.fillMaxSize()
            ) {
                composable<NavRoutes.Transactions> {
                    TransactionsView(
                            onAddTransaction = {
                                navController.navigate(NavRoutes.TransactionManagerNew)
                            },
                            onTransactionClick = { transaction ->
                                navController.navigate(
                                        NavRoutes.TransactionManagerEdit(transaction = transaction)
                                )
                            }
                    )
                }

                composable<NavRoutes.Categories> { CategoriesView() }

                composable<NavRoutes.TransactionManagerNew> {
                    TransactionManagerView(onReturn = { navController.popBackStack() })
                }

                composable<NavRoutes.TransactionManagerEdit>(
                        typeMap = mapOf(typeOf<Transaction>() to TransactionNavType())
                ) { navBackStackEntry ->
                    val route = navBackStackEntry.toRoute<NavRoutes.TransactionManagerEdit>()
                    TransactionManagerView(
                            onReturn = { navController.popBackStack() },
                            transactionToEdit = route.transaction
                    )
                }
            }
        }
    }
}
