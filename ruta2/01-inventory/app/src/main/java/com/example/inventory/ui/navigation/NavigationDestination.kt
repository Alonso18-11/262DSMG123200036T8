package com.example.inventory.ui.navigation

/** Describe un destino de navegación de la app. */
interface NavigationDestination {
    /** Nombre único de la ruta */
    val route: String
    /** Recurso de texto con el título de la pantalla */
    val titleRes: Int
}
