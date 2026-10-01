package com.alvaro.seguimientodegarantiasyserviciostecnicos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.login.LoginActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.clientes.ClientesActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.garantias.GarantiasActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.serviciostecnicos.ServiciosTecnicosActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (com.google.firebase.auth.FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            return
        }
        setContentView(R.layout.activity_dashboard)
        title = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email ?: "Sin sesión"
        findViewById<android.view.View>(R.id.botonClientes).setOnClickListener { open(ClientesActivity::class.java) }
        findViewById<android.view.View>(R.id.botonGarantias).setOnClickListener { open(GarantiasActivity::class.java) }
        findViewById<android.view.View>(R.id.botonServiciosTecnicos).setOnClickListener { open(ServiciosTecnicosActivity::class.java) }
        findViewById<android.view.View>(R.id.botonCerrarSesion).setOnClickListener {
            com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
    }

    private fun open(activity: Class<out AppCompatActivity>) {
        startActivity(Intent(this, activity))
    }
}


