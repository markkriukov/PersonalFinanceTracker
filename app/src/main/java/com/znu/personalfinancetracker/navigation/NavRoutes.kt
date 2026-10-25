package com.znu.personalfinancetracker.navigation

import com.znu.personalfinancetracker.data.model.transaction.Transaction
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavRoutes {
    @Serializable data object Transactions : NavRoutes

    @Serializable data object Categories : NavRoutes

    @Serializable data object TransactionManagerNew : NavRoutes

    @Serializable data class TransactionManagerEdit(val transaction: Transaction) : NavRoutes
}
