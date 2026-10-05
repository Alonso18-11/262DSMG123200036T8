package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/** Repositorio: inserta, actualiza, elimina y obtiene artículos. */
interface ItemsRepository {
    fun getAllItemsStream(): Flow<List<Item>>
    fun getItemStream(id: Int): Flow<Item?>
    suspend fun insertItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun updateItem(item: Item)
}
