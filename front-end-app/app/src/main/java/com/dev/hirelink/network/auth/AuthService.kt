package com.dev.hirelink.network.auth

import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.models.ApplicationUserRequest
import com.dev.hirelink.modules.auth.dto.Credentials
import com.dev.hirelink.modules.auth.dto.EmployerCreatePasswordRequest
import com.dev.hirelink.modules.auth.dto.LoginResponse
import com.dev.hirelink.modules.auth.dto.OTPVerificationRequest
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @POST("auth/register")
    fun register(@Body user: ApplicationUserRequest): Single<ApplicationUser?>

    @POST("auth/verify-digit")
    fun verifyDigit(@Body otpVerificationRequest: OTPVerificationRequest): Single<ApplicationUser?>

    @PUT("auth/{id}/create-password")
    fun createPassword(@Path("id") id: Int, @Body payload: EmployerCreatePasswordRequest): Single<ApplicationUser?>
}