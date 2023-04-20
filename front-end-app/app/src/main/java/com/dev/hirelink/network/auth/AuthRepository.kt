package com.dev.hirelink.network.auth

import android.content.Context
import com.dev.hirelink.components.RetrofitServiceBuilder
import com.dev.hirelink.models.ApplicationUser
import com.dev.hirelink.modules.auth.dto.Credentials
import com.dev.hirelink.modules.auth.dto.LoginResponse
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class AuthRepository(private val context: Context) {

    // BehaviorSubject pour gérer et émettre les informations sur l'utilisateur courant
    val currentUser: BehaviorSubject<ApplicationUser?> = BehaviorSubject.createDefault(ApplicationUser())

    // Création d'une instance lazy de AuthService à l'aide de RetrofitServiceBuilder
    private val service: AuthService by lazy { RetrofitServiceBuilder(context).create(AuthService::class.java) }

    // La méthode login prend en argument un objet Credentials et utilise le service AuthService pour effectuer la requête de connexion
    fun login(credentials: Credentials): Single<LoginResponse?> = service.login(credentials)

    // La méthode fetchCurrentUser utilise le service AuthService pour récupérer les informations de l'utilisateur courant
    fun fetchCurrentUser(): Single<ApplicationUser?> = service.fetchCurrentUser()

    // La méthode emitUser prend en argument un objet ApplicationUser et émet les informations de
    // l'utilisateur à travers le BehaviorSubject currentUser
    fun emitUser(user: ApplicationUser) = currentUser.onNext(user)
}