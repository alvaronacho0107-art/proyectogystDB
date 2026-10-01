package com.alvaro.seguimientodegarantiasyserviciostecnicos.ui.register

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvaro.seguimientodegarantiasyserviciostecnicos.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.analytics.FirebaseAnalytics

class RegistroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private var procesando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Formulario Registro"
        FirebaseAnalytics.getInstance(this).logEvent("Formulario_registro",
            Bundle().apply { putString("Mensaje", "Entro_al_registro") })
        binding.btnCrearRegistro.setOnClickListener { crearRegistro() }
    }

    private fun crearRegistro() {
        if (procesando) return
        val correo = binding.txtCorreoR.text.toString().trim()
        val password = binding.txtPass1.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.txtCorreoR.error = "Ingresa un correo válido"
            return
        }
        if (password.length < 6) {
            binding.txtPass1.error = "Usa al menos 6 caracteres"
            return
        }
        setProcesando(true)
        auth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener { task ->
            if (isFinishing || isDestroyed) return@addOnCompleteListener
            setProcesando(false)
            val usuario = if (task.isSuccessful) task.result?.user else null
            if (usuario != null) {
                Log.d("EmailPassword", "createUserWithEmail:success")


                auth.signOut()
                Toast.makeText(this, "Usuario ha sido creado", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                setProcesando(false)
                Log.w("RegistroActivity", "Error creando cuenta", task.exception)
                Toast.makeText(this, "No se pudo crear la cuenta. Revisa la conexión, el correo y la configuración de Authentication.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setProcesando(valor: Boolean) {
        procesando = valor
        binding.btnCrearRegistro.isEnabled = !valor
        binding.txtCorreoR.isEnabled = !valor
        binding.txtPass1.isEnabled = !valor
    }
}

