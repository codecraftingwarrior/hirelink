package com.dev.hirelink.network.auth
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.dto.Credentials
import com.dev.hirelink.modules.auth.dto.LoginResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

private const val basePath = "auth"
interface AuthService {

    @Headers("Accept: application/json", "Content-type: application/json")
    @POST("login-check")
    fun login(@Body credentials: Credentials): Single<LoginResponse?>

    @GET("users/current-user")
    fun fetchCurrentUser(): Single<ApplicationUser?>

    suspend fun register(@Body user: ApplicationUser): Call<ApplicationUser>
}