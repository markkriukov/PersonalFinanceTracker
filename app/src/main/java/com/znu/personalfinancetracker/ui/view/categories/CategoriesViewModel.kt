package com.znu.personalfinancetracker.ui.view.categories

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.znu.personalfinancetracker.data.model.category.Category
import com.znu.personalfinancetracker.data.model.category.CategoryRepository

class CategoriesViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    val categories =
            mutableStateListOf<Category>().apply { addAll(categoryRepository.getAllCategories()) }

    var name = mutableStateOf("")
        private set
    var description = mutableStateOf("")
        private set
    var isExpense = mutableStateOf(true)
        private set
    var nameError = mutableStateOf(false)
        private set
    var editingCategory = mutableStateOf<Category?>(null)
        private set

    fun updateName(newName: String) {
        name.value = newName.trim()
        nameError.value = newName.isBlank()
    }

    fun updateDescription(newDesc: String) {
        description.value = newDesc.trim()
    }

    fun updateIsExpense(isExpense: Boolean) {
        this.isExpense.value = isExpense
    }

    fun startEdit(category: Category) {
        editingCategory.value = category
        name.value = category.name
        description.value = category.description ?: ""
        isExpense.value = category.isExpense
        nameError.value = false
    }

    fun cancelEdit() {
        editingCategory.value = null
        resetForm()
    }

    fun saveCategory() {
        val trimmedName = name.value.trim()
        if (trimmedName.isBlank()) {
            nameError.value = true
            return
        }
        val newCategory =
                categoryRepository.addCategory(
                        name = trimmedName,
                        description = description.value.takeIf { it.isNotBlank() },
                        isExpense = isExpense.value
                )
        categories.add(newCategory)

        cancelEdit()
    }

    fun updateCategory(category: Category) {
        categoryRepository.updateCategory(category)
        val index = categories.indexOfFirst { it.id == category.id }
        if (index != -1) categories[index] = category
    }

    fun deleteCategory(category: Category) {
        if (categoryRepository.deleteCategory(category.id)) {
            categories.remove(category)
        }
    }

    private fun resetForm() {
        name.value = ""
        description.value = ""
        isExpense.value = true
        nameError.value = false
    }
}
