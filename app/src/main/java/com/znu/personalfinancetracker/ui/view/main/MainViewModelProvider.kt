package com.znu.personalfinancetracker.ui.view.main

import com.znu.personalfinancetracker.data.model.category.CategoryRepositoryImpl
import com.znu.personalfinancetracker.data.model.transaction.TransactionRepositoryImpl
import com.znu.personalfinancetracker.ui.view.categories.CategoriesViewModel
import com.znu.personalfinancetracker.ui.view.transactions.TransactionsViewModel

object MainViewModelProvider {
    private val categoryRepo = CategoryRepositoryImpl()
    private val transactionRepo = TransactionRepositoryImpl(categoryRepo)

    val transactionsViewModel: TransactionsViewModel by lazy {
        TransactionsViewModel(categoryRepo, transactionRepo)
    }

    val categoriesViewModel: CategoriesViewModel by lazy { CategoriesViewModel(categoryRepo) }
}
