package com.dev.hirelink.network.auth

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.dto.Credentials
import com.dev.hirelink.modules.auth.dto.LoginResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call


class AuthRepository(private val context: Context) {
    val currentUser: BehaviorSubject<ApplicationUser?> = BehaviorSubject.createDefault(ApplicationUser())

    private val service: AuthService by lazy { RetrofitServiceBuilder(context).create(AuthService::class.java) }

    fun login(credentials: Credentials): Single<LoginResponse?> = service.login(credentials)

    fun fetchCurrentUser(): Single<ApplicationUser?> = service.fetchCurrentUser()

    fun emitUser(user: ApplicationUser) = currentUser.onNext(user)
}