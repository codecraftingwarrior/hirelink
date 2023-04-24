package com.dev.hirelink.network

import okhttp3.Interceptor
import okhttp3.Response

// Déclaration de la classe JWTTokenInterceptor implémentant l'interface Interceptor d'OkHttp
class JWTTokenInterceptor(private val token: String) : Interceptor {

    // La méthode intercept est appelée pour chaque requête HTTP effectuée par l'application
    override fun intercept(chain: Interceptor.Chain): Response {

        // Récupération de la requête originale
        val original = chain.request()

        // Si la requête concerne la connexion ou l'authentification, on la laisse passer sans ajouter de token
        if (original.url.encodedPath.contains("/login-check") || original.url.encodedPath.contains("/auth")) {
            return chain.proceed(original)
        }

        // Récupération de l'URL de la requête originale
        val originalHttpUrl = original.url

        // Construction d'une nouvelle requête avec l'ajout du header "Authorization" contenant le token JWT
        val requestBuilder = original
            .newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .url(originalHttpUrl)

        // Création de la nouvelle requête
        val request = requestBuilder.build()

        // Poursuite de l'exécution de la chaîne d'intercepteurs avec la nouvelle requête
        return chain.proceed(request)
    }
}