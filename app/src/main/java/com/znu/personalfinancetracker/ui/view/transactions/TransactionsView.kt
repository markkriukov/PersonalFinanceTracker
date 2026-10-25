package com.znu.personalfinancetracker.ui.view.transactions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState // Import for LiveData observation
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.znu.personalfinancetracker.data.model.transaction.Transaction
import com.znu.personalfinancetracker.ui.view.main.MainViewModelProvider
import com.znu.personalfinancetracker.ui.view.transactions.components.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TransactionsView(
        viewModel: TransactionsViewModel = remember { MainViewModelProvider.transactionsViewModel },
        onAddTransaction: () -> Unit = {},
        onTransactionClick: (Transaction) -> Unit = {}
) {
    val transactions by viewModel.transactions.observeAsState(emptyList())

    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                        title = {
                            Text("MY FINANCES", style = MaterialTheme.typography.headlineMedium)
                        }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                        onClick = onAddTransaction,
                        modifier = Modifier.padding(16.dp)
                ) { Icon(Icons.Filled.Add, contentDescription = "Add Transaction") }
            }
    ) { paddingValues ->
        LazyColumn(
                modifier =
                        Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactions, key = { it.id }) { transaction ->
                TransactionItem(
                        transaction = transaction,
                        onItemClick = { onTransactionClick(transaction) }
                )
            }
        }
    }
}
