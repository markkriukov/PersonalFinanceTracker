package com.znu.personalfinancetracker.data.model.category

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ParameterizedCategoryRepositoryImplTest(
    private val name: String,
    private val description: String?,
    private val isExpense: Boolean,
    private val expectedIncomeCount: Int
) {

    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setUp() {
        repository = CategoryRepositoryImpl()
    }

    @Test
    fun addCategory() {
        val added = repository.addCategory(name, description, isExpense)

        assertEquals(name, added.name)
        assertEquals(description, added.description)
        assertEquals(isExpense, added.isExpense)
        assertEquals(added, repository.getCategoryById(added.id))
        assertEquals(expectedIncomeCount, repository.getIncomeCategories().size)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: addCategory(\"{0}\", {1}, isExpense = {2}) -> доходів {3}")
        fun data(): Collection<Array<Any?>> = listOf(
            arrayOf("Транспорт", "Проїзд у громадському транспорті", true, 1),
            arrayOf("Стипендія", null, false, 2),
            arrayOf("Кафе", null, true, 1),
            arrayOf("Фриланс", "Підробіток", false, 2)
        )
    }
}
