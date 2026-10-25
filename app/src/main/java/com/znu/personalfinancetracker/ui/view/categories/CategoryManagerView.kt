package com.znu.personalfinancetracker.ui.view.categories

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.znu.personalfinancetracker.data.model.category.Category
import com.znu.personalfinancetracker.ui.view.main.MainViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CategoryManagerView(
        viewModel: CategoriesViewModel = remember { MainViewModelProvider.categoriesViewModel },
        categoryToEdit: Category? = null
) {
    val name by viewModel.name
    val description by viewModel.description
    val isExpense by viewModel.isExpense
    val nameError by viewModel.nameError

    LaunchedEffect(categoryToEdit) {
        categoryToEdit?.let { viewModel.startEdit(it) } ?: viewModel.cancelEdit()
    }

    val title = if (categoryToEdit == null) "NEW CATEGORY" else "EDIT CATEGORY"

    fun saveCategory() {
        if (categoryToEdit == null) {
            viewModel.saveCategory()
        } else {
            val updated =
                    categoryToEdit.copy(
                            name = name,
                            description = description,
                            isExpense = isExpense
                    )
            viewModel.updateCategory(updated)
        }
    }

    fun cancelCategory() {
        viewModel.cancelEdit()
    }

    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                        title = { Text(title, style = MaterialTheme.typography.headlineMedium) }
                )
            }
    ) { paddingValues ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(paddingValues)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
        ) {
            // Name Field
            OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Category name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = nameError,
                    supportingText =
                            if (nameError) {
                                {
                                    Text(
                                            "The field cannot be empty",
                                            color = MaterialTheme.colorScheme.error
                                    )
                                }
                            } else null
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Description Field
            OutlinedTextField(
                    value = description,
                    onValueChange = { viewModel.updateDescription(it) },
                    label = { Text("Category description") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Type Switch
            Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                        text = "Type: ${if (isExpense) "Expense" else "Income"}",
                        style = MaterialTheme.typography.bodyMedium
                )
                Switch(checked = isExpense, onCheckedChange = { viewModel.updateIsExpense(it) })
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { cancelCategory() }) { Text("Cancel") }
                Button(onClick = { saveCategory() }, enabled = !nameError && name.isNotBlank()) {
                    Text("Save")
                }
            }
        }
    }
}
