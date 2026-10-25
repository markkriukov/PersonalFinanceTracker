package com.znu.personalfinancetracker.ui.view.transactions

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.znu.personalfinancetracker.data.model.transaction.Transaction
import com.znu.personalfinancetracker.ui.view.main.MainViewModelProvider
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TransactionManagerView(
        viewModel: TransactionsViewModel = remember { MainViewModelProvider.transactionsViewModel },
        onReturn: () -> Unit = {},
        transactionToEdit: Transaction? = null
) {
    val categories by viewModel.categories.observeAsState(emptyList())
    val title = if (transactionToEdit == null) "NEW TRANSACTION" else "EDIT TRANSACTION"

    LaunchedEffect(transactionToEdit) {
        transactionToEdit?.let { tx ->
            viewModel.updateDescription(tx.description)
            viewModel.updateAmount(tx.amount.absoluteValue.toString())
            viewModel.updateDate(tx.date)
            viewModel.updateCategory(tx.category)
        }
        ?: run {
            // Only call cancelTransaction if we are not editing, or if a previous edit was cancelled
            viewModel.cancelTransaction()
        }
    }

    fun saveTransaction() {
        if (transactionToEdit == null) {
            viewModel.addTransaction()
        } else {
            val amountValue = viewModel.amount.toDoubleOrNull() ?: return
            val selectedCategory = viewModel.selectedCategory ?: return
            val signedAmount =
                    if (selectedCategory.isExpense) -amountValue else amountValue
            val updatedTransaction =
                    transactionToEdit.copy(
                            date = viewModel.date,
                            description = viewModel.description,
                            amount = signedAmount,
                            category = selectedCategory
                    )
            viewModel.updateTransaction(updatedTransaction)
        }
        onReturn()
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
            topBar = {
                if (!isLandscape) {
                    CenterAlignedTopAppBar(
                            title = { Text(title, style = MaterialTheme.typography.headlineMedium) }
                    )
                }
            }
    ) { paddingValues ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
        ) {
            // Description
            OutlinedTextField(
                    value = viewModel.description,
                    onValueChange = { viewModel.updateDescription(it) },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Amount
            OutlinedTextField(
                    value = viewModel.amount,
                    onValueChange = { viewModel.updateAmount(it) },
                    label = { Text("Amount (₴)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = viewModel.amountError,
                    supportingText =
                            if (viewModel.amountError) {
                                {
                                    Text(
                                            "Enter a valid number",
                                            color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else null
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Date
            OutlinedTextField(
                    value = viewModel.date,
                    onValueChange = { viewModel.updateDate(it) },
                    label = { Text("Date (DD.MM.YYYY)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = viewModel.dateError,
                    supportingText =
                            if (viewModel.dateError) {
                                {
                                    Text(
                                            "Format: DD.MM.YYYY",
                                            color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else null
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Category Dropdown
            Box {
                OutlinedTextField(
                        value = viewModel.selectedCategory?.name ?: "",
                        onValueChange = {},
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { viewModel.toggleExpanded() }) {
                                Icon(
                                        imageVector =
                                                if (viewModel.expanded)
                                                        Icons.Default.KeyboardArrowUp
                                                else Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Toggle category"
                                )
                            }
                        }
                )
                DropdownMenu(
                        expanded = viewModel.expanded,
                        onDismissRequest = { viewModel.toggleExpanded() },
                        modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.updateCategory(category)
                                    viewModel.toggleExpanded()
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                        onClick = {
                            viewModel.cancelTransaction()
                            onReturn()
                        }
                ) { Text("Cancel") }
                val canSave =
                        viewModel.description.isNotBlank() &&
                                viewModel.amount.isNotBlank() &&
                                !viewModel.amountError &&
                                viewModel.date.isNotBlank() &&
                                !viewModel.dateError &&
                                viewModel.selectedCategory != null

                Button(onClick = { saveTransaction() }, enabled = canSave) { Text("Save") }
            }
        }
    }
}
