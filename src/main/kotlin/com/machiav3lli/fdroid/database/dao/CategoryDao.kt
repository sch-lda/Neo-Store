package com.machiav3lli.fdroid.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.machiav3lli.fdroid.database.entity.Category
import com.machiav3lli.fdroid.database.entity.CategoryTemp
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<Category> {
    @get:Query(
        """SELECT DISTINCT category.label
        FROM category AS category
        JOIN repository AS repository
        ON category.repositoryId = repository._id
        WHERE repository.enabled != 0"""
    )
    val allNames: List<String>

    @get:Query(
        """SELECT DISTINCT category.label
        FROM category AS category
        JOIN repository AS repository
        ON category.repositoryId = repository._id
        WHERE repository.enabled != 0"""
    )
    val allNamesFlow: Flow<List<String>>

    @Query("DELETE FROM category WHERE repositoryId = :id")
    fun deleteById(id: Long): Int
}

@Dao
interface CategoryTempDao : BaseDao<CategoryTemp> {
    @get:Query("SELECT * FROM temporary_category")
    val all: Array<CategoryTemp>

    @Query("DELETE FROM temporary_category")
    fun emptyTable()
}