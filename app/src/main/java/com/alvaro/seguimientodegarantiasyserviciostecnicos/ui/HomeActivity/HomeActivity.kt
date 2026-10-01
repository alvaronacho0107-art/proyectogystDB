package com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.HomeActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.alvaro.seguimientodegarantiasyserviciostecnicos.R
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        title = getUsuarioActual()

        findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            cerrarSesion()
            finish()
            }
        }

    fun cerrarSesion() {
        FirebaseAuth.getInstance().signOut()
        }

    private fun getUsuarioActual(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.email ?: "Sin sesión"
        }
}
