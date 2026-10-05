package com.example.dessertrelease

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.example.dessertrelease.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

/** Prueba local: crea un DataStore real en una carpeta temporal. */
class UserPreferencesRepositoryTest {
    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Test
    fun isLinearLayout_defaultIsTrue() = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tmpFolder.root, "test1.preferences_pb") }
        )
        val repository = UserPreferencesRepository(dataStore)
        assertTrue(repository.isLinearLayout.first())
    }

    @Test
    fun saveLayoutPreference_false_isPersisted() = runTest {
        val dataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tmpFolder.root, "test2.preferences_pb") }
        )
        val repository = UserPreferencesRepository(dataStore)
        repository.saveLayoutPreference(false)
        assertFalse(repository.isLinearLayout.first())
    }
}
