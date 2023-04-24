package com.dev.hirelink.network.auth

import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.dto.Credentials
import com.dev.hirelink.modules.auth.dto.LoginResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

// Définition d'une constante pour le chemin de base des requêtes d'authentification
private const val basePath = "auth"
interface AuthService {

    // Déclaration de la méthode login avec une annotation POST pour envoyer une requête POST
    // et l'annotation Headers pour définir les en-têtes de la requête
    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("login-check")
    fun login(@Body credentials: Credentials): Single<LoginResponse?>

    // Déclaration de la méthode fetchCurrentUser avec une annotation GET pour envoyer une requête GET
    @GET("users/current-user")
    fun fetchCurrentUser(): Single<ApplicationUser?>

    // Déclaration de la méthode register avec une annotation POST pour envoyer une requête POST
    // Cette méthode est déclarée en utilisant le mot-clé "suspend" pour la prise en charge de la coroutines
    suspend fun register(@Body user: ApplicationUser): Call<ApplicationUser>
}