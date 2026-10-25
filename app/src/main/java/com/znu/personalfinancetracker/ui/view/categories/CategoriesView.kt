package com.znu.personalfinancetracker.ui.view.categories

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.znu.personalfinancetracker.ui.view.categories.components.CategoryItem
import com.znu.personalfinancetracker.ui.view.main.MainViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CategoriesView(
        viewModel: CategoriesViewModel = remember { MainViewModelProvider.categoriesViewModel },
) {
    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                        title = {
                            Text("CATEGORIES", style = MaterialTheme.typography.headlineMedium)
                        }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                        modifier = Modifier.padding(16.dp),
                        onClick = { /* TODO: Implement transition to Transaction Manager screen */}
                ) { Icon(Icons.Filled.Add, contentDescription = "Add Category") }
            }
    ) { paddingValues ->
        LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            itemsIndexed(viewModel.categories) { index, category ->
                CategoryItem(
                        category = category,
                        onItemClick = { /* TODO: handleCategoryClick */},
                        onRemoveClick = { viewModel.deleteCategory(category) }
                )
                if (index < viewModel.categories.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
