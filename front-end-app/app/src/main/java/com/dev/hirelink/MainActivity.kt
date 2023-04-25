package com.dev.hirelink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import com.dev.hirelink.modules.auth.login.LoginActivity
import com.dev.hirelink.modules.auth.register.RegisterActivity
import com.dev.hirelink.modules.core.BaseActivity

// Classe MainActivity héritant de AppCompatActivity
class MainActivity : AppCompatActivity() {

    // Méthode onCreate appelée lors de la création de l'activité
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Définition du layout de l'activité
        setContentView(R.layout.activity_main)

        // Cache la barre d'action
        supportActionBar?.hide()

        // Appelle de la méthode bindListeners pour associer les écouteurs d'événements
        bindListeners();
    }

    // Méthode pour associer les écouteurs d'événements aux boutons
    private fun bindListeners() {
        // Bouton d'inscription
        val registerButton = findViewById<AppCompatButton>(R.id.button_register)
        registerButton.setOnClickListener {
            // Naviguer vers l'activité RegisterActivity
            startActivity(
                Intent(
                    applicationContext,
                    RegisterActivity::class.java
                )
            )
        }

        // Bouton de connexion
        val loginButton = findViewById<AppCompatButton>(R.id.button_login)
        loginButton.setOnClickListener {
            // Naviguer vers l'activité LoginActivity
            startActivity(
                Intent(
                    applicationContext,
                    LoginActivity::class.java
                )
            )
        }

        // Bouton pour afficher les offres
        val viewOffersButton = findViewById<Button>(R.id.button_view_offers)
        viewOffersButton.setOnClickListener {
            // Naviguer vers l'activité BaseActivity
            startActivity(
                Intent(
                    this,
                    BaseActivity::class.java
                )
            )
        }

    }
}