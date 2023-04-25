package com.dev.hirelink

import android.app.Application
import com.dev.hirelink.network.auth.AuthRepository

// Classe HirelinkApplication héritant de la classe Application
class HirelinkApplication : Application() {

    // Propriété authRepository de type AuthRepository, initialisée avec un objet de type AuthRepository
    // Utilise le mécanisme 'lazy' pour n'instancier l'objet qu'au moment de la première utilisation
    val authRepository: AuthRepository by lazy { AuthRepository(applicationContext) }
}